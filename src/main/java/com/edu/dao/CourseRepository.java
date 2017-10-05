package com.edu.dao;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.edu.domain.Course;

public interface CourseRepository extends PagingAndSortingRepository<Course, Long> {
	Page<Course> findAllOrderByCourseName(Pageable pageable);
	Collection<Course> findByDateOrderByTimeFromAsc(String date);
}
