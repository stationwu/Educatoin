package com.edu.dao;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.edu.domain.Student;
import com.edu.domain.StudentContainer;

public interface StudentRepository extends PagingAndSortingRepository<Student, String> {
	@Query("SELECT s from Student s join s.customer c where s.birthday = ?1 or s.studentName LIKE CONCAT('%',?1,'%') or s.id LIKE CONCAT('%',?1,'%') or c.mobilePhone = ?1")
	 List<Student> search(String keyword);
}
