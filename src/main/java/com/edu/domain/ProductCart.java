package com.edu.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="productcart")
public class ProductCart extends BaseEntity{
	@OneToOne
	private Student student;
	
	@OneToMany(fetch = FetchType.LAZY)
	private Set<Product> products;
	
	@OneToMany(fetch = FetchType.LAZY)
	private Set<DerivedProduct> derivedProducts;
	
	@OneToMany(fetch = FetchType.LAZY)
	private Set<ImageCollection> imageCollection;
	
	public void setStudent(Student student) {
		this.student = student;
	}
	
	public Student getStudent() {
		return student;
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
	
}
