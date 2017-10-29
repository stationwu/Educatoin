package com.edu.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.edu.dao.DerivedProductRepository;
import com.edu.dao.ImageCollectionRepository;
import com.edu.dao.OrderRepository;
import com.edu.dao.ProductRepository;
import com.edu.dao.StudentRepository;
import com.edu.domain.Course;
import com.edu.domain.DerivedProduct;
import com.edu.domain.ImageCollection;
import com.edu.domain.Order;
import com.edu.domain.Product;
import com.edu.domain.ProductCart;
import com.edu.domain.ProductContainer;
import com.edu.domain.ProductType;
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

	@PostMapping("/user/order")
	@ResponseBody
	public String getCart(@RequestParam(value = "code") String authCode, @RequestBody List<ProductContainer> products, Model model) {
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
			for(ProductContainer productContainer : products){
				switch (productContainer.getType()) {
					case 1:
						productMap.put(productRepository.findOne(productContainer.getId()), productContainer.getQuantity());
						amount+=productContainer.getProductPrice()*productContainer.getQuantity();
						break;
					case 2:
						derivedProductMap.put(derivedProductRepository.findOne(productContainer.getId()), productContainer.getQuantity());
						amount+=productContainer.getProductPrice()*productContainer.getQuantity();
						break;
					case 3:
						imageCollectionMap.put(imageCollectionRepository.findOne(productContainer.getId()), productContainer.getQuantity());
						amount+=productContainer.getProductPrice()*productContainer.getQuantity();
						break;
				}
			}
			order.setProductsMap(productMap);
			order.setDerivedProductsMap(derivedProductMap);
			order.setImageCollectionMap(imageCollectionMap);
			order.setTotalAmount(amount);
			LocalDate localDate = LocalDate.now();
			order.setDate(localDate.toString());
			student.addOrder(orderRepository.save(order));
			repository.save(student);
			return "下单成功";
		}
	}
}
