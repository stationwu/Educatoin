package com.edu.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="productcart")
public class productCart {
	@OneToOne
	private Student student;
	
	@OneToMany(mappedBy = "productCart", orphanRemoval = true)
	@JsonIgnore
	private List<Product> products;
	
	@OneToMany(mappedBy = "derivedProductCart", orphanRemoval = true)
	@JsonIgnore
	private List<DerivedProduct> derivedProducts;
	
	@OneToMany(mappedBy = "imageCollectionCart", orphanRemoval = true)
	@JsonIgnore
	private List<ImageCollection> imageCollection;
	
	public void setStudent(Student student) {
		this.student = student;
	}
	
	public Student getStudent() {
		return student;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public List<DerivedProduct> getDerivedProducts() {
		return derivedProducts;
	}

	public void setDerivedProducts(List<DerivedProduct> derivedProducts) {
		this.derivedProducts = derivedProducts;
	}

	public List<ImageCollection> getImageCollection() {
		return imageCollection;
	}

	public void setImageCollection(List<ImageCollection> imageCollection) {
		this.imageCollection = imageCollection;
	}
	
}
