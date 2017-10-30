package com.edu.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
	public String getCourses(HttpServletRequest request, Model model) {
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
			Set<Course> signedCourses = student.getCoursesSet();
			Set<Course> notSignedCourses = student.getCourseNotSignSet();
			Set<Course> reservedCourses = student.getReservedCoursesSet();
			model.addAttribute("signedCourses",
					signedCourses.stream().sorted((x, y) -> y.getCourseName().compareTo(x.getCourseName()))
							.collect(Collectors.toCollection(ArrayList::new)));
			model.addAttribute("notSignedCourses",
					notSignedCourses.stream().sorted((x, y) -> y.getCourseName().compareTo(x.getCourseName()))
							.collect(Collectors.toCollection(ArrayList::new)));
			model.addAttribute("reservedCourses",
					reservedCourses.stream().sorted((x, y) -> y.getCourseName().compareTo(x.getCourseName()))
							.collect(Collectors.toCollection(ArrayList::new)));
			return "user_courses";
		}
	}

	@GetMapping("/user/signcourse")
	public String navToSignCourse(HttpServletRequest request, Model model) {
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
			model.addAttribute("code", authCode);
			return "user_sign_course";
		}
	}

	@GetMapping("/user/reservecourse")
	public String navToReserveCourse(HttpServletRequest request,  Model model) {
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
			model.addAttribute("code", authCode);
			return "user_reserve_course";
		}
	}
}