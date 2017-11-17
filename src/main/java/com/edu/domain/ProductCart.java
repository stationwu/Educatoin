package com.edu.domain;

import java.util.Set;

import javax.persistence.*;

import com.edu.domain.dto.DerivedProduct;

@Entity
@Table(name="productcart")
public class ProductCart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne(cascade = { CascadeType.ALL })
	private Customer customer;
	
	@OneToMany(fetch = FetchType.LAZY)
	private Set<Product> products;
	
	@OneToMany(fetch = FetchType.LAZY)
	private Set<DerivedProduct> derivedProducts;
	
	@OneToMany(fetch = FetchType.LAZY)
	private Set<ImageCollection> imageCollection;
	
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Set<Product> getProducts() {
		return products;
	}

	public void addProducts(Product product) {
		this.products.add(product);
	}
	
	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	public Set<DerivedProduct> getDerivedProducts() {
		return derivedProducts;
	}

	public void addDerivedProducts(DerivedProduct derivedProduct) {
		this.derivedProducts.add(derivedProduct);
	}
	
	public void setDerivedProducts(Set<DerivedProduct> derivedProducts) {
		this.derivedProducts = derivedProducts;
	}

	public Set<ImageCollection> getImageCollection() {
		return imageCollection;
	}

	public void addImageCollection(ImageCollection imageCollection) {
		this.imageCollection.add(imageCollection);
	}
	
	public void setImageCollection(Set<ImageCollection> imageCollection) {
		this.imageCollection = imageCollection;
	}

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
