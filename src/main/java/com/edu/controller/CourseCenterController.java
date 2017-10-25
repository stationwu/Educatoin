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
import com.edu.domain.Student;

import me.chanjar.weixin.mp.api.WxMpService;

@Controller
public class CourseCenterController {
	@Autowired
	private WxMpService wxMpService;

	@Autowired
	private StudentRepository repository;

	@GetMapping("/user/course")
	public String authorize(@RequestParam(value = "code") String authCode, Model model) {
		Student student = repository.findOneByOpenCode(authCode);
		if (student == null) {
			Student newStudent = new Student();
			newStudent.setOpenCode(authCode);
			model.addAttribute("student", newStudent);
			return "user_signup";
		} else {
			Set<Course> signedCourses = student.getCoursesSet();
			Set<Course> notSignedCourses = student.getCourseNotSignSet();
			Set<Course> reservedCourses = student.getReservedCoursesSet();
			ArrayList<Course> signedCourseList = new ArrayList<>();
			model.addAttribute("signedCourses",
					signedCourses.stream().sorted((x, y) -> x.getCourseName().compareTo(y.getCourseName()))
							.collect(Collectors.toCollection(ArrayList::new)));
			model.addAttribute("notSignedCourses", notSignedCourses);
			model.addAttribute("reservedCourses", reservedCourses);
			return "user_courses";
		}
	}
}
