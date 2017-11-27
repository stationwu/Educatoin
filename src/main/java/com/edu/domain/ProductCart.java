package com.edu.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

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
	
	@OneToMany(fetch = FetchType.LAZY)
	private Set<ClassProduct> classProducts;
	
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Set<Product> getProducts() {
		Set<Product> productSet = new HashSet<>();
		for(Product product : this.products){
			if(product.isInvalidFlag() != true){
				productSet.add(product);
			}
		}
		return productSet;
	}

	public void addProducts(Product product) {
		this.products.add(product);
	}
	
	public void removeProduct(Product product) {
		this.products.remove(product);
	}
	
	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	public Set<DerivedProduct> getDerivedProducts() {
		Set<DerivedProduct> derivedProductList = new HashSet<>();
		for(DerivedProduct derivedProduct : this.derivedProducts){
			if(derivedProduct.getProduct().isInvalidFlag() != true){
				derivedProductList.add(derivedProduct);
			}
		}
		return derivedProductList;
	}

	public void addDerivedProducts(DerivedProduct derivedProduct) {
		this.derivedProducts.add(derivedProduct);
	}
	
	public void removeDerivedProduct(DerivedProduct derivedProduct) {
		this.derivedProducts.remove(derivedProduct);
	}
	
	public void setDerivedProducts(Set<DerivedProduct> derivedProducts) {
		this.derivedProducts = derivedProducts;
	}

	public Set<ImageCollection> getImageCollection() {
		Set<ImageCollection> imageCollectionSet = new HashSet<>();
		for(ImageCollection imageCollection : this.imageCollection){
			if(imageCollection.getProduct().isInvalidFlag() != true){
				imageCollectionSet.add(imageCollection);
			}
		}
		return imageCollectionSet;
	}

	public void addImageCollection(ImageCollection imageCollection) {
		this.imageCollection.add(imageCollection);
	}
	
	public void removeImageCollection(ImageCollection imageCollection) {
		this.imageCollection.remove(imageCollection);
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

	public Set<ClassProduct> getClassProducts() {
		Set<ClassProduct> classProductSet = new HashSet<>();
		for(ClassProduct classProduct : this.classProducts){
			if(classProduct.getProduct().isInvalidFlag() != true){
				classProductSet.add(classProduct);
			}
		}
		return classProductSet;
	}

	public void setClassProducts(Set<ClassProduct> classProducts) {
		this.classProducts = classProducts;
	}
	
	public void addClassProduct(ClassProduct classProduct) {
		this.classProducts.add(classProduct);
	}
	
	public void removeClassProduct(ClassProduct classProduct) {
		this.classProducts.remove(classProduct);
	}
    
}
