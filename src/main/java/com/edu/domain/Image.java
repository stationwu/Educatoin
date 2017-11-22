package com.edu.domain;

import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author I067768
 *
 */
@Entity
@Table(name = "image")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String imageName;

    @Column
    private String path;

    @Column
    private String thumbnailPath;

    @Column
    private String smallVersionPath;

    @Column
    @NotNull
    private String contentType;
    
    private String date;
    
    private String material;
    
    @ManyToOne
    @JoinColumn(name="COURESE_ID")
    private Course course;
    
	@ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.EAGER)
	@JsonIgnore
	@JoinTable(
		      name="IMAGE_IMAGECOLLECTION",
		      joinColumns= @JoinColumn(name="IMAGE_ID", referencedColumnName="ID"),
		      inverseJoinColumns= @JoinColumn(name="IMAGECOLLECTION_ID", referencedColumnName="ID"))
	private Set<ImageCollection> imageCollections;
    
    public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
    
	public Image() {}

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

	public Set<ImageCollection> getImageCollections() {
		return imageCollections;
	}

	public void setImageCollections(Set<ImageCollection> imageCollections) {
		this.imageCollections = imageCollections;
	}

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public String getSmallVersionPath() {
        return smallVersionPath;
    }

    public void setSmallVersionPath(String smallVersionPath) {
        this.smallVersionPath = smallVersionPath;
    }

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}
    
}
