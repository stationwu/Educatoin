package com.edu.web.rest;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.edu.controller.RouteConstant;
import com.edu.dao.CourseRepository;
import com.edu.domain.Course;
import com.edu.domain.Student;

@RestController
public class CourseManagerController {
	public static final String PATH = "/CourseManager";
	public static final String RELATIVE_STUDENT_PATH = StudentController.PATH + "/{studentId}" + PATH;
	private final CourseRepository courseRepository;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	// private final Environment environment;

	@Autowired
	public CourseManagerController(
			CourseRepository courseRepository/* , Environment environment */) {
		this.courseRepository = courseRepository;
		// this.environment = environment;
	}

	@RequestMapping(path = PATH + "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Resource<Course>> show(@PathVariable("id") Long id) {
		Course entity = courseRepository.findOne(id);
		return new ResponseEntity<>(buildResource(entity), HttpStatus.OK);
	}

	@RequestMapping(path = PATH, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<Resources<Resource<Course>>> index(
			@PageableDefault(value = 15, sort = { "date" }, direction = Sort.Direction.DESC) Pageable pageRequest) {
		Page<Course> entityPage = courseRepository.findAll(pageRequest);
		List<Course> entities = entityPage.getContent();
		return new ResponseEntity<>(buildResources(entities), HttpStatus.OK);
	}

	@PostMapping(path = PATH)
	public Resource<Course> createCourse(@RequestBody @Valid Course course) {
		Course courseEntity = courseRepository.save(course);
		return buildResource(courseEntity);
	}

	@PostMapping(path = PATH + "/{id}")
	public Resource<Course> manageCourse(@PathVariable("id") Long id, @RequestBody @Valid Course course) {
		Course entity = courseRepository.findOne(id);
		if (entity == null) {
			logger.error("Couse id:" + course.getId() + "missing");
		}
		entity.setCourseName(course.getCourseName());
		entity.setDate(course.getDate());
		entity.setTimeFrom(course.getTimeFrom());
		entity.setTimeTo(course.getTimeTo());
		Course courseEntity = courseRepository.save(entity);
		return buildResource(courseEntity);
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
		resource.add(linkTo(methodOn(CourseManagerController.class).show(entity.getId())).withSelfRel());
		if (entity.getStudentsSet() != null) {
			for (Student student : entity.getStudentsSet()) {
				resource.add(
						new Link(url + StudentController.PATH + "/" + student.getId(), RouteConstant.REL_TO_STUDENTS));
			}
		}
		return resource;
	}
}
