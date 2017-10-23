package com.edu.boot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.edu.controller.WxErrorController;
import com.edu.dao.CourseRepository;
import com.edu.dao.DerivedProductRepository;
import com.edu.dao.ImageCollectionRepository;
import com.edu.dao.ImageRepository;
import com.edu.dao.OrderRepository;
import com.edu.dao.ProductCartRepository;
import com.edu.dao.ProductCategoryRepository;
import com.edu.dao.ProductRepository;
import com.edu.dao.StudentRepository;
import com.edu.domain.Course;
import com.edu.domain.DerivedProduct;
import com.edu.domain.ImageCollection;
import com.edu.domain.Product;
import com.edu.domain.Image;
import com.edu.domain.ProductCategory;
import com.edu.domain.Student;

import antlr.collections.List;

@Component
// test data loader
public class DataLoader {
	private static final Logger logger = LoggerFactory.getLogger(WxErrorController.class);

	@Bean
	@Transactional
	public CommandLineRunner initialiseDataProviders(CourseRepository courseRepository,
			ProductCategoryRepository productCategoryRepository, ProductRepository productRepository,
			StudentRepository studentRepository, DerivedProductRepository derivedProductRepository,
			OrderRepository orderRepository, ProductCartRepository productCartRepository,
			ImageCollectionRepository imageCollectionRepository, ImageRepository imageRepository) {
		return (args) -> {
			ArrayList<Course> courses = new ArrayList<>();
			if (0 == courseRepository.count()) {
				LocalDate localDate = LocalDate.now();
				LocalTime timeFrom = LocalTime.of(9, 0);
				LocalTime timeTo = LocalTime.of(18, 0);
				LocalDateTime localDateTimeFrom = LocalDateTime.of(localDate, timeFrom);
				LocalDateTime localDateTimeTo = LocalDateTime.of(localDate, timeTo);
				ZoneId zone = ZoneId.systemDefault();
				Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
				Instant instantFrom = localDateTimeFrom.atZone(zone).toInstant();
				Instant instantTo = localDateTimeTo.atZone(zone).toInstant();
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
				for (int i = 0; i < 100; i++) {
					LocalDate date = localDate.plusDays(i);
					courses.add(courseRepository.save(new Course(date.toString(), date.toString(),
							timeFormat.format(Date.from(instantFrom)), timeFormat.format(Date.from(instantTo)))));
				}
			}
			ArrayList<Image> images = new ArrayList<>();
			if (0 == imageRepository.count()) {
				File imagefile = new File("../resource/star.jpg");
				OutputStream output = new FileOutputStream(imagefile);
		        BufferedOutputStream bufferedOutput = new BufferedOutputStream(output);
		        long fileSize = imagefile.length();  
		        byte[] buffer = new byte[(int) fileSize];  
		        bufferedOutput.write(buffer);
		        Image imageStar = new Image();
		        imageStar.setImageName("star");
		        imageStar.setContentType("JPEG");
		        imageStar.setData(buffer);
		        imageStar.setDate(courses.get(0).getDate());
		        images.add(imageStar);
		        imagefile = new File("../resource/galaxy.jpg");
				output = new FileOutputStream(imagefile);
		        bufferedOutput = new BufferedOutputStream(output);
		        fileSize = imagefile.length();  
		        buffer = new byte[(int) fileSize];  
		        bufferedOutput.write(buffer);
		        Image imageGalaxy = new Image();
		        imageGalaxy.setImageName("galaxy");
		        imageGalaxy.setContentType("JPEG");
		        imageGalaxy.setData(buffer);
		        imageGalaxy.setDate(courses.get(1).getDate());
		        imageGalaxy.setCourse(courses.get(0));
		        images.add(imageGalaxy);
			}
			ArrayList<ProductCategory> productCategories = new ArrayList<>();
			if (0 == productCategoryRepository.count()) {
				productCategories.add(productCategoryRepository.save(new ProductCategory("画布", "黑白画布")));
				productCategories.add(productCategoryRepository.save(new ProductCategory("衍生品", "创新衍生品")));
			}
			
			ArrayList<Product> products = new ArrayList<>();
			if (0 == productRepository.count()) {
				Product product = new Product();
				product.setProductName("星星画布");
				product.setProductCategory(productCategories.get(0));
				product.setProductPrice(520D);
				product.setProductDescription("画布");
				product.setDerivedProductFlag(false);
				HashSet<Image> imageSet = new HashSet<>();
				imageSet.add(images.get(0));
				product.setProductImages(imageSet);
				products.add(productRepository.save(product));
				Product derivedProduct = new Product();
				derivedProduct.setProductName("衍生品");
				product.setProductCategory(productCategories.get(0));
				product.setProductPrice(200d);
				product.setProductDescription("衍生品");
				product.setDerivedProductFlag(true);
				product.setProductImages(imageSet);
				products.add(productRepository.save(product));
			}
			ArrayList<DerivedProduct> derivedProducts = new ArrayList<>();
			if (0 == derivedProductRepository.count()) {
				DerivedProduct derivedProduct = new DerivedProduct();
				derivedProduct.setProduct(products.get(1));
				derivedProduct.setImage(images.get(0));
				derivedProducts.add(derivedProductRepository.save(derivedProduct));
			}
			
		};
	}
}
