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
        if (openId == null) {
            return "error_500";
        }

		Customer customer = custRepo.findOneByOpenCode(openId);
		if (customer == null) {
			Customer newCustomer = new Customer();
			newCustomer.setOpenCode(openId);
			model.addAttribute("customer", newCustomer);
			return "user_signup";
		} else {
			Set<Product> products = customer.getCart().getProducts();
			Set<DerivedProduct> derivedProducts = customer.getCart().getDerivedProducts();
			Set<ImageCollection> imageCollection = customer.getCart().getImageCollection();

			Stream<ProductContainer> productsStream = products.stream()
					.map(x -> new ProductContainer(x.getProductName(), x.getProductCategory().getCategoryName(),
							x.getProductPrice(), x.getProductDescription(),
							"/Images/" + x.getProductImages().stream().findFirst().get().getId() + "/thumbnail", 1,
							x.getId(), 1));
			Stream<ProductContainer> derivedProductsStream = derivedProducts.stream()
					.map(x -> new ProductContainer(x.getProduct().getProductName(), x.getProduct().getProductCategory().getCategoryName(),
							x.getProduct().getProductPrice(), x.getProduct().getProductDescription(),
							"/Images/" + x.getImage().getId() + "/thumbnail", 1, x.getId(), 2));
			Stream<ProductContainer> imageCollectionStream = imageCollection.stream()
					.map(x -> new ProductContainer(x.getCollectionName(), "作品集", x.getPrice(),
							x.getCollectionDescription(),
							"/Images/" + x.getImageCollection().stream().findFirst().get().getId() + "/thumbnail", 1,
							x.getId(), 3));
			model.addAttribute("products", Stream.of(productsStream, derivedProductsStream, imageCollectionStream)
					.flatMap(i -> i).collect(Collectors.toCollection(ArrayList::new)));
			model.addAttribute("code", openId);
			return "user_cart";
		}
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
				.map(x -> new ProductContainer(x.getProductName(), x.getProductCategory().getCategoryName(),
						x.getProductPrice(), x.getProductDescription(),
						"/Images/" + x.getProductImages().stream().findFirst().get().getId() + "/thumbnail", 1,
						x.getId(), 1));
		Stream<ProductContainer> derivedProductsStream = derivedProducts.stream()
				.map(x -> new ProductContainer(x.getProduct().getProductName(), "衍生品", x.getProduct().getProductPrice(),
						x.getProduct().getProductDescription(), "/Images/" + x.getImage().getId() + "/thumbnail", 1,
						x.getId(), 2));
		Stream<ProductContainer> imageCollectionStream = imageCollection.stream()
				.map(x -> new ProductContainer(x.getCollectionName(), "作品集", x.getPrice(), x.getCollectionDescription(),
						"/Images/" + x.getImageCollection().stream().findFirst().get().getId() + "/thumbnail", 1,
						x.getId(), 3));

		return Stream.of(productsStream, derivedProductsStream, imageCollectionStream).flatMap(i -> i)
				.collect(Collectors.toCollection(ArrayList::new));
	}
}
