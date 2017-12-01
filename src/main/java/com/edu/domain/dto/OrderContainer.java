package com.edu.domain.dto;

import com.edu.domain.ClassProduct;
import com.edu.domain.DerivedProduct;
import com.edu.domain.ImageCollection;
import com.edu.domain.Order;
import com.edu.domain.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
public class OrderContainer {

	@JsonIgnore
    private Order order;

	@JsonProperty("items")
	private List<ProductContainer> productContainers;

	public OrderContainer(Order order, Map<Product, Integer> productsMap,
			Map<DerivedProduct, Integer> derivedProductsMap, Map<ImageCollection, Integer> imageCollectionMap,
			Map<ClassProduct, Integer> classProductMap) {
		super();

		this.order = order;

		Stream<ProductContainer> productsStream = productsMap.entrySet().stream()
				.map(x -> new ProductContainer(x.getKey(),x.getValue(), 1));
		Stream<ProductContainer> derivedProductsStream = derivedProductsMap.entrySet().stream()
				.map(x -> new ProductContainer(x.getKey(),x.getValue(), 2));
		Stream<ProductContainer> imageCollectionStream = imageCollectionMap.entrySet().stream()
				.map(x -> new ProductContainer(x.getKey(),x.getValue(), 3));
		Stream<ProductContainer> classProductStream = classProductMap.entrySet().stream()
				.map(x -> new ProductContainer(x.getKey(),x.getValue(), 4));
		this.productContainers = Stream.of(productsStream, derivedProductsStream, imageCollectionStream, classProductStream).flatMap(i -> i)
				.collect(Collectors.toCollection(ArrayList::new));
	}

    public long getId() {
		return order.getId();
	}

	public Order.Status getStatus() {
		return order.getStatus();
	}

	public String getStatusText() {
		return order.getStatusText();
	}

	public double getTotalAmount() {
		return order.getTotalAmount();
	}

	public String getCreationDate() {
		return order.getDate();
	}

	public int getItemCount() {
		return productContainers.size();
	}
}
