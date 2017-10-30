package com.edu.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.edu.dao.DerivedProductRepository;
import com.edu.dao.ImageRepository;
import com.edu.dao.ProductRepository;
import com.edu.dao.StudentRepository;
import com.edu.domain.Course;
import com.edu.domain.DerivedProduct;
import com.edu.domain.Image;
import com.edu.domain.ImageCollection;
import com.edu.domain.ImageContainer;
import com.edu.domain.Product;
import com.edu.domain.ProductCart;
import com.edu.domain.ProductContainer;
import com.edu.domain.ProductType;
import com.edu.domain.Student;

import me.chanjar.weixin.mp.api.WxMpService;

@Controller
public class DerivedProductCenterController {
	@Autowired
	private WxMpService wxMpService;

	@Autowired
	private StudentRepository repository;

	@Autowired
	private ImageRepository imageRepository;

	@Autowired
	private DerivedProductRepository derivedProductRepository;

	@Autowired
	private ProductRepository productRepository;
		
	@GetMapping("/user/relatedimage")
	public String getImages(HttpServletRequest request, Model model) {
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
			Set<Image> images = student.getImagesSet();
			ArrayList<ImageContainer> imagesContainer = (ArrayList<ImageContainer>) images.stream()
					.sorted((x,y) -> y.getDate().compareTo(x.getDate()))
					.map(x -> new ImageContainer(x.getId(), x.getImageName(), x.getDate(), x.getCourse(), "/Images/"+x.getId(), "/Images/"+x.getId()+"/thumbnail"))
					.collect(Collectors.toCollection(ArrayList::new));
			model.addAttribute("code", authCode);
			model.addAttribute("images", imagesContainer);
			return "user_imagelist";
		}
	}

	@GetMapping("/user/derivedproduct")
	public String getDerivedProduct(HttpServletRequest request,
			@RequestParam(value = "imgcontainer") String imageId, Model model) {
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
			Image image = imageRepository.findOne(Long.parseLong(imageId));
			model.addAttribute("image", new ImageContainer(image.getId(), image.getImageName(), image.getDate(), image.getCourse(), "/Images/" + image.getId(), "/Images/" + image.getId() + "/thumbnail"));
			ArrayList<ProductContainer> products = productRepository.getDerivedProductList().stream()
					.map(x -> new ProductContainer(x.getProductName(), x.getProductCategory().getCategoryName(),
							x.getProductPrice(), x.getProductDescription(),
							"/Images/" + x.getProductImages().stream().findFirst().get().getId(), 1,
							x.getId(), 2))
					.collect(Collectors.toCollection(ArrayList::new));
			model.addAttribute("products", products);
			return "user_derivedproduct";
		}
	}
	
	@PostMapping("/user/createderivedproduct")
	@ResponseBody
	public String createDerivedProduct(HttpServletRequest request,
			@RequestParam(value = "productid") String productid, @RequestParam(value = "imageid") String imageid, Model model) {
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
			DerivedProduct derivedProduct = new DerivedProduct();
			derivedProduct.setProduct(productRepository.findOne(Long.parseLong(productid)));
			derivedProduct.setImage(imageRepository.findOne(Long.parseLong(imageid)));
			student.getCart().addDerivedProducts(derivedProductRepository.save(derivedProduct));
			repository.save(student);
			return "请至购物车查看";
		}
	}
}
