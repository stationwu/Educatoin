package com.edu.dao;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.edu.domain.Student;

public interface StudentRepository extends PagingAndSortingRepository<Student, Long> {
	Page<Student> findAllOrderByStudentName(Pageable pageable);
	Student findOneByOpenCode(String openCode);
	
	@Query("select count(s)>0 from Student s where s.openCode = ?1")
	boolean isStudentAlreadyRegistered(String openCode);
}
