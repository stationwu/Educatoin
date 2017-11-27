package com.edu.dao;


import org.springframework.data.repository.PagingAndSortingRepository;

import com.edu.domain.ClassProduct;

public interface ClassProductRepository extends PagingAndSortingRepository<ClassProduct, Long> {
}
