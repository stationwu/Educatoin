package com.edu.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.imageio.ImageIO;

import com.edu.config.ImageProperties;
import com.edu.domain.Course;
import com.edu.domain.Student;
import com.edu.storage.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.edu.dao.ImageRepository;
import com.edu.domain.Image;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ImageServiceImpl implements ImageService {
    //private static final Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ImageProperties imageProperties;

    @Autowired
    private FileStorageService fileStorageService;
    
    public ImageServiceImpl() {}
    
    public ImageServiceImpl(ImageRepository repo, ImageProperties imageProperties, FileStorageService fileStorageService) {
        this.imageRepository = repo;
        this.imageProperties = imageProperties;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public Image find(Long id) {
        return imageRepository.findOne(id);
    }

	@Override
    @Transactional
    public Image save(String imageName, String contentType, byte[] fileContent) {
        Image image = new Image();
        image.setImageName(imageName);
        image.setContentType(contentType);

        BufferedImage imageFull = readFromByteArray(fileContent);
        BufferedImage thumbnail = scale(imageFull,
                imageProperties.getThumbnailMaxWidth(), imageProperties.getThumbnailMaxHeight());

        String pathToFull = fileStorageService.store(imageFull);
        String pathToThumbnail = fileStorageService.store(thumbnail);
        image.setPath(pathToFull);
        image.setThumbnailPath(pathToThumbnail);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        //LocalDate localDate = LocalDate.now();
        image.setDate(LocalDateTime.now().format(formatter));
        return imageRepository.save(image);
    }

    @Transactional
    @Override
    public Image saveIn3Size(String imageName, Student student, Course course, MultipartFile file) {
        Image img = new Image();
        img.setImageName(imageName);
        img.setCourse(course);

        // Each file must save 3 versions - full, small, thumbnail
        BufferedImage imageFull = readFromUploaded(file);
        BufferedImage imageSmall = scale(imageFull,
                imageProperties.getSmallVersionMaxWidth(), imageProperties.getSmallVersionMaxHeight());
        BufferedImage imageThumbnail = scale(imageFull,
                imageProperties.getThumbnailMaxWidth(), imageProperties.getThumbnailMaxHeight());

        String pathToFull = fileStorageService.store(student.getId(), file);
        String pathToSmall = fileStorageService.store(student.getId(), imageSmall);
        String pathToThumbnail = fileStorageService.store(student.getId(), imageThumbnail);

        img.setPath(pathToFull);
        img.setSmallVersionPath(pathToSmall);
        img.setThumbnailPath(pathToThumbnail);

        img.setContentType(file.getContentType());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        //LocalDate localDate = LocalDate.now();
        img.setDate(LocalDateTime.now().format(formatter));
        return imageRepository.save(img);
    }
    
    @Transactional
    @Override
    public Image saveIn3Size(String imageName, String material,Student student, Course course, MultipartFile file) {
    	Image img = this.saveIn3Size(imageName, student, course, file);
    	img.setMaterial(material);
    	return imageRepository.save(img);
    }

    @Override
    public BufferedImage scale(BufferedImage image, int maxWidth, int maxHeight) {
        int width = image.getWidth();
        int height = image.getHeight();

        if(width <= maxWidth && height <= maxHeight) {
            return image; // No need to change
        }
        float ratioW = (float)width / (float)maxWidth;
        float ratioH = (float)height / (float)maxHeight;

        int scaledWidth, scaledHeight;

        if (ratioW > ratioH) {
            scaledWidth = maxWidth;
            scaledHeight = (int)(height / ratioW);
        } else {
            scaledWidth = (int)(width / ratioH);
            scaledHeight = maxHeight;
        }

        int imageType =  BufferedImage.TYPE_INT_RGB;
        BufferedImage scaledImage = new BufferedImage(scaledWidth, scaledHeight, imageType);
        Graphics2D g2d = scaledImage.createGraphics();

        g2d.setComposite(AlphaComposite.Src);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        //g2d.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
        //g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.drawImage(image, 0, 0, width, height, null);

        g2d.drawImage(image, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();

        return scaledImage;
    }

    private BufferedImage readFromByteArray(byte[] data) {
        try {
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(data));
            return image;
        } catch (IOException e) {
            throw new ImageFileReadingException("Failed to read image from bytes", e);
        }
    }

    private BufferedImage readFromUploaded(MultipartFile file) {
        try {
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(file.getBytes()));
            return image;
        } catch (IOException e) {
            throw new ImageFileReadingException("Failed to read image file " + file.getName(), e);
        }
    }

//    private static byte[] generateThumbnail(byte[] image)
//        throws IOException
//    {
//        BufferedImage original = ImageIO.read(new ByteArrayInputStream(image));
//        ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
//
//        //don't force jpg for thumbnails
//        int imageType =  BufferedImage.TYPE_INT_RGB;
//        BufferedImage scaledBI = new BufferedImage(100, 100, imageType);
//        Graphics2D g = scaledBI.createGraphics();
//
//        g.setComposite(AlphaComposite.Src);
//
//        g.drawImage(original, 0, 0, 100, 100, null);
//        g.dispose();
//
//        ImageIO.write(scaledBI, "jpg", out);
//        return out.toByteArray();
//    }

	@Override
	public Image save(String imageName, MultipartFile file) {
		// TODO Auto-generated method stub
		Image img = new Image();
        img.setImageName(imageName);
     // Each file must save 3 versions - full, small, thumbnail
        BufferedImage imageFull = readFromUploaded(file);
        BufferedImage imageSmall = scale(imageFull,
                imageProperties.getSmallVersionMaxWidth(), imageProperties.getSmallVersionMaxHeight());
        BufferedImage imageThumbnail = scale(imageFull,
                imageProperties.getThumbnailMaxWidth(), imageProperties.getThumbnailMaxHeight());
        
        String pathToFull = fileStorageService.store(imageFull);
        String pathToSmall = fileStorageService.store(imageSmall);
        String pathToThumbnail = fileStorageService.store(imageThumbnail);
        img.setPath(pathToFull);
        img.setSmallVersionPath(pathToSmall);
        img.setThumbnailPath(pathToThumbnail);

        img.setContentType(file.getContentType());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        img.setDate(LocalDateTime.now().format(formatter));
        
		return imageRepository.save(img);
	}
}
