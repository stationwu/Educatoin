package com.edu.domain;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Set;

@Entity
@Table(name = "product")
public class Product extends BaseEntity {
	private String productName;

	@ManyToOne
	@JoinColumn(name = "CATEGORY_ID")
	private ProductCategory productCategory;

	private double productPrice = 0.0d;

	private String productDescription;

	@OneToMany(fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<Image> productImages;

	private boolean derivedProductFlag;

	public Product() {

	}

	public Product(String productName, ProductCategory productCategory, double productPrice, String productDescription,
			boolean derivedProductFlag) {
		this.productName = productName;
		this.productCategory = productCategory;
		this.productPrice = productPrice;
		this.productDescription = productDescription;
		this.derivedProductFlag = derivedProductFlag;
	}

	public boolean getDerivedProductFlag() {
		return derivedProductFlag;
	}

	public void setDerivedProductFlag(boolean derivedProductFlag) {
		this.derivedProductFlag = derivedProductFlag;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public ProductCategory getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(ProductCategory productCategory) {
		this.productCategory = productCategory;
	}

	public double getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(double productPrice) {
		this.productPrice = productPrice;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public Set<Image> getProductImages() {
		return productImages;
	}

	public void setProductImages(Set<Image> productImages) {
		this.productImages = productImages;
	}
}
