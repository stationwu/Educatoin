package com.edu.web.rest;

import com.edu.dao.CustomerRepository;
import com.edu.dao.StudentRepository;
import com.edu.domain.Customer;
import com.edu.domain.Student;
import com.edu.domain.dto.CustomerContainer;
import org.apache.http.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    public Customer create(@RequestBody @Valid CustomerContainer form) {
        Customer customer = new Customer(form.getOpenCode(), form.getName(), form.getMobilePhone(), form.getAddress());
        customer = repository.save(customer);

        for (CustomerContainer.ChildForm childForm : form.getChildren()) {
            Student student = new Student(childForm.getChildName(), childForm.getBirthday(), childForm.getClassPeriod(),
                    0, childForm.getClassPeriod(), true);
            student.setCustomer(customer);
            student = studentRepository.save(student);
            customer.addStudent(student);
        }

        return customer;
    }

    @PostMapping(path = PATH + "/{id}")
    public Customer edit(@PathVariable(value = "id") Long id,
                         @RequestBody @Valid Customer customer) {
        Customer entity = repository.findOne(id);
        entity.setName(customer.getName());
        entity.setAddress(customer.getAddress());
        entity.setMobilePhone(customer.getMobilePhone());
        return repository.save(entity);
    }

    @PutMapping(path = PATH + "/{id}")
    public String activateCustomer(@PathVariable(value = "id") Long id) throws HttpException  {
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
