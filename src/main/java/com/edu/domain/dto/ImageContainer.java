package com.edu.domain.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.edu.domain.Course;
import com.edu.domain.Image;
import com.edu.domain.Student;

import lombok.Data;

@Data
public class ImageContainer {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private long id;

    private String imageName;

    private String date;

    private Course course = new Course();

    private String imageUrl;

    private String thumbnailUrl;

    private String createdBy;

    private String material;

    public ImageContainer(long id, String imageName, String date, Course course,
            String imageUrl, String thumbnailUrl) {
        super();
        this.id = id;
        this.imageName = imageName;
        this.date = date;
        this.course = course;
        this.imageUrl = imageUrl;
        this.thumbnailUrl = thumbnailUrl;
    }

    public ImageContainer(Image image) {
        super();
        this.id = image.getId();
        this.imageName = image.getImageName();
        this.date = image.getDate();
        this.course = image.getCourse();
        this.material = image.getMaterial();
        this.imageUrl = "/Images/" + image.getId();
        this.thumbnailUrl = "/Images/" + image.getId() + "/thumbnail";
    }

    public ImageContainer(Image x, Student student) {
        this(x);
        this.createdBy = student.getStudentName();
    }
}
