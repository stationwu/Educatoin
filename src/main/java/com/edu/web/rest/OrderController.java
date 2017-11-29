package com.edu.web.rest;

import com.edu.dao.CustomerRepository;
import com.edu.dao.OrderRepository;
import com.edu.domain.Customer;
import com.edu.domain.dto.OrderContainer;
import com.edu.errorhandler.RequestDeniedException;
import com.edu.utils.Constant;
import com.github.binarywang.wxpay.service.WxPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class OrderController {
    public static final String PATH = "/api/v1/Order";

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private WxPayService wxPayService;

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
}
