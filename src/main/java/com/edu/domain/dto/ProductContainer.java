package com.edu.domain.dto;

import com.edu.domain.ClassProduct;
import com.edu.domain.DerivedProduct;
import com.edu.domain.ImageCollection;
import com.edu.domain.Product;

import lombok.Data;

@Data
public class ProductContainer {

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

    public ProductContainer() {

    }

    public ProductContainer(String productName, String category,
            double productPrice, String productDescription, String imageUrl,
            int quantity, Long id, int type) {
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
        this.imageUrl = "/Images/" + product.getProductImages().stream().sorted(
                (x, y) -> (int) (x.getId() - y.getId())).findFirst().get()
                .getId() + "/thumbnail";
        this.quantity = quantity;
        this.id = product.getId();
        this.type = type;
    }

    public ProductContainer(DerivedProduct product, int quantity, int type) {
        super();
        this.productName = product.getProduct().getProductName();
        this.category = product.getProduct().getProductCategory()
                .getCategoryName();
        this.productPrice = product.getProduct().getProductPrice();
        this.productDescription = product.getDescription();
        this.longProductDescription = product.getProduct()
                .getLongProductDescription();
        this.classPeriod = product.getProduct().getClassPeriod();
        this.priority = product.getProduct().getPriority();
        this.imageUrl = "/Images/" + product.getProduct().getProductImages()
                .stream().sorted((x, y) -> (int) (x.getId() - y.getId()))
                .findFirst().get().getId() + "/thumbnail";
        this.quantity = quantity;
        this.id = product.getId();
        this.type = type;
    }

    public ProductContainer(ImageCollection product, int quantity, int type) {
        super();
        this.productName = product.getProduct().getProductName();
        this.category = product.getProduct().getProductCategory()
                .getCategoryName();
        this.productPrice = product.getProduct().getProductPrice();
        this.productDescription = product.getCollectionDescription();
        this.longProductDescription = product.getProduct()
                .getLongProductDescription();
        this.classPeriod = product.getProduct().getClassPeriod();
        this.priority = product.getProduct().getPriority();
        this.imageUrl = "/Images/" + product.getImageCollection().stream()
                .sorted((x, y) -> (int) (x.getId() - y.getId())).findFirst()
                .get().getId() + "/thumbnail";
        this.quantity = quantity;
        this.id = product.getId();
        this.type = type;
    }

    public ProductContainer(ClassProduct product, int quantity, int type) {
        super();
        this.productName = product.getProduct().getProductName();
        this.category = product.getProduct().getProductCategory()
                .getCategoryName();
        this.productPrice = product.getProduct().getProductPrice();
        this.productDescription = product.getDescription();
        this.longProductDescription = product.getProduct()
                .getLongProductDescription();
        this.classPeriod = product.getProduct().getClassPeriod();
        this.priority = product.getProduct().getPriority();
        this.imageUrl = "/Images/" + product.getProduct().getProductImages()
                .stream().sorted((x, y) -> (int) (x.getId() - y.getId()))
                .findFirst().get().getId() + "/thumbnail";
        this.quantity = quantity;
        this.id = product.getId();
        this.type = type;
    }
}
