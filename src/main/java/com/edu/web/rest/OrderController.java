package com.edu.web.rest;

import com.edu.config.WechatPayProperties;
import com.edu.dao.CustomerRepository;
import com.edu.dao.OrderRepository;
import com.edu.dao.PaymentRepository;
import com.edu.domain.Customer;
import com.edu.domain.Order;
import com.edu.domain.dto.OrderContainer;
import com.edu.errorhandler.InvalidOrderException;
import com.edu.errorhandler.RequestDeniedException;
import com.edu.utils.Constant;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.service.WxPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class OrderController {
    public static final String PATH = "/api/v1/Order";

    public static final String NOTIFY_PATH = "/public/wxpay_notify";

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private WxPayService wxPayService;

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
    public WxPayUnifiedOrderResult initiatePay(@PathVariable("id") long id) {
        Order order = orderRepository.findOne(id);

        if (order == null) {
            throw new InvalidOrderException("Order " + id + " does not exist");
        }

        WxPayUnifiedOrderRequest request = WxPayUnifiedOrderRequest.newBuilder()
                .appid(wxPayProperties.getAppId())
                .mchId(wxPayProperties.getMchId())
                .nonceStr(String.valueOf(System.currentTimeMillis()))
                .feeType("CNY")
                .outTradeNo(String.valueOf(id))
                .deviceInfo("WEB")
                .build();


        WxPayUnifiedOrderResult result = wxPayService.unifiedOrder(request);

        return result;
    }

    @GetMapping(NOTIFY_PATH)
    public void onNotify() {

    }
}
