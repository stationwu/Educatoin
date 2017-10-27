package com.edu.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "imagecollection")
public class ImageCollection extends BaseEntity {
	
	public String collectionName;
	
	public String collectionDescription;
	
	@OneToMany(fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<Image> imageSet;

	private double price = 0.0d;

	public Set<Image> getImageCollection() {
		return imageSet;
	}

	public void setImageCollection(Set<Image> imageSet) {
		this.imageSet = imageSet;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getCollectionName() {
		return collectionName;
	}

	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}

	public String getCollectionDescription() {
		return collectionDescription;
	}

	public void setCollectionDescription(String collectionDescription) {
		this.collectionDescription = collectionDescription;
	}

}
