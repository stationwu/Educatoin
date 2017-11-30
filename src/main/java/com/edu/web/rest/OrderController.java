package com.edu.web.rest;

import com.edu.config.WechatPayProperties;
import com.edu.dao.CustomerRepository;
import com.edu.dao.OrderRepository;
import com.edu.dao.PaymentRepository;
import com.edu.domain.Customer;
import com.edu.domain.Order;
import com.edu.domain.Payment;
import com.edu.domain.dto.OrderContainer;
import com.edu.errorhandler.InvalidOrderException;
import com.edu.errorhandler.PaymentException;
import com.edu.errorhandler.RequestDeniedException;
import com.edu.utils.*;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.util.SignUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class OrderController {
    public static final String PATH = "/api/v1/Order";

    public static final String NOTIFY_PATH = "/api/v1/wxpay/notify";

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentRepository paymentRepository;

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
                .feeType("CNY")
                .outTradeNo(String.valueOf(id))
                .deviceInfo("WEB")
                .spbillCreateIp(clientIpAddr)
                .notifyURL(hostUrl + NOTIFY_PATH)
                .timeStart(wxTimeStampUtil.getCurrentTimeStamp())
                .timeExpire(wxTimeStampUtil.builder().now().afterMinutes(wxPayProperties.getExpiryInMinutes()).build())
                .openid(openId)
                .build();

        WxPayUnifiedOrderResult unifiedOrderResult = null;
        try {
            unifiedOrderResult = wxPayService.unifiedOrder(payRequest);
        } catch (WxPayException e) {
            throw new PaymentException("创建预付单失败", e);
        }

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPrepayId(unifiedOrderResult.getPrepayId());
        paymentRepository.save(payment);

        order.setPayment(payment);
        order.setStatus(Order.Status.NOTPAY);
        orderRepository.save(order);

        String timestamp = String.valueOf(System.currentTimeMillis() / 1000L);
        String nonceStr = String.valueOf(System.currentTimeMillis());
        WxPayMpOrderResult payResult = WxPayMpOrderResult.newBuilder().appId(unifiedOrderResult.getAppid()).timeStamp(timestamp).nonceStr(nonceStr).packageValue("prepay_id=" + unifiedOrderResult.getPrepayId()).signType("MD5").build();
        payResult.setPaySign(SignUtils.createSign(payResult, wxPayService.getConfig().getMchKey(), (String)null));

        return payResult;
    }

    @PostMapping(NOTIFY_PATH)
    public void onNotify(@RequestBody String notifyData) {
        Map<String, String> notifyMap = null;

        try {
            notifyMap = WxPayUtil.xmlToMap(notifyData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
