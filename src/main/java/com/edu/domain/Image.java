package com.edu.domain;

import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "image")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String imageName;
	
	@Basic(fetch=FetchType.LAZY)
    @Column
    @NotNull
    @JsonIgnore
    @Lob
    private byte[] data;

    @Column
    @NotNull
    private String contentType;
    
    @Basic(fetch=FetchType.LAZY)
    @Column
    @JsonIgnore
    @Lob
    private byte[] thumbnail;
    
    private String date;
    
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
	
    public Image(byte[] data) {
        setData(data);
    }
    
    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
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
}
