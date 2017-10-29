package com.edu.boot;

import java.nio.file.Paths;
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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

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
import com.edu.domain.Order;
import com.edu.domain.Product;
import com.edu.domain.ProductCart;
import com.edu.domain.Image;
import com.edu.domain.ProductCategory;
import com.edu.domain.Student;
import com.edu.utils.ImageServiceImpl;

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
			ImageCollectionRepository imageCollectionRepository, ImageRepository imageRepository,
			ImageServiceImpl imageServiceImpl) {
		return (args) -> {
			ArrayList<Course> courses = new ArrayList<>();
			if (0 == courseRepository.count()) {
				LocalDate localDate = LocalDate.now();
				LocalTime timeFrom1 = LocalTime.of(9, 0);
				LocalTime timeTo1 = LocalTime.of(10, 0);
				LocalDateTime localDateTimeFrom1 = LocalDateTime.of(localDate, timeFrom1);
				LocalDateTime localDateTimeTo1 = LocalDateTime.of(localDate, timeTo1);
				LocalTime timeFrom2 = LocalTime.of(10, 0);
				LocalTime timeTo2 = LocalTime.of(11, 0);
				LocalDateTime localDateTimeFrom2 = LocalDateTime.of(localDate, timeFrom2);
				LocalDateTime localDateTimeTo2 = LocalDateTime.of(localDate, timeTo2);
				LocalTime timeFrom3 = LocalTime.of(11, 0);
				LocalTime timeTo3 = LocalTime.of(12, 0);
				LocalDateTime localDateTimeFrom3 = LocalDateTime.of(localDate, timeFrom3);
				LocalDateTime localDateTimeTo3 = LocalDateTime.of(localDate, timeTo3);
				LocalTime timeFrom4 = LocalTime.of(12, 0);
				LocalTime timeTo4 = LocalTime.of(13, 0);
				LocalDateTime localDateTimeFrom4 = LocalDateTime.of(localDate, timeFrom4);
				LocalDateTime localDateTimeTo4 = LocalDateTime.of(localDate, timeTo4);
				LocalTime timeFrom5 = LocalTime.of(13, 0);
				LocalTime timeTo5 = LocalTime.of(14, 0);
				LocalDateTime localDateTimeFrom5 = LocalDateTime.of(localDate, timeFrom5);
				LocalDateTime localDateTimeTo5 = LocalDateTime.of(localDate, timeTo5);
				LocalTime timeFrom6 = LocalTime.of(14, 0);
				LocalTime timeTo6 = LocalTime.of(15, 0);
				LocalDateTime localDateTimeFrom6 = LocalDateTime.of(localDate, timeFrom6);
				LocalDateTime localDateTimeTo6 = LocalDateTime.of(localDate, timeTo6);
				LocalTime timeFrom7 = LocalTime.of(15, 0);
				LocalTime timeTo7 = LocalTime.of(16, 0);
				LocalDateTime localDateTimeFrom7 = LocalDateTime.of(localDate, timeFrom7);
				LocalDateTime localDateTimeTo7 = LocalDateTime.of(localDate, timeTo7);
				LocalTime timeFrom8 = LocalTime.of(16, 0);
				LocalTime timeTo8 = LocalTime.of(17, 0);
				LocalDateTime localDateTimeFrom8 = LocalDateTime.of(localDate, timeFrom8);
				LocalDateTime localDateTimeTo8 = LocalDateTime.of(localDate, timeTo8);
				ZoneId zone = ZoneId.systemDefault();
				Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
				Instant instantFrom1 = localDateTimeFrom1.atZone(zone).toInstant();
				Instant instantTo1 = localDateTimeTo1.atZone(zone).toInstant();
				Instant instantFrom2 = localDateTimeFrom2.atZone(zone).toInstant();
				Instant instantTo2 = localDateTimeTo2.atZone(zone).toInstant();
				Instant instantFrom3 = localDateTimeFrom3.atZone(zone).toInstant();
				Instant instantTo3 = localDateTimeTo3.atZone(zone).toInstant();
				Instant instantFrom4 = localDateTimeFrom4.atZone(zone).toInstant();
				Instant instantTo4 = localDateTimeTo4.atZone(zone).toInstant();
				Instant instantFrom5 = localDateTimeFrom5.atZone(zone).toInstant();
				Instant instantTo5 = localDateTimeTo5.atZone(zone).toInstant();
				Instant instantFrom6 = localDateTimeFrom6.atZone(zone).toInstant();
				Instant instantTo6 = localDateTimeTo6.atZone(zone).toInstant();
				Instant instantFrom7 = localDateTimeFrom7.atZone(zone).toInstant();
				Instant instantTo7 = localDateTimeTo7.atZone(zone).toInstant();
				Instant instantFrom8 = localDateTimeFrom8.atZone(zone).toInstant();
				Instant instantTo8 = localDateTimeTo8.atZone(zone).toInstant();
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
				for (int i = 0; i < 10; i++) {
					LocalDate date = localDate.plusDays(i);
					courses.add(courseRepository.save(new Course(
							date.toString() + " " + timeFormat.format(Date.from(instantFrom1)) + " "
									+ timeFormat.format(Date.from(instantTo1)),
							date.toString(), timeFormat.format(Date.from(instantFrom1)),
							timeFormat.format(Date.from(instantTo1)))));
					courses.add(courseRepository.save(new Course(
							date.toString() + " " + timeFormat.format(Date.from(instantFrom2)) + " "
									+ timeFormat.format(Date.from(instantTo2)),
							date.toString(), timeFormat.format(Date.from(instantFrom2)),
							timeFormat.format(Date.from(instantTo2)))));
					courses.add(courseRepository.save(new Course(
							date.toString() + " " + timeFormat.format(Date.from(instantFrom3)) + " "
									+ timeFormat.format(Date.from(instantTo3)),
							date.toString(), timeFormat.format(Date.from(instantFrom3)),
							timeFormat.format(Date.from(instantTo3)))));
					courses.add(courseRepository.save(new Course(
							date.toString() + " " + timeFormat.format(Date.from(instantFrom4)) + " "
									+ timeFormat.format(Date.from(instantTo4)),
							date.toString(), timeFormat.format(Date.from(instantFrom4)),
							timeFormat.format(Date.from(instantTo4)))));
					courses.add(courseRepository.save(new Course(
							date.toString() + " " + timeFormat.format(Date.from(instantFrom5)) + " "
									+ timeFormat.format(Date.from(instantTo5)),
							date.toString(), timeFormat.format(Date.from(instantFrom5)),
							timeFormat.format(Date.from(instantTo5)))));
					courses.add(courseRepository.save(new Course(
							date.toString() + " " + timeFormat.format(Date.from(instantFrom6)) + " "
									+ timeFormat.format(Date.from(instantTo6)),
							date.toString(), timeFormat.format(Date.from(instantFrom6)),
							timeFormat.format(Date.from(instantTo6)))));
					courses.add(courseRepository.save(new Course(
							date.toString() + " " + timeFormat.format(Date.from(instantFrom7)) + " "
									+ timeFormat.format(Date.from(instantTo7)),
							date.toString(), timeFormat.format(Date.from(instantFrom7)),
							timeFormat.format(Date.from(instantTo7)))));
					courses.add(courseRepository.save(new Course(
							date.toString() + " " + timeFormat.format(Date.from(instantFrom8)) + " "
									+ timeFormat.format(Date.from(instantTo8)),
							date.toString(), timeFormat.format(Date.from(instantFrom8)),
							timeFormat.format(Date.from(instantTo8)))));
				}
			}
			ArrayList<Image> images = new ArrayList<>();
			if (0 == imageRepository.count()) {
				String path = new File(".").getCanonicalPath();
				File imagefile = new File(Paths.get(path, "image", "star.png").toString());
				FileInputStream fs = new FileInputStream(imagefile);
				FileChannel channel = fs.getChannel();
				ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
				while ((channel.read(byteBuffer)) > 0) {
					// do nothing
					// System.out.println("reading");
				}
				Image imageStar = new Image();
				imageStar.setImageName("star");
				imageStar.setContentType("image/png");
				imageStar.setData(byteBuffer.array());
				channel.close();
				fs.close();

				imageStar.setDate(courses.get(0).getDate());
				images.add(imageServiceImpl.save(imageStar));
				File imagefile2 = new File(Paths.get(path, "image", "galaxy.png").toString());
				FileInputStream fs2 = new FileInputStream(imagefile2);
				FileChannel channel2 = fs2.getChannel();
				ByteBuffer byteBuffer2 = ByteBuffer.allocate((int) channel2.size());
				while ((channel2.read(byteBuffer2)) > 0) {
					// do nothing
					// System.out.println("reading");
				}
				Image imageGalaxy = new Image();
				imageGalaxy.setImageName("galaxy");
				imageGalaxy.setContentType("image/png");
				imageGalaxy.setData(byteBuffer2.array());
				channel2.close();
				fs2.close();
				imageGalaxy.setDate(courses.get(1).getDate());
				imageGalaxy.setCourse(courses.get(0));
				images.add(imageServiceImpl.save(imageGalaxy));
			}
			ArrayList<ProductCategory> productCategories = new ArrayList<>();
			if (0 == productCategoryRepository.count()) {
				productCategories.add(productCategoryRepository.save(new ProductCategory("画布", "黑白画布")));
				productCategories.add(productCategoryRepository.save(new ProductCategory("衍生品", "创新衍生品")));
			}

			ArrayList<Product> products = new ArrayList<>();
			if (0 == productRepository.count()) {
				Product product = new Product("星星画布", productCategories.get(0), 520d, "画布", false);
				Set<Image> imageSet = new HashSet<>();
				imageSet.add(images.get(0));
				product.setProductImages(imageSet);
				products.add(productRepository.save(product));
				Product derivedProduct = new Product("T恤衍生品", productCategories.get(0), 200d, "衍生品", true);
				imageSet.clear();
				imageSet.add(images.get(1));
				derivedProduct.setProductImages(imageSet);
				products.add(productRepository.save(derivedProduct));
			}
			ArrayList<DerivedProduct> derivedProducts = new ArrayList<>();
			if (0 == derivedProductRepository.count()) {
				DerivedProduct derivedProduct = new DerivedProduct();
				derivedProduct.setProduct(products.get(1));
				derivedProduct.setImage(images.get(0));
				derivedProducts.add(derivedProductRepository.save(derivedProduct));
			}
			ArrayList<ImageCollection> imageCollections = new ArrayList<>();
			if (0 == imageCollectionRepository.count()) {
				ImageCollection imageCollection = new ImageCollection();
				Set<Image> imageList = new HashSet<>();
				imageList.add(images.get(1));
				imageCollection.setImageCollection(imageList);
				imageCollection.setPrice(200d);
				imageCollection.setCollectionName("作品集");
				imageCollection.setCollectionDescription(imageList.size()+"幅作品");
				imageCollections.add(imageCollectionRepository.save(imageCollection));
			}
			ArrayList<Student> students = new ArrayList<>();
			if (0 == studentRepository.count()) {
				Student student = new Student("123456", "Arthur", "13585813816", 30, "XXX路", 24, new ProductCart(),false);
				Set<Image> imagesList = new HashSet<>();
				imagesList.add(images.get(1));
				student.setImagesSet(imagesList);
				studentRepository.save(student);
				Set<Course> courseList = new HashSet<>();
				courseList.add(courses.get(0));
				courseList.add(courses.get(1));
				courseList.add(courses.get(2));
				Set<Course> reservedCourseList = new HashSet<>();
				reservedCourseList.add(courses.get(10));
				reservedCourseList.add(courses.get(11));
				reservedCourseList.add(courses.get(12));
				Set<Course> courseNotSignList = new HashSet<>();
				courseNotSignList.add(courses.get(3));
				student.setCoursesSet(courseList);
				student.setReservedCoursesSet(reservedCourseList);
				student.setCourseNotSignSet(courseNotSignList);
				Student student1 = studentRepository.save(student);
				Set<Product> productSet = new HashSet<>();
				productSet.add(products.get(0));
				student1.getCart().setProducts(productSet);
				Set<DerivedProduct> derivedProductSet = new HashSet<>();
				derivedProductSet.add(derivedProducts.get(0));
				student1.getCart().setDerivedProducts(derivedProductSet);
				Set<ImageCollection> imageCollectionSet = new HashSet<>();
				imageCollectionSet.add(imageCollections.get(0));
				student1.getCart().setImageCollection(imageCollectionSet);
				Student student2 = studentRepository.save(student1);
				students.add(student2);
			}
			if (0 == orderRepository.count()) {
				Order order = new Order();
				Map<Product, Integer> productMap = new HashMap<>();
				productMap.put( products.get(0),2);
				order.setProductsMap(productMap);
				Map<DerivedProduct, Integer> derivedProductMap = new HashMap<>();
				derivedProductMap.put(derivedProducts.get(0), 3);
				order.setDerivedProductsMap(derivedProductMap);
				Map<ImageCollection, Integer> imageCollectionMap = new HashMap<>();
				imageCollectionMap.put(imageCollections.get(0),5);
				order.setImageCollectionMap(imageCollectionMap);
				order.setTotalAmount(1000d);
				Set<Order> orderSet = new HashSet<>();
				order.setStudent(students.get(0));
				orderSet.add(orderRepository.save(order));
			}
		};
	}
}
