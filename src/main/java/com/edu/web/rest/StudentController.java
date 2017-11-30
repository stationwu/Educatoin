package com.edu.web.rest;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.apache.http.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
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
import com.edu.dao.CustomerRepository;
import com.edu.dao.StudentRepository;
import com.edu.domain.Course;
import com.edu.domain.Image;
import com.edu.domain.Student;

@RestController
public class StudentController {
    public static final String PATH = "/api/v1/Student";
    public static final String RELATIONSHIP_PATH = "/api/v1/Student/Customer";
    public static final String RELATIVE_COURSE_PATH = CourseController.PATH
            + "/{courseId}" + PATH;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final CustomerRepository customerRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public StudentController(StudentRepository studentRepository,
            CourseRepository courseRepository,
            CustomerRepository customerRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.customerRepository = customerRepository;
    }

    @RequestMapping(path = PATH + "/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource<Student>> show(
        @PathVariable("id") String id) {
        Student entity = studentRepository.findOne(id);
        return new ResponseEntity<>(buildResource(entity), HttpStatus.OK);
    }

    @RequestMapping(path = RELATIVE_COURSE_PATH,
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resources<Resource<Student>>> showStudentbyCourse(
        @PathVariable("courseId") @Min(0) Long courseId) {
        Course course = courseRepository.findOne(courseId);
        Iterable<Student> entities = course.getStudentsSet();
        return new ResponseEntity<>(buildResources(entities), HttpStatus.OK);
    }

//    @PostMapping(path = RELATIONSHIP_PATH + "/{id}")
//    public Resources<Resource<Student>> createStudentbyCustomer(@PathVariable(
//        value = "id") String id, @RequestBody @Valid List<Student> students)
//        throws HttpException {
//        Customer studentsParent = customerRepository.findOne(Long.parseLong(id));
//        List<Student> newCreatedStudents = new ArrayList<>();
//        for (Student student : students) {
//            student.setCustomer(studentsParent);
//            Student savedStudent = studentRepository.save(student);
//
//            newCreatedStudents.add(savedStudent);
//        }
//
//        return buildResources(newCreatedStudents);
//    }

    @PostMapping(path = PATH + "/{id}")
    public Resource<Student> editStudent(@PathVariable(value = "id") String id,
        @RequestBody @Valid Student student) throws HttpException {
        Student entity = studentRepository.findOne(id);
        entity.setStudentName(student.getStudentName());
        entity.setClassPeriod(student.getClassPeriod());
        entity.setBirthday(student.getBirthday());
        Student savedStudent = studentRepository.save(entity);
        return buildResource(savedStudent);
    }

    private Resources<Resource<Student>> buildResources(
        Iterable<Student> entities) {
        List<Resource<Student>> resourceList = new ArrayList<>();

        // Links
        for (Student entity : entities) {
            resourceList.add(buildResource(entity));
        }

        Resources<Resource<Student>> resources = new Resources<>(resourceList);

        return resources;
    }

    private Resource<Student> buildResource(Student entity) {
        // String address = InetAddress.getLoopbackAddress().getHostName();
        // String port = environment.getProperty("server.port");
        String url = "";// "http://" + address + ":" + port;
        Resource<Student> resource = new Resource<>(entity);
        // Links
        resource.add(linkTo(methodOn(StudentController.class).show(entity
                .getId())).withSelfRel());
        if (entity.getCoursesSet() != null) {
            for (Course course : entity.getCoursesSet()) {
                resource.add(new Link(url + CourseController.PATH + "/" + course
                        .getId(), RouteConstant.REL_TO_COURSES));
            }
        }
        if (entity.getImagesSet() != null) {
            for (Image image : entity.getImagesSet()) {
                resource.add(new Link(url + ImageController.PATH + "/" + image
                        .getId() + "/thumbnail", RouteConstant.REL_TO_IMAGES));
            }
        }
        return resource;
    }
}
