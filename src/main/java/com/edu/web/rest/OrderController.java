package com.edu.web.rest;

import com.edu.config.WechatPayProperties;
import com.edu.dao.*;
import com.edu.domain.*;
import com.edu.domain.dto.OrderContainer;
import com.edu.domain.dto.ProductContainer;
import com.edu.errorhandler.RequestDeniedException;
import com.edu.utils.*;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyCoupon;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayBaseRequest;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayOrderCloseResult;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceAbstractImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.edu.utils.WxPayConstants.*;
import static com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse.fail;
import static com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse.success;

@Controller
public class OrderController {
    public static final String PATH = "/api/v1/Order";
    public static final String PAYMENT_NOTIFY_PATH = "/api/v1/pay/notify";
    public static final String PAY_PATH = PATH + "/{id}/initiatePay";
    public static final String CANCEL_PATH = PATH + "/{id}/cancelPay";
    public static final String REQ_REFUND_PATH = PATH + "/{id}/requestRefund";
    public static final String REFUND_PATH = "/manager/api/v1/order/{id}/refund";
    public static final String REFUND_NOTIFY_PATH = "/api/v1/refund/notify";
    public static final String PAY_RESULT_VIEW = "/paymentResult/{orderId}";

    public static final String PAY_TEST_PATH = "/pay";

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private RefundRepository refundRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private DerivedProductRepository derivedProductRepository;

    @Autowired
    private ImageCollectionRepository imageCollectionRepository;

    @Autowired
    private ClassProductRepository classProductRepository;

    @Autowired
    private ProductCartRepository cartRepository;

    @Autowired
    private WxPayService wxPayService;

    @Autowired
    private WebUtils webUtils;

    @Autowired
    private WxTimeStampUtil wxTimeStampUtil;

    @Autowired
    private WechatPayProperties wxPayProperties;

    @Autowired
    private WxPayConversionUtil wxPayConvUtil;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ResponseBody
    @RequestMapping(PATH)
    public List<OrderContainer> listOrders(HttpSession session) {
        String openId = (String) session.getAttribute(Constant.SESSION_OPENID_KEY);

        Customer customer = customerRepository.findOneByOpenCode(openId);

        if (customer == null) {
            throw new RequestDeniedException("You're not allowed to access the requested resource");
        }

        return customer.getOrders().stream()
                .map(x -> new OrderContainer(x, x.getProductsMap(), x.getDerivedProductsMap(),
                        x.getImageCollectionMap(), x.getClassProductsMap()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @ResponseBody
    @RequestMapping(value = PATH, method = RequestMethod.POST)
    public OrderContainer placeOrder(@RequestBody List<ProductContainer> products, HttpSession session) {
        String openId = (String)session.getAttribute(Constant.SESSION_OPENID_KEY);

        Customer customer = customerRepository.findOneByOpenCode(openId);

        ProductCart cart = customer.getCart();
        Order order = new Order();
        Map<Product, Integer> productMap = new HashMap<>();
        Map<DerivedProduct, Integer> derivedProductMap = new HashMap<>();
        Map<ImageCollection, Integer> imageCollectionMap = new HashMap<>();
        Map<ClassProduct, Integer> classProductMap = new HashMap<>();
        double amount = 0d;
        for (ProductContainer productContainer : products) {
            switch (productContainer.getType()) {
                case 1:
                    Product product = productRepository.findOne(productContainer.getId());
                    productMap.put(product, productContainer.getQuantity());
                    amount += productContainer.getProductPrice() * productContainer.getQuantity();
                    cart.removeProduct(product);
                    break;
                case 2:
                    DerivedProduct derivedProduct = derivedProductRepository.findOne(productContainer.getId());
                    derivedProductMap.put(derivedProduct, productContainer.getQuantity());
                    amount += productContainer.getProductPrice() * productContainer.getQuantity();
                    cart.removeDerivedProduct(derivedProduct);
                    break;
                case 3:
                    ImageCollection imageCollection = imageCollectionRepository.findOne(productContainer.getId());
                    imageCollectionMap.put(imageCollection, productContainer.getQuantity());
                    amount += productContainer.getProductPrice() * productContainer.getQuantity();
                    cart.removeImageCollection(imageCollection);
                    break;
                case 4:
                    ClassProduct classProduct = classProductRepository.findOne(productContainer.getId());
                    classProductMap.put(classProduct, productContainer.getQuantity());
                    amount += productContainer.getProductPrice() * productContainer.getQuantity();
                    cart.removeClassProduct(classProduct);
                    break;
            }
        }

        cartRepository.save(cart);

        order.setProductsMap(productMap);
        order.setDerivedProductsMap(derivedProductMap);
        order.setImageCollectionMap(imageCollectionMap);
        order.setClassProductsMap(classProductMap);
        order.setTotalAmount(amount);
        LocalDate localDate = LocalDate.now();
        order.setDate(localDate.toString());
        order.setCustomer(customer);
        order.setStatus(Order.Status.CREATED);
        orderRepository.save(order);

        return new OrderContainer(
                order,
                order.getProductsMap(),
                order.getDerivedProductsMap(),
                order.getImageCollectionMap(),
                order.getClassProductsMap()
        );
    }

    @ResponseBody
    @RequestMapping(value = PAY_TEST_PATH, method = RequestMethod.POST)
    public PayResult testPay(@RequestParam("orderId") long orderId, HttpServletRequest request) {
        String openId = (String) request.getSession().getAttribute(Constant.SESSION_OPENID_KEY);
        Order order = orderRepository.findOne(orderId);

        if (order == null) {
            return PayResult.fail( "订单号（" + orderId + "）不存在" );
        }
        if (order.getStatus() != Order.Status.CREATED && order.getStatus() != Order.Status.NOTPAY) {
            return PayResult.fail( "订单（" + orderId + "）已关闭，无法继续支付" );
        }

        WxPayUnifiedOrderRequest payRequest = WxPayUnifiedOrderRequest.newBuilder()
                //.appid()
                .outTradeNo(wxPayConvUtil.toOutTradeNo(order.getId()))
                .openid(openId)
                .body(buildBody(order))
                .spbillCreateIp(webUtils.getClientIp(request))
                .timeStart(wxTimeStampUtil.getCurrentTimeStamp())
                .timeExpire(wxTimeStampUtil.builder().now().afterMinutes(wxPayProperties.getExpiryInMinutes()).build())
                .notifyURL(URLUtil.getHostUrl(request) + PAYMENT_NOTIFY_PATH)
                .totalFee(WxPayBaseRequest.yuanToFee(String.valueOf(order.getTotalAmount()))) // 198.99 -> "198.99" -> 19899
                .build();

        WxPayMpOrderResult payResult = null;
        try {
            payResult = wxPayService.createOrder(payRequest);
        } catch (WxPayException e) {
            return PayResult.fail("创建预付单失败, 原因:{" + e.getMessage() + "}");
        }

        Payment payment;
        if (order.getPayment() != null) {
            payment = order.getPayment();
        } else {
            payment = new Payment();
        }

        payment.setTimeStart(payRequest.getTimeStart());
        payment.setSpBillCreateIp(payRequest.getSpbillCreateIp());
        payment = paymentRepository.save(payment);

        order.setPayment(payment);
        order.setStatus(Order.Status.NOTPAY);
        orderRepository.save(order);

        return PayResult.ok(payResult);
    }

    @ResponseBody
    @RequestMapping(value = PAY_PATH, method = RequestMethod.POST)
    public PayResult pay(@PathVariable("id") long id, HttpServletRequest request) {
        String openId = (String) request.getSession().getAttribute(Constant.SESSION_OPENID_KEY);
        Order order = orderRepository.findOne(id);

        if (order == null) {
            return PayResult.fail( "订单号（" + id + "）不存在" );
        }
        if (order.getStatus() != Order.Status.CREATED && order.getStatus() != Order.Status.NOTPAY) {
            return PayResult.fail( "订单（" + id + "）已关闭，无法继续支付" );
        }

        WxPayUnifiedOrderRequest payRequest = WxPayUnifiedOrderRequest.newBuilder()
                .outTradeNo(wxPayConvUtil.toOutTradeNo(order.getId()))
                .openid(openId)
                .body(buildBody(order))
                .spbillCreateIp(webUtils.getClientIp(request))
                .timeStart(wxTimeStampUtil.getCurrentTimeStamp())
                .timeExpire(wxTimeStampUtil.builder().now().afterMinutes(wxPayProperties.getExpiryInMinutes()).build())
                .notifyURL(URLUtil.getHostUrl(request) + PAYMENT_NOTIFY_PATH)
                .totalFee(WxPayBaseRequest.yuanToFee(String.valueOf(order.getTotalAmount()))) // 198.99 -> "198.99" -> 19899
                .build();

        WxPayMpOrderResult payResult = null;
        try {
            payResult = wxPayService.createOrder(payRequest);
        } catch (WxPayException e) {
            return PayResult.fail("创建预付单失败, 原因:{" + e.getMessage() + "}");
        }

        Payment payment;
        if (order.getPayment() != null) {
            payment = order.getPayment();
        } else {
            payment = new Payment();
        }

        payment.setTimeStart(payRequest.getTimeStart());
        payment.setSpBillCreateIp(payRequest.getSpbillCreateIp());
        payment = paymentRepository.save(payment);

        order.setPayment(payment);
        order.setStatus(Order.Status.NOTPAY);
        orderRepository.save(order);

        return PayResult.ok(payResult);
    }

    @ResponseBody
    @RequestMapping(value = PAYMENT_NOTIFY_PATH, method = RequestMethod.POST)
    public String onPaymentNotify(@RequestBody String xmlData) {
        WxPayOrderNotifyResult result = null;

        try {
            result = wxPayService.parseOrderNotifyResult(xmlData);
        } catch (WxPayException e) {
            return WxPayNotifyResponse.fail("无效请求 " + e.getMessage());
        }

        long orderId = Long.parseLong(result.getOutTradeNo());
        Order order = orderRepository.findOne(orderId);

        if (order == null) {
            return fail("订单号（" + orderId + "）不存在");
        }
        if (order.getStatus() != Order.Status.NOTPAY) {
            return fail("订单（" + orderId + "）状态错误（" + order.getStatusText() + "），无法继续支付");
        }

        Payment payment = order.getPayment();

        // Important -->
        payment.setOrder(order);
        payment.setTransactionId(result.getTransactionId());
        payment.setOpenId(result.getOpenid());
        // <--
        // Not so important -->
        payment.setDeviceInfo(result.getDeviceInfo());
        payment.setFeeType(result.getFeeType());
        payment.setTradeType(result.getTradeType());
        payment.setAttach(result.getAttach());
        payment.setBankType(result.getBankType());
        payment.setTimeEnd(result.getTimeEnd());
        payment.setIsSubsribe(result.getIsSubscribe());
        payment.setCashFee(result.getCashFee());
        payment.setCashFeeType(result.getCashFeeType());
        payment.setTotalFee(result.getTotalFee());
        payment.setSettlementTotalFee(result.getSettlementTotalFee());
        payment.setCouponFee(result.getCouponFee());
        payment.setCouponCount(result.getCouponCount());

        int index = 0;
        for (WxPayOrderNotifyCoupon couponUsed : result.getCouponList()) {
            Coupon coupon = new Coupon();
            coupon.setCouponIndex(++index);
            coupon.setCouponFee(couponUsed.getCouponFee());
            coupon.setCouponId(couponUsed.getCouponId());
            coupon.setCouponType(couponUsed.getCouponType());
            coupon.setPayment(payment);
            coupon = couponRepository.save(coupon);
            payment.addCoupon(coupon);
        }
        // <--

        paymentRepository.save(payment);

        order.setStatus(Order.Status.PAID);
        orderRepository.save(order);

        return success("支付完成");
    }

    @RequestMapping(value = CANCEL_PATH, method = RequestMethod.POST)
    public ResponseEntity<String> cancelPay(@PathVariable("id") long id) {
        Order order = orderRepository.findOne(id);

        if (order == null) {
            return new ResponseEntity<>("订单号（" + id + "）不存在", HttpStatus.NOT_FOUND);
        }
        if (order.getStatus() != Order.Status.CREATED && order.getStatus() != Order.Status.NOTPAY) {
            return new ResponseEntity<>("订单（" + id + "）已关闭，无法取消", HttpStatus.BAD_REQUEST);
        }

        order.setStatus(Order.Status.CANCELLED);
        orderRepository.save(order);

        WxPayOrderCloseResult result;

        try {
            result = wxPayService.closeOrder(String.valueOf(id));
        } catch (WxPayException e) {
            return new ResponseEntity<>("取消付款时发生了错误", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result.toString(), HttpStatus.OK);
    }

    @RequestMapping(value = REQ_REFUND_PATH, method = RequestMethod.POST)
    public ResponseEntity<String> requestRefund(@PathVariable("id") long id) {
        Order order = orderRepository.findOne(id);

        if (order == null) {
            return new ResponseEntity<>("订单号（" + id + "）不存在", HttpStatus.NOT_FOUND);
        }
        if (order.getStatus() != Order.Status.PAID) {
            return new ResponseEntity<>("订单（" + id + "）未支付，无法申请退款", HttpStatus.BAD_REQUEST);
        }

        order.setStatus(Order.Status.REFUND_REQUESTED);
        orderRepository.save(order);

        //TODO: Mark it as not supported at the moment
        return new ResponseEntity<>("尚不支持退款", HttpStatus.NOT_IMPLEMENTED);
    }

    @Transactional
    @RequestMapping(value = REFUND_PATH, method = RequestMethod.POST)
    public ResponseEntity<String> refund(@PathVariable("id") long id) {
        Order order = orderRepository.findOne(id);

        if (order == null) {
            return new ResponseEntity<>("订单 #" + id + " 不存在", HttpStatus.NOT_FOUND);
        }
        if (order.getStatus() != Order.Status.REFUND_REQUESTED) {
            return new ResponseEntity<>("订单 #" + id + " 未申请退款", HttpStatus.BAD_REQUEST);
        }

        Payment payment = order.getPayment();
        if (payment == null) {
            return new ResponseEntity<>("订单 #" + id + " 尚未付款", HttpStatus.NOT_FOUND);
        }

        // Lock entry to prevent two people approve refund at same time
        paymentRepository.findOneForUpdate(payment.getId());

        Refund refund;
        if (payment.getRefund() != null) { // Already requested once
            refund = payment.getRefund();
        } else {
            refund = new Refund();
            refund.setPayment(payment);
            refund.setCashFee(payment.getCashFee());
            refund.setCashFeeType(payment.getCashFeeType());
            refund = refundRepository.save(refund);
        }

        WxPayRefundRequest refundRequest = WxPayRefundRequest.newBuilder()
                .outTradeNo(String.valueOf(order.getId()))
                .outRefundNo(String.valueOf(refund.getId()))
                .refundFee(WxPayBaseRequest.yuanToFee(String.valueOf(order.getTotalAmount())))
                .refundFeeType(FEE_TYPE_CNY)
                // .transactionId(order.getPayment().getTransactionId()) // 与out_trade_no二选一
                .totalFee(WxPayBaseRequest.yuanToFee(String.valueOf(order.getTotalAmount())))
                .deviceInfo(DEVICE_WEB)
                .refundDesc("商户订单（" + order.getId() + "）微信订单号（" + order.getPayment().getTransactionId() + "）请求退款")
                .build();

        WxPayRefundResult result;

        try {
            result = wxPayService.refund(refundRequest);
        } catch (WxPayException e) {
            return new ResponseEntity<>("退款失败, 原因:{" + e.getMessage() + "}", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        payment.setRefund(refund);
        paymentRepository.save(payment);

        return new ResponseEntity<>(result.toString(), HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = REFUND_NOTIFY_PATH, method = RequestMethod.POST)
    public String onRefundNotify(@RequestBody String xmlData) {
        WxPayRefundNotifyResult result = null;

        try {
            result = wxPayService.parseRefundNotifyResult(xmlData);
        } catch (WxPayException e) {
            return WxPayNotifyResponse.fail("无效请求 " + e.getMessage());
        }

        WxPayRefundNotifyResult.ReqInfo reqInfo = result.getReqInfo();
        long orderId = Long.parseLong(reqInfo.getOutTradeNo());
        Order order = orderRepository.findOne(orderId);

        if (order == null) {
            return fail("订单号（" + orderId + "）不存在");
        }
        if (order.getStatus() != Order.Status.NOTPAY) {
            return fail("订单（" + orderId + "）状态错误（" + order.getStatusText() + "），无法继续支付");
        }

        Payment payment = order.getPayment();
        Refund refund = payment.getRefund();
        refund.setTotalFee(reqInfo.getTotalFee());
        refund.setSettlementRefundFee(reqInfo.getSettlementRefundFee());
        refund.setSettlementTotalFee(reqInfo.getSettlementTotalFee());
        refund.setRefundId(reqInfo.getRefundId());
        refund.setRefundFee(reqInfo.getRefundFee());

        refundRepository.save(refund);

        order.setStatus(Order.Status.REFUND);

        return success("退款完成");
    }

    @RequestMapping(value = PAY_RESULT_VIEW, method = RequestMethod.GET)
    public ModelAndView onSuccessfulPay(@PathVariable("orderId") long orderId) {
        ModelAndView mav = new ModelAndView();
        Order order = orderRepository.findOne(orderId);

        if (order == null) {
            mav.addObject("orderId", orderId);
            mav.addObject("errorMessage", "订单 #" + orderId + " 不存在");
            mav.addObject("hintMessage", "您可以到“我的订单”中检查您的订单。如果您已经支付，请联系客服人员。");
            mav.setViewName("payment_error");
            return mav;
        }
        if (order.getStatus() != Order.Status.PAID) {
            mav.addObject("orderId", orderId);
            mav.addObject("errorMessage", "订单 #" + orderId + " 支付未成功");
            mav.addObject("hintMessage", "您可以到“我的订单”中继续支付。如果您已经支付，请联系客服人员。");
            mav.setViewName("payment_error");
            return mav;
        }

        mav.addObject("orderId", orderId);
        mav.addObject("detailMessage", "订单 #" + orderId);
        mav.setViewName("payment_done");
        return mav;
    }


    private String buildBody(Order order) {
        List<String> list = new LinkedList<>();

        if (order.getProductsMap() != null && !order.getProductsMap().isEmpty()) {
            for (Map.Entry<Product, Integer> entry : order.getProductsMap().entrySet()) {
                list.add(entry.getKey().getProductName() + " x " + entry.getValue());
            }
        }
        if (order.getClassProductsMap() != null && !order.getClassProductsMap().isEmpty()) {
            for (Map.Entry<ClassProduct, Integer> entry : order.getClassProductsMap().entrySet()) {
                list.add(entry.getKey().getDescription());
            }
        }
        if (order.getDerivedProductsMap() != null && !order.getDerivedProductsMap().isEmpty()) {
            for (Map.Entry<DerivedProduct, Integer> entry : order.getDerivedProductsMap().entrySet()) {
                list.add(entry.getKey().getDescription() + " x " + entry.getValue());
            }
        }
        if (order.getImageCollectionMap() != null && !order.getImageCollectionMap().isEmpty()) {
            for (Map.Entry<ImageCollection, Integer> entry : order.getImageCollectionMap().entrySet()) {
                list.add(entry.getKey().getCollectionName() + " x " + entry.getValue());
            }
        }

        String body = String.join(", ", list);
        if (body.length() > 128) { // max length is 128 by Weixin service
            return body.substring(0, 126) + "等";
        } else {
            return body;
        }
    }

    /**
     * In order to response the JS client with a JSON object
     * instead of an XML string
     */
    public static class PayResult {
        private String timestamp;
        private String nonceStr;
        private String packageValue; // can't name it 'package' as it's keyword
        private String signType;
        private String paySign;
        private String message; // In case error happens
        private boolean success;

        private PayResult(WxPayMpOrderResult wxResult) {
            this.timestamp = wxResult.getTimeStamp();
            this.nonceStr = wxResult.getNonceStr();
            this.packageValue = wxResult.getPackageValue();
            this.signType = wxResult.getSignType();
            this.paySign = wxResult.getPaySign();
            this.success = true;
        }

        private PayResult(String message) {
            this.message = message;
            this.success = false;
        }

        public static PayResult ok(WxPayMpOrderResult wxResult) {
            return new PayResult(wxResult);
        }

        public static PayResult fail(String message) {
            return new PayResult(message);
        }

        public String getTimestamp() {
            return timestamp;
        }

        public String getNonceStr() {
            return nonceStr;
        }

        public String getPackage() {
            return packageValue;
        }

        public String getSignType() {
            return signType;
        }

        public String getPaySign() {
            return paySign;
        }

        public String getMessage() {
            return message;
        }

        public boolean isSuccess() {
            return success;
        }
    }
}
