package com.edu.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.edu.domain.Course;

public interface CourseRepository extends PagingAndSortingRepository<Course, Long> {
	Page<Course> findAllOrderByCourseName(Pageable pageable);
	Collection<Course> findByDateOrderByTimeFromAsc(String date);
	@Query("select c from Course c where c.date <= ?1 and c.timeFrom <= ?2 order by c.date desc, c.timeFrom desc")
	 List<?> search(String date, String hour, Pageable pageable);
}
