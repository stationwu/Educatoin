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

import com.edu.utils.URLUtil;
import com.edu.utils.WxUserOAuthHelper;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.edu.dao.DerivedProductRepository;
import com.edu.dao.ImageCollectionRepository;
import com.edu.dao.OrderRepository;
import com.edu.dao.ProductRepository;
import com.edu.dao.StudentRepository;
import com.edu.domain.DerivedProduct;
import com.edu.domain.ImageCollection;
import com.edu.domain.Order;
import com.edu.domain.OrderContainer;
import com.edu.domain.Product;
import com.edu.domain.ProductContainer;
import com.edu.domain.Student;

import me.chanjar.weixin.mp.api.WxMpService;

@Controller
public class OrderCenterController {
	@Autowired
	private WxMpService wxMpService;

	@Autowired
	private StudentRepository repository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private DerivedProductRepository derivedProductRepository;

	@Autowired
	private ImageCollectionRepository imageCollectionRepository;

	@Autowired
	private OrderRepository orderRepository;

    public final static String ORDER_LIST_PATH = "/user/orderList";
    public final static String ORDER_LIST_CALLBACK_PATH = "/user/orderList/cb";

    public final static String SESSION_OPENID_KEY = "openCode";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@PostMapping("/user/order")
	@ResponseBody
	public String createOrder(HttpServletRequest request,
			@RequestBody List<ProductContainer> products, Model model) {
		HttpSession session = request.getSession();
		Object openCodeObject = session.getAttribute("openCode");

		if (null == openCodeObject) {
			return "error_500";
		}

		String authCode = openCodeObject.toString();
		Student student = repository.findOneByOpenCode(authCode);
		if (student == null) {
			Student newStudent = new Student();
			newStudent.setOpenCode(authCode);
			model.addAttribute("student", newStudent);
			return "user_signup";
		} else {
			Order order = new Order();
			Map<Product, Integer> productMap = new HashMap<>();
			Map<DerivedProduct, Integer> derivedProductMap = new HashMap<>();
			Map<ImageCollection, Integer> imageCollectionMap = new HashMap<>();
			double amount = 0d;
			for (ProductContainer productContainer : products) {
				switch (productContainer.getType()) {
				case 1:
					productMap.put(productRepository.findOne(productContainer.getId()), productContainer.getQuantity());
					amount += productContainer.getProductPrice() * productContainer.getQuantity();
					break;
				case 2:
					derivedProductMap.put(derivedProductRepository.findOne(productContainer.getId()),
							productContainer.getQuantity());
					amount += productContainer.getProductPrice() * productContainer.getQuantity();
					break;
				case 3:
					imageCollectionMap.put(imageCollectionRepository.findOne(productContainer.getId()),
							productContainer.getQuantity());
					amount += productContainer.getProductPrice() * productContainer.getQuantity();
					break;
				}
			}
			order.setProductsMap(productMap);
			order.setDerivedProductsMap(derivedProductMap);
			order.setImageCollectionMap(imageCollectionMap);
			order.setTotalAmount(amount);
			LocalDate localDate = LocalDate.now();
			order.setDate(localDate.toString());
			order.setStudent(student);
			orderRepository.save(order);
			return "下单成功";
		}
	}

	@GetMapping(ORDER_LIST_PATH)
    @ResponseBody
    public List<OrderContainer> orderList(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        Object openIdInSession = session.getAttribute(SESSION_OPENID_KEY);

        if (openIdInSession == null) { // OAuth to get OpenID
            String requestUrl = request.getRequestURL().toString();
            String hostAddress = URLUtil.getServiceURLBeforePath(requestUrl, ORDER_LIST_PATH);
            String redirectUrl = hostAddress + ORDER_LIST_CALLBACK_PATH;
            String authorizationUrl = wxMpService.oauth2buildAuthorizationUrl(redirectUrl, WxConsts.OAUTH2_SCOPE_BASE, "2");

            logger.debug(">>> Redirecting to " + authorizationUrl);

            try {
                response.sendRedirect(authorizationUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            return getOrderList((String) openIdInSession);
        }

        return null; // Ugly and probably not working //TODO: Fix this
    }

    @GetMapping(ORDER_LIST_CALLBACK_PATH)
    @ResponseBody
    public List<OrderContainer> orderListCallback(@RequestParam(value="code") String authCode, HttpSession session) {
        String openId = null;

        try {
            Object openIdInSession = session.getAttribute(SESSION_OPENID_KEY);
            if (openIdInSession != null) {
                openId = (String)openIdInSession;
            } else {
                WxMpOAuth2AccessToken wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(authCode);
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

		Student student = repository.findOneByOpenCode(openId);
		return student.getOrders().stream().map(
				x -> new OrderContainer(x, x.getProductsMap(), x.getDerivedProductsMap(), x.getImageCollectionMap()))
				.collect(Collectors.toCollection(ArrayList::new));
	}
}
