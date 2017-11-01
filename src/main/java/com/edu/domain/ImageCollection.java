package com.edu.domain;

import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "imagecollection")
public class ImageCollection {
	
	public String collectionName;
	
	public String collectionDescription;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToMany(cascade = { CascadeType.MERGE }, fetch = FetchType.EAGER)
	@JsonIgnore
	@JoinTable(
		      name="IMAGE_IMAGECOLLECTION",
		      joinColumns= @JoinColumn(name="IMAGECOLLECTION_ID", referencedColumnName="ID"),
		      inverseJoinColumns= @JoinColumn(name="IMAGE_ID", referencedColumnName="ID"))
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

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
