package com.edu.web.rest;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.edu.controller.RouteConstant;
import com.edu.dao.CourseRepository;
import com.edu.dao.StudentRepository;
import com.edu.domain.Course;
import com.edu.domain.Student;
import com.edu.utils.DateToStringConverter;

@RestController
public class CourseController {
	public static final String PATH = "/Course";
	public static final String RELATIVE_STUDENT_PATH = StudentController.PATH + "/{studentId}" + PATH;
	private final CourseRepository courseRepository;
	private final StudentRepository studentRepository;
	// private final Environment environment;

	@Autowired
	public CourseController(CourseRepository courseRepository,
			StudentRepository studentRepository/* , Environment environment */) {
		this.courseRepository = courseRepository;
		this.studentRepository = studentRepository;
		// this.environment = environment;
	}

	@RequestMapping(path = PATH + "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Resource<Course>> show(@PathVariable("id") Long id) {
		Course entity = courseRepository.findOne(id);
		return new ResponseEntity<>(buildResource(entity), HttpStatus.OK);
	}

	@RequestMapping(path = PATH
			+ "/date/{date}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Resources<Resource<Course>>> getCourseByDate(@PathVariable("date") Date date) {
		String dateStr = DateToStringConverter.convertDatetoString(date);
		Iterable<Course> entities = courseRepository.findByDateOrderByTimeFromAsc(dateStr);
		return new ResponseEntity<>(buildResources(entities), HttpStatus.OK);
	}

	@RequestMapping(path = RELATIVE_STUDENT_PATH, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Resources<Resource<Course>>> indexCourse(@PathVariable("studentId") String studentId) {
		Student student = studentRepository.findOne(studentId);
		Iterable<Course> entities = student.getCoursesSet();

		return new ResponseEntity<>(buildResources(entities), HttpStatus.OK);
	}

	private Resources<Resource<Course>> buildResources(Iterable<Course> entities) {
		List<Resource<Course>> resourceList = new ArrayList<>();

		// Links
		for (Course entity : entities) {
			resourceList.add(buildResource(entity));
		}

		Resources<Resource<Course>> resources = new Resources<>(resourceList);

		return resources;
	}

	private Resource<Course> buildResource(Course entity) {
		// String address = InetAddress.getLoopbackAddress().getHostName();
		// String port = environment.getProperty("server.port");
		String url = "";// "http://" + address + ":" + port;
		Resource<Course> resource = new Resource<>(entity);
		// Links
		resource.add(linkTo(methodOn(CourseController.class).show(entity.getId())).withSelfRel());
		if (entity.getStudentsSet() != null) {
			for (Student student : entity.getStudentsSet()) {
				resource.add(
						new Link(url + StudentController.PATH + "/" + student.getId(), RouteConstant.REL_TO_STUDENTS));
			}
		}
		return resource;
	}
}
