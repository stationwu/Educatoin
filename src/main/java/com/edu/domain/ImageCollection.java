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
@Table(name="imagecollection")
public class ImageCollection extends BaseEntity{
	
	@OneToMany(fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<Image> imageSet;

	public Set<Image> getImageCollection() {
		return imageSet;
	}

	public void setImageCollection(Set<Image> imageSet) {
		this.imageSet = imageSet;
	}
	
}
