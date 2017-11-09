package com.edu.controller;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import com.edu.dao.CustomerRepository;
import com.edu.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.edu.domain.Course;
import com.edu.domain.Student;

@Controller
public class CourseCenterController {
	@Autowired
    private CustomerRepository custRepo;

	public final static String USER_COURSE_PATH = "/user/course";
	
	public final static String ABOUT_COURSE_PATH = "/user/aboutcourse";

    public final static String BOOK_COURSE_PATH = "/user/reservecourse";
    
    public final static String SESSION_OPENID_KEY = "openCode";

    @GetMapping(ABOUT_COURSE_PATH)
    public String aboutCourse() {
    	return "about_course";
    }

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

    /**
     * TODO: now only the one of the student could book the courese
     */
    //课程预约
    @GetMapping(BOOK_COURSE_PATH)
	private String doBookCourse(HttpSession session, Model model) {
    	String openId = (String)session.getAttribute(SESSION_OPENID_KEY);

        Customer customer = custRepo.findOneByOpenCode(openId);
        model.addAttribute("students", customer.getStudents().stream().collect(Collectors.toCollection(ArrayList::new)));
        model.addAttribute("code", openId);
			
		return "user_reserve_course";
	}
}