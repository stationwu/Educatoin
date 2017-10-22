package com.edu.dao;


import org.springframework.data.repository.PagingAndSortingRepository;

import com.edu.domain.Product;

public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {
}
