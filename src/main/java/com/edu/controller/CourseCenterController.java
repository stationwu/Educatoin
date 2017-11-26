package com.edu.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpSession;

import com.edu.dao.ClassProductRepository;
import com.edu.dao.CourseRepository;
import com.edu.dao.CustomerRepository;
import com.edu.dao.ProductRepository;
import com.edu.dao.StudentRepository;
import com.edu.domain.Customer;
import com.edu.domain.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.edu.domain.ClassProduct;
import com.edu.domain.Course;
import com.edu.domain.Student;
import com.edu.domain.dto.CourseContainer;
import com.edu.utils.DateToStringConverter;

@Controller
public class CourseCenterController {
	@Autowired
	private CustomerRepository custRepo;

	@Autowired
	private CourseRepository courseRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ClassProductRepository classProductRepository;

	@Autowired
	private StudentRepository studentRepository;

	public final static String USER_COURSE_PATH = "/user/course";

	public final static String RELEATED_COURSE_PATH = "/user/relatedcourse";

	public final static String ABOUT_COURSE_PATH = "/user/aboutcourse";

	public final static String BOOK_COURSE_PATH = "/user/reservecourse";

	public final static String SEARCH_COURSE_PATH = "/user/searchcourse";

	public final static String RESERVE_COURSE_PATH = "/user/reserve";
	
	public final static String BUY_COURSE_PATH = "/user/buycourse";

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
		String openId = (String) session.getAttribute(SESSION_OPENID_KEY);

		Customer customer = custRepo.findOneByOpenCode(openId);
		model.addAttribute("students", customer.getStudents().stream().sorted((x, y) -> x.getId().compareTo(y.getId()))
				.collect(Collectors.toCollection(ArrayList::new)));

		return "user_courses";
	}

	@GetMapping(RELEATED_COURSE_PATH)
	@ResponseBody
	private ArrayList<CourseContainer> getRelatedCourse(@RequestParam(value = "studentid") String studentId, HttpSession session) {
		Student student = studentRepository.findOne(studentId);
		Set<Course> signedCourses = student.getCoursesSet();
		Set<Course> reservedCourses = student.getReservedCoursesSet();
		Stream<CourseContainer> signedStream = signedCourses.stream().map(x -> new CourseContainer(x,"已签到"));
		Stream<CourseContainer> reservedStream = reservedCourses.stream().map(x -> new CourseContainer(x,"已预约"));
		return Stream.of(signedStream, reservedStream).flatMap(i -> i)
				.sorted((x, y) -> (y.getDate() + y.getTimeFrom()).compareTo(x.getDate() + x.getTimeFrom()))
				.collect(Collectors.toCollection(ArrayList::new));
	}

	// 课程预约
	@GetMapping(BOOK_COURSE_PATH)
	private String doBookCourse(HttpSession session, Model model) {
		String openId = (String) session.getAttribute(SESSION_OPENID_KEY);

		Customer customer = custRepo.findOneByOpenCode(openId);
		model.addAttribute("students", customer.getStudents().stream().sorted((x, y) -> x.getId().compareTo(y.getId()))
				.collect(Collectors.toCollection(ArrayList::new)));
		model.addAttribute("code", openId);

		return "user_reserve_course";
	}

	@GetMapping(SEARCH_COURSE_PATH)
	@ResponseBody
	private ArrayList<CourseContainer> searchCourseByDate(@RequestParam(value = "date") Date date, HttpSession session,
			Model model) {
		String dateStr = DateToStringConverter.convertDatetoString(date);
		Collection<Course> entities = courseRepository.findByDateOrderByTimeFromAsc(dateStr);
		ArrayList<CourseContainer> courseContainers = entities.stream().map(x -> new CourseContainer(x,"预约课程"))
				.collect(Collectors.toCollection(ArrayList::new));
		return courseContainers;
	}

	@PostMapping(RESERVE_COURSE_PATH)
	@ResponseBody
	private String reserveCourse(@RequestParam(value = "studentid") String studentId,
			@RequestParam(value = "courseid") String courseId) {
		Student student = studentRepository.findOne(studentId);
		if(student.getLeftPeriods()<=0){
			return "剩余课程数为0，请购买课程";
		}
		Course course = courseRepository.findOne(Long.parseLong(courseId));
		student.addReservedCourse(course);
		studentRepository.save(student);
		return "预约成功";
	}
	
	@PostMapping(BUY_COURSE_PATH)
	@ResponseBody
	private String buyCourse(@RequestParam(value = "studentid") String studentId, HttpSession session) {
		String openId = (String) session.getAttribute(SESSION_OPENID_KEY);
		Customer customer = custRepo.findOneByOpenCode(openId);
		Student student = studentRepository.findOne(studentId);
		ClassProduct classProduct = new ClassProduct();
		Product product = productRepository.getClassProductList().get(0);
		classProduct.setProduct(product);
		classProduct.setStudent(student);
		classProduct.setDescription(student.getStudentName()+"的课程:"+product.getClassPeriod()+"课时");
		customer.getCart().addClassProduct(classProductRepository.save(classProduct));
		custRepo.save(customer);
		
		return "课程已加入购物车";
	}
}