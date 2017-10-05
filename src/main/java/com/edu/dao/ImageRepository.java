package com.edu.dao;


import org.springframework.data.repository.CrudRepository;

import com.edu.domain.Image;

public interface ImageRepository extends CrudRepository<Image, Long> {
}
