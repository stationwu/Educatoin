package com.edu.domain.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.edu.domain.ClassProduct;
import com.edu.domain.DerivedProduct;
import com.edu.domain.ImageCollection;
import com.edu.domain.Product;

public class ProductContainer {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String productName;

	private String category;

	private double productPrice = 0.0d;

	private String productDescription;

	private String imageUrl;

	private int quantity;

	private String longProductDescription;
	
	private int type;
	
	private int priority;
	
	private int classPeriod;

	private final boolean checked = false;

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

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public boolean getChecked() {
		return checked;
	}

	public ProductContainer() {

	}

	public ProductContainer(String productName, String category, double productPrice, String productDescription,
			String imageUrl, int quantity, Long id, int type) {
		super();
		this.productName = productName;
		this.category = category;
		this.productPrice = productPrice;
		this.productDescription = productDescription;
		this.imageUrl = imageUrl;
		this.quantity = quantity;
		this.id = id;
		this.type = type;
	}

	public ProductContainer(Product product, int quantity, int type) {
		super();
		this.productName = product.getProductName();
		this.category = product.getProductCategory().getCategoryName();
		this.productPrice = product.getProductPrice();
		this.productDescription = product.getProductDescription();
		this.longProductDescription = product.getLongProductDescription();
		this.classPeriod = product.getClassPeriod();
		this.priority = product.getPriority();
		this.imageUrl = "/Images/" + product.getProductImages().stream().sorted((x, y) -> (int) (x.getId() - y.getId()))
				.findFirst().get().getId() + "/thumbnail";
		this.quantity = quantity;
		this.id = product.getId();
		this.type = type;
	}

	public ProductContainer(DerivedProduct product, int quantity, int type) {
		super();
		this.productName = product.getProduct().getProductName();
		this.category = product.getProduct().getProductCategory().getCategoryName();
		this.productPrice = product.getProduct().getProductPrice();
		this.productDescription = product.getDescription();
		this.longProductDescription = product.getProduct().getLongProductDescription();
		this.classPeriod = product.getProduct().getClassPeriod();
		this.priority = product.getProduct().getPriority();
		this.imageUrl = "/Images/" + product.getProduct().getProductImages().stream()
				.sorted((x, y) -> (int) (x.getId() - y.getId())).findFirst().get().getId() + "/thumbnail";
		this.quantity = quantity;
		this.id = product.getId();
		this.type = type;
	}

	public ProductContainer(ImageCollection product, int quantity, int type) {
		super();
		this.productName = product.getProduct().getProductName();
		this.category = product.getProduct().getProductCategory().getCategoryName();
		this.productPrice = product.getProduct().getProductPrice();
		this.productDescription = product.getCollectionDescription();
		this.longProductDescription = product.getProduct().getLongProductDescription();
		this.classPeriod = product.getProduct().getClassPeriod();
		this.priority = product.getProduct().getPriority();
		this.imageUrl = "/Images/" + product.getImageCollection().stream()
				.sorted((x, y) -> (int) (x.getId() - y.getId())).findFirst().get().getId() + "/thumbnail";
		this.quantity = quantity;
		this.id = product.getId();
		this.type = type;
	}

	public ProductContainer(ClassProduct product, int quantity, int type) {
		super();
		this.productName = product.getProduct().getProductName();
		this.category = product.getProduct().getProductCategory().getCategoryName();
		this.productPrice = product.getProduct().getProductPrice();
		this.productDescription = product.getDescription();
		this.longProductDescription = product.getProduct().getLongProductDescription();
		this.classPeriod = product.getProduct().getClassPeriod();
		this.priority = product.getProduct().getPriority();
		this.imageUrl = "/Images/" + product.getProduct().getProductImages().stream()
				.sorted((x, y) -> (int) (x.getId() - y.getId())).findFirst().get().getId() + "/thumbnail";
		this.quantity = quantity;
		this.id = product.getId();
		this.type = type;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public String getLongProductDescription() {
		return longProductDescription;
	}

	public void setLongProductDescription(String longProductDescription) {
		this.longProductDescription = longProductDescription;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getClassPeriod() {
		return classPeriod;
	}

	public void setClassPeriod(int classPeriod) {
		this.classPeriod = classPeriod;
	}
	
}
