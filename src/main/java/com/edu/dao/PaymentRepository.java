package com.edu.dao;

import com.edu.domain.Payment;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.LockModeType;

public interface PaymentRepository extends CrudRepository<Payment, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select * from Payment where id = ?1")
    Payment findOneForUpdate(Long id);
}
