package com.edu.utils;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.edu.dao.ImageRepository;
import com.edu.domain.Image;

@Component
public class ImageServiceImpl implements ImageService{
    private static final Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);

    @Autowired
    private ImageRepository imageRepository;
    
    public ImageServiceImpl() {}
    
    public ImageServiceImpl(ImageRepository repo) {
        this.imageRepository = repo;
    }

    public Image find(Long id) {
        return imageRepository.findOne(id);
    }

	@Transactional
    public Image save(Image image) {

        try {
            image.setThumbnail(generateThumbnail(image.getData()));
        } catch (IOException e) {
            logger.error("Failed to generate thumbnail", e);
            throw new RuntimeException("Failed to generate thumbnail");
        }
        return imageRepository.save(image);
    }

    private static byte[] generateThumbnail(byte[] image)
        throws IOException
    {
        BufferedImage original = ImageIO.read(new ByteArrayInputStream(image));
        ByteArrayOutputStream out = new ByteArrayOutputStream(1000);

        //don't force jpg for thumbnails
        int imageType =  BufferedImage.TYPE_INT_RGB;
        BufferedImage scaledBI = new BufferedImage(100, 100, imageType);
        Graphics2D g = scaledBI.createGraphics();

        g.setComposite(AlphaComposite.Src);
        
        g.drawImage(original, 0, 0, 100, 100, null); 
        g.dispose();
        
        ImageIO.write(scaledBI, "jpg", out);
        return out.toByteArray();
    }

}
