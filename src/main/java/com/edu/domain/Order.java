package com.edu.domain;

import java.util.Map;
import javax.persistence.*;

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

    public static String[] ALL_STATUS = {
    		"等待付款", "付款中", "已付款", "已取消", "已发货", "退款申请中", "已退款"
	};

    @OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "payment_id")
	private Payment payment;
    
    @ManyToOne
    @JoinColumn(name = "CUSTOMER_ID")
    @JsonIgnore
    private Customer customer;

	@JsonIgnore
	@ElementCollection
    @CollectionTable(name="ORDER_PRODUCT", joinColumns=@JoinColumn(name="ORDER_ID", referencedColumnName="ID"))
	@MapKeyJoinColumn(name="PRODUCT_ID", referencedColumnName="ID")
	@Column(name="COPIES_IN_ORDER")
	private Map<Product,Integer > productsMap;

	@JsonIgnore
	@ElementCollection
    @CollectionTable(name="ORDER_DERIVEDPRODUCT", joinColumns=@JoinColumn(name="ORDER_ID", referencedColumnName="ID"))
	@MapKeyJoinColumn(name="DERIVEDPRODUCT_ID", referencedColumnName="ID")
	@Column(name="COPIES_IN_ORDER")
	private Map<DerivedProduct, Integer> derivedProductsMap;

	@JsonIgnore
	@ElementCollection
    @CollectionTable(name="ORDER_IMAGECOLLECTION", joinColumns=@JoinColumn(name="ORDER_ID", referencedColumnName="ID"))
	@MapKeyJoinColumn(name="IMAGECOLLECTION_ID", referencedColumnName="ID")
	@Column(name="COPIES_IN_ORDER")
	private Map<ImageCollection, Integer> imageCollectionMap;

	@JsonIgnore
	@ElementCollection
    @CollectionTable(name="ORDER_CLASSPRODUCT", joinColumns=@JoinColumn(name="ORDER_ID", referencedColumnName="ID"))
	@MapKeyJoinColumn(name="CLASSPRODUCT_ID", referencedColumnName="ID")
	@Column(name="COPIES_IN_ORDER")
	private Map<ClassProduct, Integer> classProductsMap;
	
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
		if (status.ordinal() >= ALL_STATUS.length) {
			return "未知订单状态";
		}
		return ALL_STATUS[status.ordinal()];
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public enum Status {
		CREATED,     // Order created at this app but not yet at wechat
        NOTPAY,      // equals to wechat NOTPAY
        PAID,        // equals to wechat SUCCESS
        CANCELLED,   // equals to wechat CLOSED
        DELIVERED,   // Paid and goods delivered
		REFUND_REQUESTED, // paid and request to refund
        REFUND       // equals to wechat REFUND
	}

	public Map<ClassProduct, Integer> getClassProductsMap() {
		return classProductsMap;
	}

	public void setClassProductsMap(Map<ClassProduct, Integer> classProductsMap) {
		this.classProductsMap = classProductsMap;
	}

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}
}
