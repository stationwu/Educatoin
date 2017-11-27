package com.edu.domain;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Set;

@Entity
@Table(name = "product")
public class Product {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

	private String productName;

	@ManyToOne
	@JoinColumn(name = "CATEGORY_ID")
	private ProductCategory productCategory;

	private double productPrice = 0.0d;

	private String productDescription;
	
	private String longProductDescription;

	@OneToMany(fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<Image> productImages;

	private boolean derivedProductFlag;
	
	private boolean imageCollectionFlag;
	
	private boolean classFlag;
	
	private boolean invalidFlag;
	
	private int classPeriod;
	
	private int numberOfPic;
	
	private int priority;
	
	public Product() {

	}

	public Product(String productName, ProductCategory productCategory, double productPrice, String productDescription,
			boolean derivedProductFlag, boolean imageCollectionFlag, boolean classFlag, int classPeriod) {
		this.productName = productName;
		this.productCategory = productCategory;
		this.productPrice = productPrice;
		this.productDescription = productDescription;
		this.derivedProductFlag = derivedProductFlag;
		this.imageCollectionFlag = imageCollectionFlag;
		this.classFlag = classFlag;
		this.classPeriod = classPeriod;
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

	public void setId(long id) {
            this.id = id;
        }

	public long getId() {
            return id;
        }

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getLongProductDescription() {
		return longProductDescription;
	}

	public void setLongProductDescription(String longProductDescription) {
		this.longProductDescription = longProductDescription;
	}

	public boolean isImageCollectionFlag() {
		return imageCollectionFlag;
	}

	public void setImageCollectionFlag(boolean imageCollectionFlag) {
		this.imageCollectionFlag = imageCollectionFlag;
	}

	public boolean isClassFlag() {
		return classFlag;
	}

	public void setClassFlag(boolean classFlag) {
		this.classFlag = classFlag;
	}

	public int getClassPeriod() {
		return classPeriod;
	}

	public void setClassPeriod(int classPeriod) {
		this.classPeriod = classPeriod;
	}

	public int getNumberOfPic() {
		return numberOfPic;
	}

	public void setNumberOfPic(int numberOfPic) {
		this.numberOfPic = numberOfPic;
	}

	public boolean isInvalidFlag() {
		return invalidFlag;
	}

	public void setInvalidFlag(boolean invalidFlag) {
		this.invalidFlag = invalidFlag;
	}
}
