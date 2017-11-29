package com.edu.dao;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.edu.domain.Product;

public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {
	
	@Query("select s from Product s where s.derivedProductFlag = TRUE and s.invalidFlag != TRUE order by s.priority desc, s.id desc")
	List<Product> getDerivedProductList();
	
	@Query("select s from Product s where s.imageCollectionFlag = TRUE and s.invalidFlag != TRUE order by s.priority desc, s.id desc")
	List<Product> getImageCollectionProductList();
	
	@Query("select s from Product s where s.classFlag = TRUE and s.invalidFlag != TRUE order by s.priority desc, s.id desc")
	List<Product> getClassProductList();
}
