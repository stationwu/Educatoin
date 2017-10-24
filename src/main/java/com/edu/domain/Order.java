package com.edu.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "productorder")
public class Order extends BaseEntity {
	@ManyToOne
	@JoinColumn(name = "STUDENT_ID")
	@JsonIgnore
	private Student student;

	@OneToMany(fetch = FetchType.LAZY)
	private Set<Product> products;

	@OneToMany(fetch = FetchType.LAZY)
	private Set<DerivedProduct> derivedProducts;

	@OneToMany(fetch = FetchType.LAZY)
	private Set<ImageCollection> imageCollection;

	private String date;

	private double totalAmount;
	
	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Set<Product> getProducts() {
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	public Set<DerivedProduct> getDerivedProducts() {
		return derivedProducts;
	}

	public void setDerivedProducts(Set<DerivedProduct> derivedProducts) {
		this.derivedProducts = derivedProducts;
	}

	public Set<ImageCollection> getImageCollection() {
		return imageCollection;
	}

	public void setImageCollection(Set<ImageCollection> imageCollection) {
		this.imageCollection = imageCollection;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
}
