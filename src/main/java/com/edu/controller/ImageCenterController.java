package com.edu.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.edu.dao.StudentRepository;
import com.edu.domain.Course;
import com.edu.domain.Image;
import com.edu.domain.ImageContainer;
import com.edu.domain.Student;

import me.chanjar.weixin.mp.api.WxMpService;

@Controller
public class ImageCenterController {
	@Autowired
	private WxMpService wxMpService;

	@Autowired
	private StudentRepository repository;

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
	public String createImageCollection(@RequestParam(value = "code") String authCode, Model model) {
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
			return "user_imagecollection";
		}
	}
}
