package com.edu.web.rest;

import com.edu.config.ImageProperties;
import com.edu.controller.RouteConstant;
import com.edu.dao.CourseRepository;
import com.edu.dao.StudentRepository;
import com.edu.domain.Course;
import com.edu.domain.Image;
import com.edu.domain.Student;
import com.edu.storage.FileStorageService;
import com.edu.utils.ImageService;
import org.apache.http.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class StudentManagerController {
    public static final String PATH = "/StudentManager";
    private final StudentRepository studentRepository;
    private final ImageService imageService;
    private final CourseRepository courseRepository;
    private final FileStorageService fileStorageService;
    private final ImageProperties imageProperties;
    // private final Environment environment;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public StudentManagerController(StudentRepository studentRepository,
                                    ImageService imageService,
                                    CourseRepository courseRepository,
                                    FileStorageService fileStorageService,
                                    ImageProperties imageProperties) {
        this.studentRepository = studentRepository;
        this.imageService = imageService;
        this.courseRepository = courseRepository;
        this.fileStorageService = fileStorageService;
        this.imageProperties = imageProperties;
        // this.environment = environment;
    }

    @RequestMapping(path = PATH, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<Resources<Resource<Student>>> index(@PageableDefault(value = 15, sort = {
            "studentName"}, direction = Sort.Direction.ASC) Pageable pageRequest) {
        Page<Student> entityPage = studentRepository.findAll(pageRequest);
        List<Student> entities = entityPage.getContent();
        return new ResponseEntity<>(buildResources(entities), HttpStatus.OK);
    }

    @RequestMapping(path = PATH + "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource<Student>> show(@PathVariable("id") String id) {
        Student entity = studentRepository.findOne(id);
        return new ResponseEntity<>(buildResource(entity), HttpStatus.OK);
    }

    @PostMapping(path = PATH)
    public Resource<Student> manageStudent(@RequestBody @Valid Student student) throws HttpException {
        Student entity = studentRepository.findOne(student.getId());
        entity.setClassPeriod(student.getClassPeriod());
        Student entityUser = studentRepository.save(entity);
        return buildResource(entityUser);
    }

    @PostMapping(path = PATH + "/{id}")
    public HttpEntity<Resource<Student>> signAndUploadImage(@PathVariable("id") String studentId,
                                                            @RequestParam(value = "imageName") String imageName,
                                                            @RequestParam(value = "date") String date,
                                                            @RequestParam(value = "hour") String hour,
                                                            @RequestParam(value = "material") String material,
                                                            @RequestParam("file") MultipartFile files[])
            throws Exception {
        Student student = studentRepository.findOne(studentId);
        @SuppressWarnings("unchecked")
        List<Course> courses = (List<Course>) courseRepository.search(date, hour, new PageRequest(0, 1));
        Course course = courses.get(0);
        for(Course cou:student.getCoursesSet()){
        	if(course.getId() == cou.getId()){
        		throw new Exception("已签到过此次课程！");
        	}
        }
        student.addCourse(course);
        student.removeReservedCourse(course);
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                Image img = imageService.saveIn3Size(imageName, material, student, course, file);
                student.addImage(img);
            }
        }

		student.setDonePeriods(student.getDonePeriods()+1);
		student.setLeftPeriods(student.getClassPeriod()-student.getDonePeriods());

        Student entity = studentRepository.save(student);
        return new ResponseEntity<>(buildResource(entity), HttpStatus.OK);
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
        // String address = InetAddress.getLoopbackAddress().getHostName();
        // String port = environment.getProperty("server.port");
        String url = "";// "http://" + address + ":" + port;
        Resource<Student> resource = new Resource<>(entity);
        // Links
        resource.add(linkTo(methodOn(StudentController.class).show(entity.getId())).withSelfRel());
        if (entity.getCoursesSet() != null) {
            for (Course course : entity.getCoursesSet()) {
                resource.add(
                        new Link(url + CourseController.PATH + "/" + course.getId(), RouteConstant.REL_TO_COURSES));
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
