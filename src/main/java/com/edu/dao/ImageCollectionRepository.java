package com.edu.dao;


import org.springframework.data.repository.CrudRepository;

import com.edu.domain.ImageCollection;


public interface ImageCollectionRepository extends CrudRepository<ImageCollection, Long> {
}
