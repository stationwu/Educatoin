package com.edu.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.edu.domain.VerifyCode;

public interface VerifyCodeRepository extends CrudRepository<VerifyCode, Long>{
    @Query("SELECT c FROM VerifyCode c WHERE c.exceedTime > current_timestamp() and c.id = ?1")
    VerifyCode findOneVerifyCodeById(Long id);
}
