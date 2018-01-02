package com.edu.boot;

import com.edu.dao.*;
import com.edu.domain.*;
import com.edu.utils.ImageServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;

@Component
public class DataLoader {
    //private static final Logger logger = LoggerFactory.getLogger(WxErrorController.class);

    @Bean
    @Transactional
    public CommandLineRunner initialiseDataProviders(
        CourseRepository courseRepository,
        ProductCategoryRepository productCategoryRepository,
        ProductRepository productRepository,
        CustomerRepository customerRepository,
        StudentRepository studentRepository,
        DerivedProductRepository derivedProductRepository,
        OrderRepository orderRepository,
        ProductCartRepository productCartRepository,
        ImageCollectionRepository imageCollectionRepository,
        ImageRepository imageRepository,
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
                //Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
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
                //DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat timeFormat = new SimpleDateFormat("HH:mm");
                for (int i = 0; i < 10; i++) {
                    LocalDate date = localDate.plusDays(i);
                    courses.add(courseRepository.save(new Course(
                            date.toString() + " " + timeFormat.format(Date.from(instantFrom1)) + " "
                                    + timeFormat.format(Date.from(instantTo1)),
                            date.toString(), timeFormat.format(Date.from(instantFrom1)),
                            timeFormat.format(Date.from(instantTo1)), 10)));
                    courses.add(courseRepository.save(new Course(
                            date.toString() + " " + timeFormat.format(Date.from(instantFrom2)) + " "
                                    + timeFormat.format(Date.from(instantTo2)),
                            date.toString(), timeFormat.format(Date.from(instantFrom2)),
                            timeFormat.format(Date.from(instantTo2)), 10)));
                    courses.add(courseRepository.save(new Course(
                            date.toString() + " " + timeFormat.format(Date.from(instantFrom3)) + " "
                                    + timeFormat.format(Date.from(instantTo3)),
                            date.toString(), timeFormat.format(Date.from(instantFrom3)),
                            timeFormat.format(Date.from(instantTo3)), 10)));
                    courses.add(courseRepository.save(new Course(
                            date.toString() + " " + timeFormat.format(Date.from(instantFrom4)) + " "
                                    + timeFormat.format(Date.from(instantTo4)),
                            date.toString(), timeFormat.format(Date.from(instantFrom4)),
                            timeFormat.format(Date.from(instantTo4)), 10)));
                    courses.add(courseRepository.save(new Course(
                            date.toString() + " " + timeFormat.format(Date.from(instantFrom5)) + " "
                                    + timeFormat.format(Date.from(instantTo5)),
                            date.toString(), timeFormat.format(Date.from(instantFrom5)),
                            timeFormat.format(Date.from(instantTo5)), 10)));
                    courses.add(courseRepository.save(new Course(
                            date.toString() + " " + timeFormat.format(Date.from(instantFrom6)) + " "
                                    + timeFormat.format(Date.from(instantTo6)),
                            date.toString(), timeFormat.format(Date.from(instantFrom6)),
                            timeFormat.format(Date.from(instantTo6)), 10)));
                    courses.add(courseRepository.save(new Course(
                            date.toString() + " " + timeFormat.format(Date.from(instantFrom7)) + " "
                                    + timeFormat.format(Date.from(instantTo7)),
                            date.toString(), timeFormat.format(Date.from(instantFrom7)),
                            timeFormat.format(Date.from(instantTo7)), 10)));
                    courses.add(courseRepository.save(new Course(
                            date.toString() + " " + timeFormat.format(Date.from(instantFrom8)) + " "
                                    + timeFormat.format(Date.from(instantTo8)),
                            date.toString(), timeFormat.format(Date.from(instantFrom8)),
                            timeFormat.format(Date.from(instantTo8)), 10)));
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
                byte[] fileContent = byteBuffer.array();
                channel.close();
                fs.close();
                images.add(imageServiceImpl.save("star", "image/png", fileContent));

                File imagefile2 = new File(Paths.get(path, "image", "galaxy.png").toString());
                FileInputStream fs2 = new FileInputStream(imagefile2);
                FileChannel channel2 = fs2.getChannel();
                ByteBuffer byteBuffer2 = ByteBuffer.allocate((int) channel2.size());
                while ((channel2.read(byteBuffer2)) > 0) {
                    // do nothing
                    // System.out.println("reading");
                }
                byte[] fileContent2 = byteBuffer2.array();
                channel2.close();
                fs2.close();
                images.add(imageServiceImpl.save("galaxy", "image/png", fileContent2));
                
                File imagefile3 = new File(Paths.get(path, "image", "imageCollection.png").toString());
                FileInputStream fs3 = new FileInputStream(imagefile3);
                FileChannel channel3 = fs3.getChannel();
                ByteBuffer byteBuffer3 = ByteBuffer.allocate((int) channel3.size());
                while ((channel3.read(byteBuffer3)) > 0) {
                	// do nothing
                	// System.out.println("reading");
                }
                byte[] fileContent3 = byteBuffer3.array();
                fs3.close();
                images.add(imageServiceImpl.save("作品集", "image/png", fileContent3));
                
                File imagefile4 = new File(Paths.get(path, "image", "course.png").toString());
                FileInputStream fs4 = new FileInputStream(imagefile4);
                FileChannel channel4 = fs4.getChannel();
                ByteBuffer byteBuffer4 = ByteBuffer.allocate((int) channel4.size());
                while ((channel4.read(byteBuffer4)) > 0) {
                	// do nothing
                	// System.out.println("reading");
                }
                byte[] fileContent4 = byteBuffer4.array();
                fs4.close();
                images.add(imageServiceImpl.save("course", "image/png", fileContent4));
            }
            ArrayList<ProductCategory> productCategories = new ArrayList<>();
            if (0 == productCategoryRepository.count()) {
                productCategories.add(productCategoryRepository.save(new ProductCategory("画布", "黑白画布")));
                productCategories.add(productCategoryRepository.save(new ProductCategory("衍生品", "创新衍生品")));
                productCategories.add(productCategoryRepository.save(new ProductCategory("作品集", "作品集")));
                productCategories.add(productCategoryRepository.save(new ProductCategory("课程", "课程")));
            }

            ArrayList<Product> products = new ArrayList<>();
            if (0 == productRepository.count()) {
                Product product = new Product("星星画布", productCategories.get(0), 520d, "画布", false, false, false,0);
                Set<Image> imageSet = new HashSet<>();
                imageSet.add(images.get(0));
                product.setProductImages(imageSet);
                products.add(productRepository.save(product));
                Product derivedProduct = new Product("T恤衍生品", productCategories.get(1), 0.1d, "衍生品", true, false,false,0);
                imageSet.clear();
                imageSet.add(images.get(1));
                derivedProduct.setProductImages(imageSet);
                products.add(productRepository.save(derivedProduct));
                Product imageCollectionProduct = new Product("作品集", productCategories.get(2), 300d, "作品集",false,true,false,0);
                imageSet.clear();
                imageSet.add(images.get(2));
                imageCollectionProduct.setProductImages(imageSet);
                products.add(productRepository.save(imageCollectionProduct));
                
                Product classProduct = new Product("课程", productCategories.get(3), 2300d, "课程",false,false,true,24);
                imageSet.clear();
                imageSet.add(images.get(3));
                classProduct.setProductImages(imageSet);
                products.add(productRepository.save(classProduct));
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
                imageCollection.setProduct(products.get(2));
                imageCollection.setCollectionName("作品集");
                imageCollection.setCollectionDescription(imageList.size() + "幅作品");
                imageCollections.add(imageCollectionRepository.save(imageCollection));
            }

            List<Customer> customers = new ArrayList<>();
            if (0 == customerRepository.count()) {
                Customer customer = new Customer("123456", "Arthur", "13512345678", "中国上海");
                customer = customerRepository.save(customer);

                Student student = new Student("Arthur", "1987-03-02",24, 24, 0, false);
                Set<Image> imagesList = new HashSet<>();
                imagesList.add(images.get(1));
                student.setImagesSet(imagesList);
                student.setCustomer(customer);
                student = studentRepository.save(student);

                Student secondChild = new Student("Saber", "1990-05-02",24, 24, 0, false);
                Set<Image> imagesList2 = new HashSet<>();
                imagesList2.add(images.get(0));
                secondChild.setImagesSet(imagesList2);
                secondChild.setCustomer(customer);
                secondChild = studentRepository.save(secondChild);

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
                student = studentRepository.save(student);

                Set<Product> productSet = new HashSet<>();
                productSet.add(products.get(0));
                customer.getCart().setProducts(productSet);
                Set<DerivedProduct> derivedProductSet = new HashSet<>();
                derivedProductSet.add(derivedProducts.get(0));
                customer.getCart().setDerivedProducts(derivedProductSet);
                Set<ImageCollection> imageCollectionSet = new HashSet<>();
                imageCollectionSet.add(imageCollections.get(0));
                customer.getCart().setImageCollection(imageCollectionSet);
                customer= customerRepository.save(customer);
                customers.add(customer);
            }

            if (0 == orderRepository.count()) {
                Order order = new Order();
                Map<Product, Integer> productMap = new HashMap<>();
                productMap.put(products.get(0), 2);
                order.setProductsMap(productMap);
                Map<DerivedProduct, Integer> derivedProductMap = new HashMap<>();
                derivedProductMap.put(derivedProducts.get(0), 3);
                order.setDerivedProductsMap(derivedProductMap);
                Map<ImageCollection, Integer> imageCollectionMap = new HashMap<>();
                imageCollectionMap.put(imageCollections.get(0), 5);
                order.setImageCollectionMap(imageCollectionMap);
                order.setTotalAmount(1000d);
                Set<Order> orderSet = new HashSet<>();
                order.setCustomer(customers.get(0));
                order.setStatus(Order.Status.CREATED);
                orderSet.add(orderRepository.save(order));
            }
        };
    }
}
