package com.edu.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.edu.dao.CustomerRepository;
import com.edu.domain.*;
import com.edu.domain.dto.DerivedProduct;
import com.edu.domain.dto.ProductContainer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CartCenterController {
	@Autowired
	private CustomerRepository custRepo;


    public final static String CART_PATH = "/user/cart";
    public final static String CART_CALLBACK_PATH = "/user/cart/cb";

    public final static String SESSION_OPENID_KEY = "openCode";

    @GetMapping(CART_PATH)
	private String doShowCart(HttpSession session, Model model) {
    	String openId = (String)session.getAttribute(SESSION_OPENID_KEY);

		Customer customer = custRepo.findOneByOpenCode(openId);

		Set<Product> products = customer.getCart().getProducts();
		Set<DerivedProduct> derivedProducts = customer.getCart().getDerivedProducts();
		Set<ImageCollection> imageCollection = customer.getCart().getImageCollection();

		Stream<ProductContainer> productsStream = products.stream()
				.map(x -> new ProductContainer(x, 1,1));
		Stream<ProductContainer> derivedProductsStream = derivedProducts.stream()
				.map(x -> new ProductContainer(x,1, 2));
		Stream<ProductContainer> imageCollectionStream = imageCollection.stream()
				.map(x -> new ProductContainer(x,1, 3));
		model.addAttribute("products", Stream.of(productsStream, derivedProductsStream, imageCollectionStream)
				.flatMap(i -> i).collect(Collectors.toCollection(ArrayList::new)));
		model.addAttribute("code", openId);
		return "user_cart";
	}

	@GetMapping("/user/cartcontent")
	@ResponseBody
	public List<ProductContainer> getCartContent(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Object openCodeObject = session.getAttribute("openCode");

		if (null == openCodeObject) {
			return new ArrayList<>();
		}

		String openId = openCodeObject.toString();
		Customer customer = custRepo.findOneByOpenCode(openId);
		Set<Product> products = customer.getCart().getProducts();
		Set<DerivedProduct> derivedProducts = customer.getCart().getDerivedProducts();
		Set<ImageCollection> imageCollection = customer.getCart().getImageCollection();

		Stream<ProductContainer> productsStream = products.stream()
				.map(x -> new ProductContainer(x,1, 1));
		Stream<ProductContainer> derivedProductsStream = derivedProducts.stream()
				.map(x -> new ProductContainer(x,1, 2));
		Stream<ProductContainer> imageCollectionStream = imageCollection.stream()
				.map(x -> new ProductContainer(x,1, 3));

		return Stream.of(productsStream, derivedProductsStream, imageCollectionStream).flatMap(i -> i)
				.collect(Collectors.toCollection(ArrayList::new));
	}
}
