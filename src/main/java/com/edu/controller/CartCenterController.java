package com.edu.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.edu.dao.StudentRepository;
import com.edu.domain.Course;
import com.edu.domain.DerivedProduct;
import com.edu.domain.ImageCollection;
import com.edu.domain.Product;
import com.edu.domain.ProductCart;
import com.edu.domain.ProductContainer;
import com.edu.domain.ProductType;
import com.edu.domain.Student;

import me.chanjar.weixin.mp.api.WxMpService;

@Controller
public class CartCenterController {
	@Autowired
	private WxMpService wxMpService;

	@Autowired
	private StudentRepository repository;

	@GetMapping("/user/cart")
	public String getCart(@RequestParam(value = "code") String authCode, Model model) {
		Student student = repository.findOneByOpenCode(authCode);
		if (student == null) {
			Student newStudent = new Student();
			newStudent.setOpenCode(authCode);
			model.addAttribute("student", newStudent);
			return "user_signup";
		} else {
			Set<Product> products = student.getCart().getProducts();
			Set<DerivedProduct> derivedProducts = student.getCart().getDerivedProducts();
			Set<ImageCollection> imageCollection = student.getCart().getImageCollection();

			Stream<ProductContainer> productsStream = products.stream()
					.map(x -> new ProductContainer(x.getProductName(), x.getProductCategory().getCategoryName(),
							x.getProductPrice(), x.getProductDescription(),
							"/Images/" + x.getProductImages().stream().findFirst().get().getId() + "/thumbnail", 1,
							x.getId(), 1));
			Stream<ProductContainer> derivedProductsStream = derivedProducts.stream()
					.map(x -> new ProductContainer(x.getProduct().getProductName(), "衍生品",
							x.getProduct().getProductPrice(), x.getProduct().getProductDescription(),
							"/Images/" + x.getImage().getId() + "/thumbnail", 1, x.getId(), 2));
			Stream<ProductContainer> imageCollectionStream = imageCollection.stream()
					.map(x -> new ProductContainer(x.getCollectionName(), "作品集", x.getPrice(),
							x.getCollectionDescription(),
							"/Images/" + x.getImageCollection().stream().findFirst().get().getId() + "/thumbnail", 1,
							x.getId(), 3));
			model.addAttribute("products", Stream.of(productsStream, derivedProductsStream, imageCollectionStream)
					.flatMap(i -> i).collect(Collectors.toCollection(ArrayList::new)));
			model.addAttribute("code", authCode);
			return "user_cart";
		}
	}

	@GetMapping("/user/cartcontent")
	@ResponseBody
	public List<ProductContainer> getCartContent(@RequestParam(value = "code") String authCode) {
		Student student = repository.findOneByOpenCode(authCode);
		Set<Product> products = student.getCart().getProducts();
		Set<DerivedProduct> derivedProducts = student.getCart().getDerivedProducts();
		Set<ImageCollection> imageCollection = student.getCart().getImageCollection();

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
