package com.edu.domain.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.edu.domain.Course;

public class ImageContainer {
	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
	private long id;
	
	private String imageName;
    
    private String date;
    
    private Course course = new Course();
    
    private String imageUrl;
    
    private String thumbnailUrl;

	public ImageContainer(long id,String imageName, String date, Course course, String imageUrl, String thumbnailUrl) {
		super();
		this.id = id;
		this.imageName = imageName;
		this.date = date;
		this.course = course;
		this.imageUrl = imageUrl;
		this.thumbnailUrl = thumbnailUrl;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
