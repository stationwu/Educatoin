package com.edu.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="imagecollection")
public class ImageCollection {
	
	@OneToMany(mappedBy = "imageCollection", orphanRemoval = true)
	@JsonIgnore
	private List<Image> imageCollection;

	public List<Image> getImageCollection() {
		return imageCollection;
	}

	public void setImageCollection(List<Image> imageCollection) {
		this.imageCollection = imageCollection;
	}
	
}
