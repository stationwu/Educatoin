package com.edu.domain;

import javax.persistence.*;

@Entity
@Table(name = "classprod")
public class ClassProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
	
	@ManyToOne
	private Student Student;
    
    @ManyToOne
	private Product product;
    
    private String description;

	public Student getStudent() {
		return Student;
	}

	public void setStudent(Student student) {
		Student = student;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
