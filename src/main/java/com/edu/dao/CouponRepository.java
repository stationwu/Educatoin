package com.edu.dao;

import com.edu.domain.Coupon;
import org.springframework.data.repository.CrudRepository;

public interface CouponRepository extends CrudRepository<Coupon, Long> {
}
