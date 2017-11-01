package com.edu.controller;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.edu.utils.WxUserOAuthHelper;
import me.chanjar.weixin.common.exception.WxErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.edu.dao.StudentRepository;
import com.edu.domain.Course;
import com.edu.domain.Student;

import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CourseCenterController {
	@Autowired
	private WxMpService wxMpService;

	@Autowired
    private WxUserOAuthHelper oauthHelper;

	@Autowired
	private StudentRepository repository;

	public final static String USER_COURSE_PATH = "/user/course";
	public final static String USER_COURSE_CALLBACK_PATH = "/user/course/cb";

	public final static String SIGN_COURSE_PATH = "/user/signcourse";
	public final static String SIGN_COURSE_CALLBACK_PATH = "/user/signcourse/cb";

    public final static String BOOK_COURSE_PATH = "/user/reservecourse";
    public final static String BOOK_COURSE_CALLBACK_PATH = "/user/reservecourse/cb";
    
    public final static String SESSION_OPENID_KEY = "openCode";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping(USER_COURSE_PATH)
    public String userCourse(HttpServletRequest request, HttpSession session, Model model) {
        Object openIdInSession = session.getAttribute(SESSION_OPENID_KEY);

        if (openIdInSession == null) { // OAuth to get OpenID
            return oauthHelper.buildOAuth2RedirectURL(request, USER_COURSE_PATH, USER_COURSE_CALLBACK_PATH);
        } else {
            return doShowUserCourse((String) openIdInSession, model);
        }
    }

    @GetMapping(USER_COURSE_CALLBACK_PATH)
    public String userCourseCallback(@RequestParam(value="code") String authCode, Model model, HttpSession session) {
        String openId = null;

        try {
            openId = oauthHelper.getOpenIdWhenOAuth2CalledBack(authCode, session);
        } catch (WxErrorException e) {
            e.printStackTrace();
            return "error_500";
        }

        return doShowUserCourse(openId, model);
    }

	public String doShowUserCourse(String openId, Model model) {
        if (openId == null) {
            return "error_500";
        }

		Student student = repository.findOneByOpenCode(openId);

		if (student == null) {
			Student newStudent = new Student();
			newStudent.setOpenCode(openId);
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

	@GetMapping(SIGN_COURSE_PATH)
    public String signCourse(HttpServletRequest request, HttpSession session, Model model) {
        Object openIdInSession = session.getAttribute(SESSION_OPENID_KEY);

        if (openIdInSession == null) { // OAuth to get OpenID
            return oauthHelper.buildOAuth2RedirectURL(request, SIGN_COURSE_PATH, SIGN_COURSE_CALLBACK_PATH);
        } else {
            return doSignCourse((String) openIdInSession, model);
        }
    }

    @GetMapping(SIGN_COURSE_CALLBACK_PATH)
    public String signCourseCallback(@RequestParam(value="code") String authCode, Model model, HttpSession session) {
        String openId = null;

        try {
            openId = oauthHelper.getOpenIdWhenOAuth2CalledBack(authCode, session);
        } catch (WxErrorException e) {
            e.printStackTrace();
            return "error_500";
        }

        return doSignCourse(openId, model);
    }

	private String doSignCourse(String openId, Model model) {
        if (openId == null) {
            return "error_500";
        }
		
		Student student = repository.findOneByOpenCode(openId);
		if (student == null) {
			Student newStudent = new Student();
			newStudent.setOpenCode(openId);
			model.addAttribute("student", newStudent);
			return "user_signup";
		} else {
			model.addAttribute("code", openId);
			return "user_sign_course";
		}
	}

	@GetMapping(BOOK_COURSE_PATH)
    public String bookCourse(HttpServletRequest request, HttpSession session, Model model) {
        Object openIdInSession = session.getAttribute(SESSION_OPENID_KEY);

        if (openIdInSession == null) { // OAuth to get OpenID
            return oauthHelper.buildOAuth2RedirectURL(request, BOOK_COURSE_PATH, BOOK_COURSE_CALLBACK_PATH);
        } else {
            return doBookCourse((String) openIdInSession, model);
        }
    }

    @GetMapping(BOOK_COURSE_CALLBACK_PATH)
    public String bookCourseCallback(@RequestParam(value="code") String authCode, Model model, HttpSession session) {
        String openId = null;

        try {
            openId = oauthHelper.getOpenIdWhenOAuth2CalledBack(authCode, session);
        } catch (WxErrorException e) {
            e.printStackTrace();
            return "error_500";
        }

        return doBookCourse(openId, model);
    }

	private String doBookCourse(String openId, Model model) {
        if (openId == null) {
            return "error_500";
        }
		
		Student student = repository.findOneByOpenCode(openId);
		if (student == null) {
			Student newStudent = new Student();
			newStudent.setOpenCode(openId);
			model.addAttribute("student", newStudent);
			return "user_signup";
		} else {
			model.addAttribute("code", openId);
			return "user_reserve_course";
		}
	}
}