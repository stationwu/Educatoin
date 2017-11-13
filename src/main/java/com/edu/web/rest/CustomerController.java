package com.edu.web.rest;

import com.edu.dao.CustomerRepository;
import com.edu.domain.Customer;
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

    public static final String PATH = "/api/v1/Customer";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping(path = PATH + "/{id}")
    public ResponseEntity<Resource<Customer>> show(@PathVariable("id") Long id) {
        Customer entity = repository.findOne(id);
        return new ResponseEntity<>(buildResource(entity), HttpStatus.OK);
    }

    @PostMapping(path = PATH)
    public Resource<Customer> create(@RequestBody @Valid Customer customer) throws HttpException {
        return buildResource(repository.save(customer));
    }

    @PostMapping(path = PATH + "/{id}")
    public Resource<Customer> edit(@PathVariable(value = "id") Long id,
                                   @RequestBody @Valid Customer customer) {
        Customer entity = repository.findOne(id);
        entity.setName(customer.getName());
        entity.setAddress(customer.getAddress());
        entity.setMobilePhone(customer.getMobilePhone());
        return buildResource(repository.save(entity));
    }

    private Resource<Customer> buildResource(Customer customer) {
        Resource<Customer> resource = new Resource<>(customer);
        // Links
        resource.add(linkTo(methodOn(CustomerController.class).show(customer.getId())).withSelfRel());
        return resource;
    }
}
