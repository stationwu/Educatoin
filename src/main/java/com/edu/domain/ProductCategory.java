package com.edu.domain;

import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="productcategory")
public class ProductCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column( unique = true )
	private String categoryName;
	
	private String categoryDescription;
	
	@OneToMany(mappedBy = "productCategory", orphanRemoval = true)
	@JsonIgnore
	private Set<Product> products;
	
	public ProductCategory(){
		
	}
	
	public ProductCategory(String categoryName, String categoryDescription) {
		this.categoryName = categoryName;
		this.categoryDescription = categoryDescription;
	}
	
	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategoryDescription() {
		return categoryDescription;
	}

	public void setCategoryDescription(String categoryDescription) {
		this.categoryDescription = categoryDescription;
	}

	public Set<Product> getProducts() {
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
