package com.edu.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.edu.dao.ImageCollectionRepository;
import com.edu.dao.ImageRepository;
import com.edu.dao.StudentRepository;
import com.edu.domain.Course;
import com.edu.domain.Image;
import com.edu.domain.ImageCollection;
import com.edu.domain.ImageContainer;
import com.edu.domain.Student;
import com.mysql.fabric.xmlrpc.base.Array;

import me.chanjar.weixin.mp.api.WxMpService;

@Controller
public class ImageCenterController {
	@Autowired
	private WxMpService wxMpService;

	@Autowired
	private StudentRepository repository;
	
	@Autowired
	private ImageRepository imageRepository;
	
	@Autowired
	private ImageCollectionRepository imageCollectionRepository;

	@GetMapping("/user/image")
	public String getImages(@RequestParam(value = "code") String authCode, Model model) {
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

			model.addAttribute("images", imagesContainer);
			return "user_images";
		}
	}
	
	@GetMapping("/user/imagecollection")
	public String getImageCollection(@RequestParam(value = "code") String authCode, Model model) {
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
					.map(x -> new ImageContainer(x.getId() ,x.getImageName(), x.getDate(), x.getCourse(), "/Images/"+x.getId(), "/Images/"+x.getId()+"/thumbnail"))
					.collect(Collectors.toCollection(ArrayList::new));

			model.addAttribute("images", imagesContainer);
			model.addAttribute("code", authCode);
			return "user_imagecollection";
		}
	}
	
	@PostMapping("/user/generateImagecollection")
	@ResponseBody
	public String createImageCollection(@RequestParam(value = "code") String authCode, @RequestParam(value = "images") String images, Model model) {
		Student student = repository.findOneByOpenCode(authCode);
		ImageCollection imageCollection = new ImageCollection();
		List<String> imagesWithId = Arrays.asList(images.split(","));
		Set<Image> imageList = new HashSet<>();
		for(String id : imagesWithId){
			imageList.add(imageRepository.findOne(Long.parseLong(id)));
		}
		
		imageCollection.setImageCollection(imageList);
		imageCollection.setPrice(200d);
		imageCollection.setCollectionName("作品集");
		imageCollection.setCollectionDescription(imageList.size()+"幅作品");
		ImageCollection entity = imageCollectionRepository.save(imageCollection);
		student.getCart().addImageCollection(entity);
		repository.save(student);
		return "请到购物车查看生成的作品集";
	}
}
