package com.edu.domain;

import java.util.Map;
import javax.persistence.*;

import com.edu.domain.dto.DerivedProduct;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "productorder")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    private String date;
    
    private Status status;

    private double totalAmount;
    
    @ManyToOne
    @JoinColumn(name = "CUSTOMER_ID")
    @JsonIgnore
    private Customer customer;  
    
	@ElementCollection
    @CollectionTable(name="ORDER_PRODUCT", joinColumns=@JoinColumn(name="ORDER_ID", referencedColumnName="ID"))
	@MapKeyJoinColumn(name="PRODUCT_ID", referencedColumnName="ID")
	@Column(name="COPIES_IN_ORDER")
	private Map<Product,Integer > productsMap;

	@ElementCollection
    @CollectionTable(name="ORDER_DERIVEDPRODUCT", joinColumns=@JoinColumn(name="ORDER_ID", referencedColumnName="ID"))
	@MapKeyJoinColumn(name="DERIVEDPRODUCT_ID", referencedColumnName="ID")
	@Column(name="COPIES_IN_ORDER")
	private Map<DerivedProduct, Integer> derivedProductsMap;

	@ElementCollection
    @CollectionTable(name="ORDER_IMAGECOLLECTION", joinColumns=@JoinColumn(name="ORDER_ID", referencedColumnName="ID"))
	@MapKeyJoinColumn(name="IMAGECOLLECTION_ID", referencedColumnName="ID")
	@Column(name="COPIES_IN_ORDER")
	private Map<ImageCollection, Integer> imageCollectionMap;


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

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Status getStatus() {
		return status;
	}

	public String getStatusText() {
		if (status == null) {
			return "未知订单状态";
		}

		String text;

		switch (status) {
			case PAID:
				text = "已支付";
				break;
			case CREATED:
				text = "等待支付";
				break;
			case PREPAID:
				text = "付款中";
				break;
			case CANCELLED:
				text = "已取消";
				break;
			case COMPLETED:
				text = "已完成";
				break;
			default:
				text = "未知订单状态";
		}

		return text;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public enum Status {
		CREATED, PREPAID, PAID, CANCELLED, COMPLETED
	}
}
