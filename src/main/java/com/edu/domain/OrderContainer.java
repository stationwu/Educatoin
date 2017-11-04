package com.edu.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OrderContainer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private Order order;
	private List<ProductContainer> productContainers;

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public List<ProductContainer> getProductContainers() {
		return productContainers;
	}

	public void setProductContainers(List<ProductContainer> productContainers) {
		this.productContainers = productContainers;
	}

	public OrderContainer(Order order, Map<Product, Integer> productsMap,
			Map<DerivedProduct, Integer> derivedProductsMap, Map<ImageCollection, Integer> imageCollectionMap) {
		super();
		this.order = order;
		Stream<ProductContainer> productsStream = productsMap.entrySet().stream()
				.map(x -> new ProductContainer(x.getKey().getProductName(),
						x.getKey().getProductCategory().getCategoryName(), x.getKey().getProductPrice(),
						x.getKey().getProductDescription(),
						"/Images/" + x.getKey().getProductImages().stream().findFirst().get().getId() + "/thumbnail",
						x.getValue(), x.getKey().getId(), 1));
		Stream<ProductContainer> derivedProductsStream = derivedProductsMap.entrySet().stream()
				.map(x -> new ProductContainer(x.getKey().getProduct().getProductName(),
						x.getKey().getProduct().getProductCategory().getCategoryName(),
						x.getKey().getProduct().getProductPrice(), x.getKey().getProduct().getProductDescription(),
						"/Images/" + x.getKey().getImage().getId() + "/thumbnail", x.getValue(), x.getKey().getId(),
						2));
		Stream<ProductContainer> imageCollectionStream = imageCollectionMap.entrySet().stream()
				.map(x -> new ProductContainer(x.getKey().getCollectionName(), "作品集", x.getKey().getPrice(),
						x.getKey().getCollectionDescription(),
						"/Images/" + x.getKey().getImageCollection().stream().findFirst().get().getId() + "/thumbnail",
						x.getValue(), x.getKey().getId(), 3));
		this.productContainers = Stream.of(productsStream, derivedProductsStream, imageCollectionStream).flatMap(i -> i)
				.collect(Collectors.toCollection(ArrayList::new));
	}

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
