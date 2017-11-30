package com.edu.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.edu.dao.ClassProductRepository;
import com.edu.dao.CustomerRepository;
import com.edu.dao.DerivedProductRepository;
import com.edu.dao.ImageCollectionRepository;
import com.edu.dao.ProductRepository;
import com.edu.domain.*;
import com.edu.domain.dto.ProductContainer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CartCenterController {
	@Autowired
	private CustomerRepository custRepo;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private DerivedProductRepository derivedProductRepository;
	
	@Autowired
	private ImageCollectionRepository imageCollectionRepository;
	
	@Autowired
	private ClassProductRepository classProductRepository;

	public final static String CART_PATH = "/user/cart";

	public final static String SESSION_OPENID_KEY = "openCode";

	@GetMapping(CART_PATH)
	private String doShowCart(HttpSession session, Model model) {
		String openId = (String) session.getAttribute(SESSION_OPENID_KEY);

		Customer customer = custRepo.findOneByOpenCode(openId);

		Set<Product> products = customer.getCart().getProducts();
		Set<DerivedProduct> derivedProducts = customer.getCart().getDerivedProducts();
		Set<ImageCollection> imageCollection = customer.getCart().getImageCollection();
		Set<ClassProduct> classProducts = customer.getCart().getClassProducts();

		Stream<ProductContainer> productsStream = products.stream().map(x -> new ProductContainer(x, 1, 1));
		Stream<ProductContainer> derivedProductsStream = derivedProducts.stream()
				.map(x -> new ProductContainer(x, 1, 2));
		Stream<ProductContainer> imageCollectionStream = imageCollection.stream()
				.map(x -> new ProductContainer(x, 1, 3));
		Stream<ProductContainer> classProductsStream = classProducts.stream().map(x -> new ProductContainer(x, 1, 4));

		model.addAttribute("products",
				Stream.of(productsStream, derivedProductsStream, imageCollectionStream, classProductsStream)
						.flatMap(i -> i).collect(Collectors.toCollection(ArrayList::new)));
		model.addAttribute("code", openId);
		return "user_cart";
	}
	
	@PostMapping(CART_PATH)
	@ResponseBody
	private String deleteProduct(@RequestParam(value = "id") String id,@RequestParam(value = "type") String type,HttpSession session) {
		Object openCodeObject = session.getAttribute("openCode");
		String openId = openCodeObject.toString();
		Customer customer = custRepo.findOneByOpenCode(openId);
		ProductCart cart = customer.getCart();
		switch (type) {
		case "1":
			cart.removeProduct(productRepository.findOne(Long.parseLong(id)));
			break;
		case "2":
			cart.removeDerivedProduct(derivedProductRepository.findOne(Long.parseLong(id)));
			break;
		case "3":
			cart.removeImageCollection(imageCollectionRepository.findOne(Long.parseLong(id)));
			break;
		case "4":
			cart.removeClassProduct(classProductRepository.findOne(Long.parseLong(id)));
			break;
		}
		custRepo.save(customer);
		return "删除成功";
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
		Set<ClassProduct> classProducts = customer.getCart().getClassProducts();

		Stream<ProductContainer> productsStream = products.stream().map(x -> new ProductContainer(x, 1, 1));
		Stream<ProductContainer> derivedProductsStream = derivedProducts.stream()
				.map(x -> new ProductContainer(x, 1, 2));
		Stream<ProductContainer> imageCollectionStream = imageCollection.stream()
				.map(x -> new ProductContainer(x, 1, 3));
		Stream<ProductContainer> classProductsStream = classProducts.stream().map(x -> new ProductContainer(x, 1, 4));

		return Stream.of(productsStream, derivedProductsStream, imageCollectionStream, classProductsStream)
				.flatMap(i -> i).collect(Collectors.toCollection(ArrayList::new));
	}
}
