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
    
    @ManyToOne
	private Product product;

	public Set<Image> getImageCollection() {
		return imageSet;
	}

	public void setImageCollection(Set<Image> imageSet) {
		this.imageSet = imageSet;
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
    
    public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ImageCollection)) return false;

        ImageCollection that = (ImageCollection) o;

        return getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return (int) (getId() ^ (getId() >>> 32));
    }
}
