package com.edu.dao;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.edu.domain.Student;

public interface StudentRepository extends PagingAndSortingRepository<Student, String> {
	@Query("select s from Student s where s.studentName like :keyword or s.birthday like :keyword or s.id like :keyword)")
	 List search(@Param("keyword")String keyword);
}
