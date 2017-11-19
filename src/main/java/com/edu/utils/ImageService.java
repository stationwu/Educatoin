package com.edu.utils;

import com.edu.domain.Course;
import com.edu.domain.Image;
import com.edu.domain.Student;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;

public interface ImageService {
    public Image find(Long id);

    public Image save(Image image);

    public Image saveIn3Size(String imageName, Student student, Course course, MultipartFile file);

    public BufferedImage scale(final BufferedImage image, int maxWidth, int maxHeight);

    public BufferedImage readFromUploaded(MultipartFile file);
}