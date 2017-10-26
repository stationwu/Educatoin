package com.edu.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.apache.http.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.edu.dao.CourseRepository;
import com.edu.dao.StudentRepository;
import com.edu.domain.Course;
import com.edu.domain.Image;
import com.edu.domain.Student;

@RestController
public class StudentController {
	public static final String PATH = "/Student";
	public static final String SIGN_IN_PATH = "/Sign";
	public static final String RESERVE_PATH = "/Reserve";
	public static final String RELATIVE_COURSE_PATH = CourseController.PATH + "/{courseId}" + PATH;
	private final StudentRepository studentRepository;
	private final CourseRepository courseRepository;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
//	private final Environment environment;

	@Autowired
	public StudentController(StudentRepository studentRepository, CourseRepository courseRepository/*,
			Environment environment*/) {
		this.studentRepository = studentRepository;
		this.courseRepository = courseRepository;
//		this.environment = environment;
	}

	@RequestMapping(path = PATH + "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Resource<Student>> show(@PathVariable("id") Long id) {
		Student entity = studentRepository.findOne(id);
		return new ResponseEntity<>(buildResource(entity), HttpStatus.OK);
	}

	@RequestMapping(path = RELATIVE_COURSE_PATH, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Resources<Resource<Student>>> showStudentbyCourse(@PathVariable("courseId") @Min(0) Long courseId) {
		Course course = courseRepository.findOne(courseId);
		Iterable<Student> entities = course.getStudentsSet();
		return new ResponseEntity<>(buildResources(entities), HttpStatus.OK);
	}

	@GetMapping(path = PATH + "/OpenCode/{openCode}")
	public Resource<Student> showStudentbyOpenCode(@PathVariable("openCode") String openCode){
		Student entity = studentRepository.findOneByOpenCode(openCode);
		return buildResource(entity);
	}

	@PostMapping(path = PATH + "/{openCode}" + SIGN_IN_PATH+"/{courseId}")
	public Resource<Student> studentSign(@PathVariable("openCode") String openCode, @PathVariable("courseId") Long courseId) throws Exception {
		Student student = studentRepository.findOneByOpenCode(openCode);
		Set<Course> courses = student.getCoursesSet();
		if(student.getClassPeriod() <= courses.size())
			throw new Exception("ClassPeriod exceed size of course:" + student.getId());
		Course course = courseRepository.findOne(courseId);
		student.addCourse(course);
		Student entity = studentRepository.save(student);
		return buildResource(entity);
	}
	
	@PostMapping(path = PATH + "/{openCode}" + RESERVE_PATH+"/{courseId}")
	public Resource<Student> studentReserve(@PathVariable("openCode") String openCode, @PathVariable("courseId") Long courseId) throws Exception {
		Student student = studentRepository.findOneByOpenCode(openCode);
		Set<Course> courses = student.getReservedCoursesSet();
		Course course = courseRepository.findOne(courseId);
		student.addReservedCourse(course);
		Student entity = studentRepository.save(student);
		return buildResource(entity);
	}
	
	@PostMapping(path = PATH)
	public Resource<Student> createStudent(@RequestBody @Valid Student student) throws HttpException {
		student.setClassPeriod(0);
		Student entity = studentRepository.save(student);
		return buildResource(entity);
	}

	@PostMapping(path = PATH + "/{id}")
	public Resource<Student> editStudent(@PathVariable(value = "id") Long id, @RequestBody @Valid Student student)
			throws HttpException {
		Student entity = studentRepository.findOne(id);
		entity.setStudentName(student.getStudentName());
		entity.setMobilePhone(student.getMobilePhone());
		entity.setAge(student.getAge());
		entity.setAddress(student.getAddress());
		Student entityUser = studentRepository.save(entity);
		return buildResource(entityUser);
	}

	private Resources<Resource<Student>> buildResources(Iterable<Student> entities) {
		List<Resource<Student>> resourceList = new ArrayList<>();

		// Links
		for (Student entity : entities) {
			resourceList.add(buildResource(entity));
		}

		Resources<Resource<Student>> resources = new Resources<>(resourceList);

		return resources;
	}

	private Resource<Student> buildResource(Student entity) {
//		String address = InetAddress.getLoopbackAddress().getHostName();
//		String port = environment.getProperty("server.port");
		String url = "";//"http://" + address + ":" + port;
		Resource<Student> resource = new Resource<>(entity);
		// Links
		resource.add(linkTo(methodOn(StudentController.class).show(entity.getId())).withSelfRel());
		if (entity.getCoursesSet() != null) {
			for (Course course : entity.getCoursesSet()) {
				resource.add(new Link(url + CourseController.PATH + "/" + course.getId(), RouteConstant.REL_TO_COURSES));
			}
		}
		if (entity.getImagesSet() != null) {
			for (Image image : entity.getImagesSet()) {
				resource.add(new Link(url + ImageController.PATH + "/" + image.getId() + "/thumbnail",
						RouteConstant.REL_TO_IMAGES));
			}
		}
		return resource;
	}
}
