package com.edu.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.edu.dao.*;
import com.edu.domain.*;
import com.edu.domain.dto.DerivedProduct;
import com.edu.domain.dto.OrderContainer;
import com.edu.domain.dto.ProductContainer;
import com.edu.utils.URLUtil;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import me.chanjar.weixin.mp.api.WxMpService;

@Controller
public class OrderCenterController {
    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private CustomerRepository custRepo;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private DerivedProductRepository derivedProductRepository;

    @Autowired
    private ImageCollectionRepository imageCollectionRepository;

    @Autowired
    private OrderRepository orderRepository;

    public final static String ORDER_LIST_PATH = "/user/orderList";

    public final static String ORDER_DETAIL_PATH = "/order/detail/{id}";

    public final static String ORDER_LIST_CALLBACK_PATH = "/user/orderList/cb";

    public final static String SESSION_OPENID_KEY = "openCode";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostMapping("/user/order")
    @ResponseBody
    public String createOrder(HttpServletRequest request,
        @RequestBody List<ProductContainer> products,
        Model model) {
        HttpSession session = request.getSession();
        Object openCodeObject = session.getAttribute("openCode");

        String openId = openCodeObject.toString();
        Customer customer = custRepo.findOneByOpenCode(openId);
        Order order = new Order();
        Map<Product, Integer> productMap = new HashMap<>();
        Map<DerivedProduct, Integer> derivedProductMap = new HashMap<>();
        Map<ImageCollection, Integer> imageCollectionMap = new HashMap<>();
        double amount = 0d;
        for (ProductContainer productContainer : products) {
            switch (productContainer.getType()) {
                case 1:
                    Product product = productRepository.findOne(productContainer
                            .getId());
                    productMap.put(product, productContainer.getQuantity());
                    amount += productContainer.getProductPrice()
                            * productContainer.getQuantity();
                    customer.getCart().removeProduct(product);
                    break;
                case 2:
                    DerivedProduct derivedProduct = derivedProductRepository
                            .findOne(productContainer.getId());
                    derivedProductMap.put(derivedProduct, productContainer
                            .getQuantity());
                    amount += productContainer.getProductPrice()
                            * productContainer.getQuantity();
                    customer.getCart().removeDerivedProduct(derivedProduct);
                    break;
                case 3:
                    ImageCollection imageCollection = imageCollectionRepository
                            .findOne(productContainer.getId());
                    imageCollectionMap.put(imageCollection, productContainer
                            .getQuantity());
                    amount += productContainer.getProductPrice()
                            * productContainer.getQuantity();
                    customer.getCart().removeImageCollection(imageCollection);
                    break;
            }
        }
        custRepo.save(customer);
        order.setProductsMap(productMap);
        order.setDerivedProductsMap(derivedProductMap);
        order.setImageCollectionMap(imageCollectionMap);
        order.setTotalAmount(amount);
        LocalDate localDate = LocalDate.now();
        order.setDate(localDate.toString());
        order.setCustomer(customer);
        orderRepository.save(order);
        return "下单成功";
    }

    @GetMapping(ORDER_DETAIL_PATH)
    public String orderDetail(@PathVariable(value = "id") Long id,
        HttpServletRequest request,
        Model model) {
        HttpSession session = request.getSession();
        Object openCodeObject = session.getAttribute("openCode");
        String openCode = openCodeObject.toString();
        Order foundOrder = orderRepo.findOne(id);
        if (null == foundOrder) {
            return "error_500";
        } else {
            model.addAttribute("detail", foundOrder);
            model.addAttribute("openCode", openCode);
            return "order_detail";
        }
    }

    @GetMapping(ORDER_LIST_PATH)
    public String orderList(HttpServletRequest request,
        HttpServletResponse response,
        HttpSession session,
        Model model) {
        Object openIdInSession = session.getAttribute(SESSION_OPENID_KEY);

        if (openIdInSession == null) { // OAuth to get OpenID
            String requestUrl = request.getRequestURL().toString();
            String hostAddress = URLUtil.getServiceURLBeforePath(requestUrl,
                    ORDER_LIST_PATH);
            String redirectUrl = hostAddress + ORDER_LIST_CALLBACK_PATH;
            String authorizationUrl = wxMpService.oauth2buildAuthorizationUrl(
                    redirectUrl, WxConsts.OAUTH2_SCOPE_BASE, "2");

            logger.debug(">>> Redirecting to " + authorizationUrl);

            try {
                response.sendRedirect(authorizationUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            List<OrderContainer> orders = getOrderList(
                    (String) openIdInSession);
            model.addAttribute("orderList", orders);
        }
        return "user_order";
    }

    @GetMapping(ORDER_LIST_CALLBACK_PATH)
    @ResponseBody
    public List<OrderContainer> orderListCallback(@RequestParam(
        value = "code") String authCode, HttpSession session) {
        String openId = null;

        try {
            Object openIdInSession = session.getAttribute(SESSION_OPENID_KEY);
            if (openIdInSession != null) {
                openId = (String) openIdInSession;
            } else {
                WxMpOAuth2AccessToken wxMpOAuth2AccessToken = wxMpService
                        .oauth2getAccessToken(authCode);
                openId = wxMpOAuth2AccessToken.getOpenId();
                session.setAttribute(SESSION_OPENID_KEY, openId);
            }
        } catch (WxErrorException e) {
            e.printStackTrace();
        }

        return getOrderList(openId);
    }

    private List<OrderContainer> getOrderList(String openId) {
        if (null == openId) {
            return new ArrayList<OrderContainer>();
        }

        Customer customer = custRepo.findOneByOpenCode(openId);
        return customer.getOrders().stream().map(x -> new OrderContainer(x, x
                .getProductsMap(), x.getDerivedProductsMap(), x
                        .getImageCollectionMap())).collect(Collectors
                                .toCollection(ArrayList::new));
    }
}
