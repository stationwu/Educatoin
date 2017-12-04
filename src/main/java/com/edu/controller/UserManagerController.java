package com.edu.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.edu.dao.StudentRepository;
import com.edu.domain.Student;
import com.edu.domain.dto.StudentContainer;
import com.edu.utils.Constant;

@Controller
public class UserManagerController {
	@Autowired
	private StudentRepository studentRepository;
	
	public final static String MANAGER_SEARCH_PATH = "/manager/search";
	
	public final static String MANAGER_SIGN_COURSE_PATH = "/manager/signcourse";
	
	@PostMapping(MANAGER_SEARCH_PATH)
	@ResponseBody
	public List<Student> searchStudent(@RequestParam(value="keyword") String keyword, HttpSession session) {
		List<Student> students = studentRepository.search(keyword);
		return students;
	}
	
	@GetMapping(MANAGER_SEARCH_PATH)
	public String getSearchPage(HttpSession session,Model model) {
		//String openId = (String) session.getAttribute(Constant.SESSION_OPENID_KEY);
		model.addAttribute("type", "1");
		model.addAttribute("title", "用户签到并上传");
		return "user_search";
	}
	
	@GetMapping("user/session")
	@ResponseBody
	public String createSession(HttpSession session) {
		String openId = "123456";
        session.setAttribute(Constant.SESSION_OPENID_KEY, openId);
		
		return "true";
	}
	
    @GetMapping(MANAGER_SIGN_COURSE_PATH)
	private String doSignCourse(@RequestParam(value="id") String id, HttpSession session, Model model) {
		Student student = studentRepository.findOne(id);
    	model.addAttribute("studentContainer", new StudentContainer(student));
    	
    	return "user_signcourse_upload";
	}
}
