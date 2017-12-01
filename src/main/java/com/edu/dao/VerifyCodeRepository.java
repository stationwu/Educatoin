package com.edu.dao;

import org.springframework.data.repository.CrudRepository;

import com.edu.domain.VerifyCode;

public interface VerifyCodeRepository extends CrudRepository<VerifyCode, Long>{

}
