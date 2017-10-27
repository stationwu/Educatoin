package com.edu.domain;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Set;

public class ProductContainer extends BaseEntity {
	private String productName;

	private String category;

	private double productPrice = 0.0d;

	private String productDescription;

	private String imageUrl;

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
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

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public ProductContainer(String productName, String category, double productPrice, String productDescription,
			String imageUrl) {
		super();
		this.productName = productName;
		this.category = category;
		this.productPrice = productPrice;
		this.productDescription = productDescription;
		this.imageUrl = imageUrl;
	}
}
