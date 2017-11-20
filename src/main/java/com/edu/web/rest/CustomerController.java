package com.edu.web.rest;

import com.edu.dao.CustomerRepository;
import com.edu.dao.StudentRepository;
import com.edu.domain.Customer;
import com.edu.domain.Student;
import com.edu.domain.dto.ChildContainer;
import com.edu.domain.dto.CustomerContainer;
import com.edu.utils.Constant;
import org.apache.http.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class CustomerController {
    @Autowired
    private CustomerRepository repository;

    @Autowired
    private StudentRepository studentRepository;

    public static final String PATH = "/api/v1/Customer";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping(path = PATH + "/{id}")
    public ResponseEntity<Customer> show(@PathVariable("id") Long id) {
        Customer entity = repository.findOne(id);
        return new ResponseEntity<>(entity, HttpStatus.OK);
    }

    @PostMapping(path = PATH)
    public Customer create(@RequestBody @Valid Customer customer) throws HttpException {
        return repository.save(customer);
    }

    @PostMapping(path = PATH + "/SignUp")
    public Customer create(@RequestBody @Valid CustomerContainer customerDTO) {
        Customer customer;

        if (repository.isCustomerAlreadyRegistered(customerDTO.getOpenCode())) {
            customer = repository.findOneByOpenCode(customerDTO.getOpenCode());
        } else {
            customer = new Customer(customerDTO.getOpenCode(), customerDTO.getName(),
                    customerDTO.getMobilePhone(), customerDTO.getAddress());
            customer = repository.save(customer);

            for (ChildContainer childDTO : customerDTO.getChildren()) {
                Student student = new Student(childDTO.getChildName(), childDTO.getBirthday());
                student.setCustomer(customer);
                student = studentRepository.save(student);
                customer.addStudent(student);
            }
        }

        return customer;
    }

    @PostMapping(path = PATH + "/AddChild")
    public Customer addChild(@RequestBody @Valid List<ChildContainer> children, HttpSession session) {
        Customer customer = null;

        String openId = (String)session.getAttribute(Constant.SESSION_OPENID_KEY);

        if (openId != null) {
            customer = repository.findOneByOpenCode(openId);

            if (customer != null) {
                for (ChildContainer childDTO : children) {
                    Student student = new Student(childDTO.getChildName(), childDTO.getBirthday());
                    student.setCustomer(customer);
                    student = studentRepository.save(student);
                    customer.addStudent(student);
                }
            }
        }

        return customer;
    }

    @PutMapping(path = PATH + "/{id}")
    public String activateCustomer(@PathVariable(value = "id") Long id) throws HttpException {
        Customer customerObject = repository.findOne(id);
        customerObject.setActivated(true);
        repository.save(customerObject);
        return "客户激活成功!";
    }

    private Resource<Customer> buildResource(Customer customer) {
        Resource<Customer> resource = new Resource<>(customer);
        // Links
        resource.add(linkTo(methodOn(CustomerController.class).show(customer.getId())).withSelfRel());
        return resource;
    }
}
