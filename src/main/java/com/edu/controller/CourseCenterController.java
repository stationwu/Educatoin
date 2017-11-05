package com.edu.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.edu.dao.CustomerRepository;
import com.edu.domain.Customer;
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

	@Autowired
    private CustomerRepository custRepo;

	public final static String USER_COURSE_PATH = "/user/course";
	public final static String USER_COURSE_CALLBACK_PATH = "/user/course/cb";

	public final static String SIGN_COURSE_PATH = "/user/signcourse";
	public final static String SIGN_COURSE_CALLBACK_PATH = "/user/signcourse/cb";

    public final static String BOOK_COURSE_PATH = "/user/reservecourse";
    public final static String BOOK_COURSE_CALLBACK_PATH = "/user/reservecourse/cb";
    
    public final static String SESSION_OPENID_KEY = "openCode";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * TODO: now only the one of the student could book the courese
     */
    @GetMapping(USER_COURSE_PATH)
	public String doShowUserCourse(HttpSession session, Model model) {
    	String openId = (String)session.getAttribute(SESSION_OPENID_KEY);
    	
        if (openId == null) {
            return "error_500";
        }

        Customer customer = custRepo.findOneByOpenCode(openId);
//		if (customer == null) {
//		    Customer newCustomer = new Customer();
//		    newCustomer.setOpenCode(openId);
//		    model.addAttribute("customer", newCustomer);
//		    return "user_signup";
//        }
//
//        Set<Student> students = customer.getStudents();
//        List<Student> listOfStudents = new ArrayList<>(students);
//		model.addAttribute("students", listOfStudents);
		
		Student student = customer.getStudents().stream().collect(Collectors.toCollection(ArrayList::new)).get(0);
		model.addAttribute("student", student);
        /**
         * TODO: Below loop is wrong. We must show also the students list. So view also needs change.
         * Model was WeChat user = Student, Student has-many courses
         * It's now WeChat user = Customer, Customer has-many students, Student has-many courses
         * So it's necessary to display a list of students, then under each student list his courses
         */
//        for (Student student : customer.getStudents()) {
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
//        }

        return "user_courses";
	}

    @GetMapping(SIGN_COURSE_PATH)
	private String doSignCourse(HttpSession session, Model model) {
    	String openId = (String)session.getAttribute(SESSION_OPENID_KEY);
        if (openId == null) {
            return "error_500";
        }

        Customer customer = custRepo.findOneByOpenCode(openId);
		if (customer == null) {
			Customer newCustomer = new Customer();
            newCustomer.setOpenCode(openId);
			model.addAttribute("customer", newCustomer);
			return "user_signup";
		} else {
			model.addAttribute("code", openId);
			return "user_sign_course";
		}
	}
    
    /**
     * TODO: now only the one of the student could book the courese
     */
    //课程预约
    @GetMapping(BOOK_COURSE_PATH)
	private String doBookCourse(HttpSession session, Model model) {
    	String openId = (String)session.getAttribute(SESSION_OPENID_KEY);
        if (openId == null) {
            return "error_500";
        }

        Customer customer = custRepo.findOneByOpenCode(openId);
        model.addAttribute("student", customer.getStudents().stream().collect(Collectors.toCollection(ArrayList::new)).get(0));
        model.addAttribute("code", openId);
			
		return "user_reserve_course";
	}
}