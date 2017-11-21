package com.edu.controller;

import com.edu.dao.ProductRepository;
import com.edu.domain.Image;
import com.edu.domain.Product;
import com.edu.domain.dto.ProductContainer;
import com.edu.utils.ImageService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ProductCenterController {
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ImageService imageService;
	
	public final static String PRODUCT_EDIT_PATH = "/user/product/edit";

	public final static String CLASS_PRODUCT_PATH = "/user/classprod";

	public final static String DERIVED_PRODUCT_PATH = "/user/derivedprod";
	
	public final static String ADD_DERIVED_PRODUCT_PATH = "/user/derivedprod/add";

	public final static String IMAGE_PRODUCT_PATH = "/user/imageprod";

	public final static String SESSION_OPENID_KEY = "openCode";

	@GetMapping(CLASS_PRODUCT_PATH)
	public String getClassProduct(Model model) {
		model.addAttribute("productList", productRepository.getClassProductList().stream()
				.map(x -> new ProductContainer(x, 0, 1)).collect(Collectors.toCollection(ArrayList::new)));
		return "user_productlist";
	}

	@GetMapping(PRODUCT_EDIT_PATH)
	public String getClassProduct(@RequestParam(value = "productid") String productId, Model model) {
		Product product = productRepository.findOne(Long.parseLong(productId));
		model.addAttribute("product", product);
		return "user_editproduct";
	}

	@PostMapping(PRODUCT_EDIT_PATH)
	@ResponseBody
	public Product editClassProduct(@RequestParam(value = "productid") String productId, @RequestParam(value = "name") String name,
			@RequestParam(value = "description") String description, @RequestParam(value = "longDescription") String longDescription,
			@RequestParam(value = "priority") String priority, @RequestParam(value = "price") String price, 
			@RequestParam(value = "file") MultipartFile files[]) {
		Product product = productRepository.findOne(Long.parseLong(productId));
		Set<Image> images = new HashSet<>();
		for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                Image img = imageService.save(product.getProductName(), file);
                images.add(img);
            }
        }
		if(files.length != 0){
			product.setProductImages(images);
		}
		product.setProductName(name);
		product.setProductPrice(Double.parseDouble(price));
		product.setProductDescription(description);
		product.setLongProductDescription(longDescription);
		product.setPriority(Integer.parseInt(priority));
		return productRepository.save(product);
	}

	@GetMapping(DERIVED_PRODUCT_PATH)
	public String getDerivedProduct(Model model) {
		model.addAttribute("productList", productRepository.getDerivedProductList().stream()
				.map(x -> new ProductContainer(x, 0, 2)).collect(Collectors.toCollection(ArrayList::new)));
		return "user_productlist";
	}
	
	@GetMapping(IMAGE_PRODUCT_PATH)
	public String getImageProduct(Model model) {
		model.addAttribute("productList", productRepository.getImageCollectionProductList().stream()
				.map(x -> new ProductContainer(x, 0, 3)).collect(Collectors.toCollection(ArrayList::new)));
		return "user_productlist";
	}
	
	@GetMapping(ADD_DERIVED_PRODUCT_PATH)
	public String addDerivedProduct(Model model) {
		return "user_addderivedproduct";
	}
	
	@PostMapping(ADD_DERIVED_PRODUCT_PATH)
	@ResponseBody
	public Product createDerivedProduct( @RequestParam(value = "name") String name,
			@RequestParam(value = "description") String description, @RequestParam(value = "longDescription") String longDescription,
			@RequestParam(value = "priority") String priority, @RequestParam(value = "price") String price, 
			@RequestParam(value = "file") MultipartFile files[]) {
		Product product = new Product();
		Set<Image> images = new HashSet<>();
		for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                Image img = imageService.save(product.getProductName(), file);
                images.add(img);
            }
        }
		if(files.length != 0){
			product.setProductImages(images);
		}
		product.setProductName(name);
		product.setProductPrice(Double.parseDouble(price));
		product.setProductDescription(description);
		product.setLongProductDescription(longDescription);
		product.setPriority(Integer.parseInt(priority));
		return productRepository.save(product);
	}
	
}
