package com.edu.domain.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.edu.domain.ImageCollection;
import com.edu.domain.Order;
import com.edu.domain.Product;

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
				.map(x -> new ProductContainer(x.getKey(),x.getValue(), 1));
		Stream<ProductContainer> derivedProductsStream = derivedProductsMap.entrySet().stream()
				.map(x -> new ProductContainer(x.getKey(),x.getValue(), 2));
		Stream<ProductContainer> imageCollectionStream = imageCollectionMap.entrySet().stream()
				.map(x -> new ProductContainer(x.getKey(),x.getValue(), 3));
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
