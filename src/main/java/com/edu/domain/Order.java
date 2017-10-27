package com.edu.domain;

import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.MapKeyClass;
import javax.persistence.MapKeyColumn;
import javax.persistence.MapKeyJoinColumn;
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

	@ElementCollection
    @CollectionTable(name="PRODUCT_ORDER", joinColumns=@JoinColumn(name="ORDER_ID", referencedColumnName="ID"))
	@MapKeyJoinColumn(name="PRODUCT_ID", referencedColumnName="ID")
	@Column(name="COPIES_IN_ORDER")
	private Map<Product,Integer > productsMap;

	@ElementCollection
    @CollectionTable(name="DERIVEDPRODUCT_ORDER", joinColumns=@JoinColumn(name="ORDER_ID", referencedColumnName="ID"))
	@MapKeyJoinColumn(name="DERIVEDPRODUCT_ID", referencedColumnName="ID")
	@Column(name="COPIES_IN_ORDER")
	private Map<DerivedProduct, Integer> derivedProductsMap;

	@ElementCollection
    @CollectionTable(name="IMAGECOLLECTION_ORDER", joinColumns=@JoinColumn(name="ORDER_ID", referencedColumnName="ID"))
	@MapKeyJoinColumn(name="IMAGECOLLECTION_ID", referencedColumnName="ID")
	@Column(name="COPIES_IN_ORDER")
	private Map<ImageCollection, Integer> imageCollectionMap;

	private String date;

	private double totalAmount;

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
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

	public Map<Product, Integer> getProductsMap() {
		return productsMap;
	}

	public void setProductsMap(Map<Product, Integer> productsMap) {
		this.productsMap = productsMap;
	}

	public Map<DerivedProduct, Integer> getDerivedProductsMap() {
		return derivedProductsMap;
	}

	public void setDerivedProductsMap(Map<DerivedProduct, Integer> derivedProductsMap) {
		this.derivedProductsMap = derivedProductsMap;
	}

	public Map<ImageCollection, Integer> getImageCollectionMap() {
		return imageCollectionMap;
	}

	public void setImageCollectionMap(Map<ImageCollection, Integer> imageCollectionMap) {
		this.imageCollectionMap = imageCollectionMap;
	}

}
