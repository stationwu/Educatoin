package com.edu.web.rest;

import com.edu.dao.CustomerRepository;
import com.edu.dao.OrderRepository;
import com.github.binarywang.wxpay.service.WxPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
    public static final String PATH = "/api/v1/Order";

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private WxPayService wxPayService;


}
