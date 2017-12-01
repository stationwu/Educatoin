package com.edu.dao;

import org.springframework.data.repository.CrudRepository;

import com.edu.domain.SmsTemplate;

public interface SmsTemplateRepository extends CrudRepository<SmsTemplate, Long> {

}
