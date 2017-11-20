package com.edu.controller;

import com.edu.dao.ProductRepository;
import com.edu.domain.Product;
import com.edu.domain.dto.ProductContainer;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProductCenterController {
	@Autowired
	private ProductRepository productRepository;

	public final static String CLASS_PRODUCT_PATH = "/user/classprod";

	public final static String CLASS_PRODUCT_EDIT_PATH = "/user/classprod/edit";

	public final static String DERIVED_PRODUCT_PATH = "/user/derivedprod";

	public final static String DERIVED_PRODUCT_EDIT_PATH = "/user/derivedprod/edit";

	public final static String IMAGE_PRODUCT_PATH = "/user/imageprod";

	public final static String IMAGE_PRODUCT_EDIT_PATH = "/user/imageprod/edit";

	public final static String SESSION_OPENID_KEY = "openCode";

	@GetMapping(CLASS_PRODUCT_PATH)
	public String getClassProduct(Model model) {
		model.addAttribute("productList", productRepository.getClassProductList().stream()
				.map(x -> new ProductContainer(x, 0, 1)).collect(Collectors.toCollection(ArrayList::new)));
		return "user_productlist";
	}

	@GetMapping(CLASS_PRODUCT_EDIT_PATH)
	public String getClassProduct(@RequestParam(value = "productid") String productId, Model model) {
		Product product = productRepository.findOne(Long.parseLong(productId));
		model.addAttribute("product", product);
		return "user_editclassproduct";
	}
	
	@PostMapping(CLASS_PRODUCT_EDIT_PATH)
	public String updateClassProduct(@RequestParam(value = "productid") String productId, Model model) {
		Product product = productRepository.findOne(Long.parseLong(productId));
		model.addAttribute("product", product);
		return "user_editclassproduct";
	}

	@GetMapping(IMAGE_PRODUCT_PATH)
	public String getImageProduct(Model model) {
		model.addAttribute("productList", productRepository.getImageCollectionProductList().stream()
				.map(x -> new ProductContainer(x, 0, 3)).collect(Collectors.toCollection(ArrayList::new)));
		return "user_productlist";
	}

	@GetMapping(DERIVED_PRODUCT_PATH)
	public String getDerivedProduct(Model model) {
		model.addAttribute("productList", productRepository.getDerivedProductList().stream()
				.map(x -> new ProductContainer(x, 0, 2)).collect(Collectors.toCollection(ArrayList::new)));
		return "user_productlist";
	}
}
