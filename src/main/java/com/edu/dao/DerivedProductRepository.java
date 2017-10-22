package com.edu.dao;


import org.springframework.data.repository.PagingAndSortingRepository;

import com.edu.domain.DerivedProduct;

public interface DerivedProductRepository extends PagingAndSortingRepository<DerivedProduct, Long> {
}
