package com.edu.dao;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.edu.domain.Student;

public interface StudentRepository extends PagingAndSortingRepository<Student, Long> {
	Page<Student> findAllOrderByStudentName(Pageable pageable);
	Student findOneByOpenCode(String openCode);
}
