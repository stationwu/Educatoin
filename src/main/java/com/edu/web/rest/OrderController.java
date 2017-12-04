package com.edu.web.rest;

import com.edu.config.WechatPayProperties;
import com.edu.dao.CouponRepository;
import com.edu.dao.CustomerRepository;
import com.edu.dao.OrderRepository;
import com.edu.dao.PaymentRepository;
import com.edu.domain.Coupon;
import com.edu.domain.Customer;
import com.edu.domain.Order;
import com.edu.domain.Payment;
import com.edu.domain.dto.OrderContainer;
import com.edu.errorhandler.InvalidOrderException;
import com.edu.errorhandler.PaymentException;
import com.edu.errorhandler.RequestDeniedException;
import com.edu.utils.*;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyCoupon;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceAbstractImpl;
import com.github.binarywang.wxpay.util.SignUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.edu.utils.WxPayConstants.DEVICE_WEB;
import static com.edu.utils.WxPayConstants.FEE_TYPE_CNY;
import static com.edu.utils.WxPayConstants.TRADE_TYPE_MP;

@RestController
public class OrderController {
    public static final String PATH = "/api/v1/Order";

    public static final String NOTIFY_PATH = "/api/v1/wxpay/notify";

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private WxPayService wxPayService;

    @Autowired
    private WebUtils webUtils;

    @Autowired
    private WxTimeStampUtil wxTimeStampUtil;

    @Autowired
    private WechatPayProperties wxPayProperties;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping(PATH)
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

    @PostMapping(PATH + "/{id}/initiatePay")
    public WxPayMpOrderResult initiatePay(@PathVariable("id") long id,
                                          HttpServletRequest request) {
        String openId = (String)request.getSession().getAttribute(Constant.SESSION_OPENID_KEY);
        Order order = orderRepository.findOne(id);

        if (order == null) {
            throw new InvalidOrderException("Order " + id + " does not exist");
        }

        String clientIpAddr = webUtils.getClientIp(request);
        String hostUrl = URLUtil.getHost(request.getRequestURL().toString());

        WxPayUnifiedOrderRequest payRequest = WxPayUnifiedOrderRequest.newBuilder()
                .feeType(FEE_TYPE_CNY)
                .tradeType(TRADE_TYPE_MP)
                .outTradeNo(String.valueOf(id))
                .deviceInfo(DEVICE_WEB)
                .spbillCreateIp(clientIpAddr)
                .notifyURL(hostUrl + NOTIFY_PATH)
                .timeStart(wxTimeStampUtil.getCurrentTimeStamp())
                .timeExpire(wxTimeStampUtil.builder().now().afterMinutes(wxPayProperties.getExpiryInMinutes()).build())
                .openid(openId)
                .build();

        WxPayMpOrderResult payResult = null;
        try {
            // In version 2.8.0 this API is not exposed via interface
            payResult = ((WxPayServiceAbstractImpl)wxPayService).createOrder(payRequest);
        } catch (WxPayException e) {
            throw new PaymentException("创建预付单失败", e);
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

        return payResult;
    }

    @PostMapping(NOTIFY_PATH)
    public void onNotify(@RequestBody String xmlData) {
        WxPayOrderNotifyResult result = null;

        try {
            result = wxPayService.parseOrderNotifyResult(xmlData);
        } catch (WxPayException e) {
            logger.error("异步接受微信支付结果通知时收到了无效的XML数据： {}", xmlData);
            logger.error("错误详细信息： {}", e.getMessage());
            throw new PaymentException("无效数据", e);
        }

        long orderId = Long.parseLong(result.getOutTradeNo());
        Order order = orderRepository.findOne(orderId);

        if (order == null) {
            throw new InvalidOrderException("Order " + orderId + " does not exist");
        }

        Payment payment = order.getPayment();

        payment.setOrder(order);
        payment.setDeviceInfo(result.getDeviceInfo());
        payment.setFeeType(result.getFeeType());
        payment.setTradeType(result.getTradeType());
        payment.setOpenId(result.getOpenid());
        payment.setTransactionId(result.getTransactionId());
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
        for (WxPayOrderNotifyCoupon couponUsed: result.getCouponList()) {
            Coupon coupon = new Coupon();
            coupon.setCouponIndex(++index);
            coupon.setCouponFee(couponUsed.getCouponFee());
            coupon.setCouponId(couponUsed.getCouponId());
            coupon.setCouponType(couponUsed.getCouponType());
            coupon.setPayment(payment);
            coupon = couponRepository.save(coupon);
            payment.addCoupon(coupon);
        }

        paymentRepository.save(payment);

        order.setStatus(Order.Status.PAID);
        orderRepository.save(order);
    }
}
