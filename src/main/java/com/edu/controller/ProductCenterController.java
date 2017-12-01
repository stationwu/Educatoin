package com.edu.controller;

import com.edu.dao.ProductCategoryRepository;
import com.edu.dao.ProductRepository;
import com.edu.domain.Image;
import com.edu.domain.Product;
import com.edu.domain.ProductCategory;
import com.edu.domain.dto.ProductContainer;
import com.edu.utils.ImageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class ProductCenterController {
	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ImageService imageService;

	@Autowired
	private ProductCategoryRepository productCategoryRepository;

	public final static String CLASS_PRODUCT_PATH = "/manager/classprod";

	public final static String CLASS_PRODUCT_EDIT_PATH = "/manager/classprod/edit";

	public final static String DERIVED_PRODUCT_PATH = "/manager/derivedprod";

	public final static String DERIVED_PRODUCT_EDIT_PATH = "/manager/derivedprod/edit";

	public final static String ADD_DERIVED_PRODUCT_PATH = "/manager/derivedprod/add";

	public final static String IMAGE_PRODUCT_PATH = "/manager/imageprod";

	public final static String IMAGE_PRODUCT_EDIT_PATH = "/manager/imageprod/edit";

	public final static String SESSION_OPENID_KEY = "openCode";

	@GetMapping(CLASS_PRODUCT_PATH)
	public String getClassProductList(Model model) {
		model.addAttribute("productList", productRepository.getClassProductList().stream()
				.map(x -> new ProductContainer(x, 0, 1)).collect(Collectors.toCollection(ArrayList::new)));
		model.addAttribute("title", "课程商品列表");
		return "user_productlist";
	}

	@GetMapping(CLASS_PRODUCT_EDIT_PATH)
	public String getClassProduct(@RequestParam(value = "productid") String productId, Model model) {
		Product product = productRepository.findOne(Long.parseLong(productId));
		model.addAttribute("product", product);
		return "user_editclassproduct";
	}

	@PostMapping(CLASS_PRODUCT_EDIT_PATH)
	@ResponseBody
	public Product editClassProduct(@RequestParam(value = "productid") String productId,
			@RequestParam(value = "name") String name, @RequestParam(value = "description") String description,
			@RequestParam(value = "longDescription") String longDescription,
			@RequestParam(value = "classPeriod") String classPeriod, @RequestParam(value = "price") String price,
			@RequestParam(value = "file") MultipartFile files[]) {
		Product product = productRepository.findOne(Long.parseLong(productId));
		Set<Image> images = new HashSet<>();
		for (MultipartFile file : files) {
			if (!file.isEmpty()) {
				Image img = imageService.save(product.getProductName(), file);
				images.add(img);
			}
		}
		if (files.length != 0) {
			product.setProductImages(images);
		}
		product.setProductName(name);
		product.setProductPrice(Double.parseDouble(price));
		product.setProductDescription(description);
		product.setLongProductDescription(longDescription);
		product.setClassPeriod(Integer.parseInt(classPeriod));
		return productRepository.save(product);
	}

	@GetMapping(DERIVED_PRODUCT_PATH)
	public String getDerivedProductList(Model model) {
		model.addAttribute("productList", productRepository.getDerivedProductList().stream()
				.map(x -> new ProductContainer(x, 0, 2)).collect(Collectors.toCollection(ArrayList::new)));
		model.addAttribute("title", "衍生品商品列表");
		return "user_productlist";
	}

	@GetMapping(DERIVED_PRODUCT_EDIT_PATH)
	public String getDerivedProduct(@RequestParam(value = "productid") String productId, Model model) {
		Product product = productRepository.findOne(Long.parseLong(productId));
		model.addAttribute("product", product);
		return "user_editderivedproduct";
	}

	@PostMapping(DERIVED_PRODUCT_EDIT_PATH)
	@ResponseBody
	public Product editDerivedProduct(@RequestParam(value = "productid") String productId,
			@RequestParam(value = "name") String name, @RequestParam(value = "description") String description,
			@RequestParam(value = "longDescription") String longDescription,
			@RequestParam(value = "priority") String priority, @RequestParam(value = "price") String price,
			@RequestParam(value = "invalidFlag") Boolean invalidFlag,
			@RequestParam(value = "file") MultipartFile files[]) {
		Product product = productRepository.findOne(Long.parseLong(productId));
		Set<Image> images = new HashSet<>();
		for (MultipartFile file : files) {
			if (!file.isEmpty()) {
				Image img = imageService.save(product.getProductName(), file);
				images.add(img);
			}
		}
		if (files.length != 0) {
			product.setProductImages(images);
		}
		product.setProductName(name);
		product.setProductPrice(Double.parseDouble(price));
		product.setProductDescription(description);
		product.setLongProductDescription(longDescription);
		product.setPriority(Integer.parseInt(priority));
		product.setInvalidFlag(invalidFlag);
		return productRepository.save(product);
	}

	@GetMapping(IMAGE_PRODUCT_PATH)
	public String getImageProductList(Model model) {
		model.addAttribute("productList", productRepository.getImageCollectionProductList().stream()
				.map(x -> new ProductContainer(x, 0, 3)).collect(Collectors.toCollection(ArrayList::new)));
		model.addAttribute("title", "作品集商品列表");
		return "user_productlist";
	}

	@GetMapping(IMAGE_PRODUCT_EDIT_PATH)
	public String getImageProduct(@RequestParam(value = "productid") String productId, Model model) {
		Product product = productRepository.findOne(Long.parseLong(productId));
		model.addAttribute("product", product);
		return "user_editimageproduct";
	}

	@PostMapping(IMAGE_PRODUCT_EDIT_PATH)
	@ResponseBody
	public Product editImageProduct(@RequestParam(value = "productid") String productId,
			@RequestParam(value = "name") String name, @RequestParam(value = "description") String description,
			@RequestParam(value = "longDescription") String longDescription,
			@RequestParam(value = "price") String price, @RequestParam(value = "numberOfPic") String numberOfPic,
			@RequestParam(value = "file") MultipartFile files[]) {
		Product product = productRepository.findOne(Long.parseLong(productId));
		Set<Image> images = new HashSet<>();
		for (MultipartFile file : files) {
			if (!file.isEmpty()) {
				Image img = imageService.save(product.getProductName(), file);
				images.add(img);
			}
		}
		if (files.length != 0) {
			product.setProductImages(images);
		}
		product.setProductName(name);
		product.setProductPrice(Double.parseDouble(price));
		product.setProductDescription(description);
		product.setLongProductDescription(longDescription);
		product.setNumberOfPic(Integer.parseInt(numberOfPic));
		return productRepository.save(product);
	}

	@GetMapping(ADD_DERIVED_PRODUCT_PATH)
	public String addDerivedProduct(Model model) {
		return "user_addderivedproduct";
	}

	@PostMapping(ADD_DERIVED_PRODUCT_PATH)
	@ResponseBody
	public Product createDerivedProduct(@RequestParam(value = "name") String name,
			@RequestParam(value = "description") String description,
			@RequestParam(value = "longDescription") String longDescription,
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
		if (files.length != 0) {
			product.setProductImages(images);
		}
		product.setProductName(name);
		product.setProductPrice(Double.parseDouble(price));
		product.setProductDescription(description);
		product.setLongProductDescription(longDescription);
		product.setPriority(Integer.parseInt(priority));
		product.setDerivedProductFlag(true);
		List<ProductCategory> target = new ArrayList<>();
		productCategoryRepository.findAll().forEach(target::add);
		product.setProductCategory(target.stream().filter(x -> x.getCategoryName().equals("衍生品")).findFirst().get());
		return productRepository.save(product);
	}

}
