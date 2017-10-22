package com.edu.dao;


import org.springframework.data.repository.PagingAndSortingRepository;

import com.edu.domain.ProductCart;



public interface ProductCartRepository extends PagingAndSortingRepository<ProductCart, Long> {
}
