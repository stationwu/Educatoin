package com.edu.dao;


import org.springframework.data.repository.PagingAndSortingRepository;

import com.edu.domain.ProductCategory;


public interface ProductCategoryRepository extends PagingAndSortingRepository<ProductCategory, Long> {
}
