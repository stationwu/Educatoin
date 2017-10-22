package com.edu.dao;


import org.springframework.data.repository.PagingAndSortingRepository;

import com.edu.domain.Order;

public interface OrderRepository extends PagingAndSortingRepository<Order, Long> {
}
