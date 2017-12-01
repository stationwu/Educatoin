package com.edu.controller;

import com.edu.dao.CustomerRepository;
import com.edu.dao.ImageCollectionRepository;
import com.edu.dao.ImageRepository;
import com.edu.dao.ProductRepository;
import com.edu.dao.StudentRepository;
import com.edu.domain.*;
import com.edu.domain.dto.ImageContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class ImageCenterController {
	@Autowired
	private CustomerRepository custRepo;

	@Autowired
	private ImageRepository imageRepository;

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private ImageCollectionRepository imageCollectionRepository;

	public final static String SESSION_OPENID_KEY = "openCode";

	public final static String IMAGE_COLLECTION_PATH = "/user/imagecollection";
	
	public final static String IMAGE_PATH = "/user/image";
	
	public final static String IMAGE_DEATIL_PATH = "/user/imagedetail";
	
	public final static String MANAGER_IMAGE_LIST_PATH = "/manager/image";
	
	public final static String MANAGER_IMAGE_SEARCH_PATH = "/manager/searchimage";
	
	public final static String MANAGER_DELETE_IMAGE_PATH = "/manager/deleteimage";
	
	public final static String CREATE_IMAGECOLLECTION_PATH = "/user/generateImagecollection";

	@GetMapping(IMAGE_PATH)
	@ResponseBody
	public List<ImageContainer> getImages(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		Object openCodeObject = session.getAttribute("openCode");

		String openId = openCodeObject.toString();
		Customer customer = custRepo.findOneByOpenCode(openId);
		Set<Student> students = customer.getStudents();
		model.addAttribute("students", students);

		/**
		 * TODO: Below loop is wrong. We must show also the students list.
		 * So view also needs change.
		 */
		ArrayList<ImageContainer> imageList = new ArrayList<>();
		for (Student student : customer.getStudents()) {
			Set<Image> images = student.getImagesSet();
			ArrayList<ImageContainer> imagesContainer = images.stream()
					.sorted((x, y) -> (int)(y.getId()-x.getId()))
					.map(x -> new ImageContainer(x,student))
					.collect(Collectors.toCollection(ArrayList::new));

			imageList.addAll(imagesContainer);
		}
		return imageList.stream().sorted((x,y) -> (int)(y.getId() - x.getId())).collect(Collectors.toCollection(ArrayList::new));
	}
	
	@GetMapping(IMAGE_DEATIL_PATH)
	public String getImageDetail(@RequestParam(value="id") String id, Model model) {
		Image image = imageRepository.findOne(Long.parseLong(id));
		ImageContainer imageContainer = new ImageContainer(image);
		model.addAttribute("image", imageContainer);
		return "user_image";		
	}

	@GetMapping(IMAGE_COLLECTION_PATH)
	private String doShowImageCollection(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		Object openCodeObject = session.getAttribute("openCode");
		String openId = openCodeObject.toString();

		Customer customer = custRepo.findOneByOpenCode(openId);
		for (Student student : customer.getStudents()) {
			Set<Image> images = student.getImagesSet();
			ArrayList<ImageContainer> imagesContainer = images.stream()
					.sorted((x, y) -> (int)(y.getId() - x.getId()))
					.map(x -> new ImageContainer(x))
					.collect(Collectors.toCollection(ArrayList::new));
	
			model.addAttribute("images", imagesContainer);
			model.addAttribute("code", openId);
		}
		return "derivation";
	}
	
	@GetMapping(MANAGER_IMAGE_SEARCH_PATH)
	private String deleteImage(Model model) {
		model.addAttribute("type", "2");
		model.addAttribute("title", "删除用户图片");
		return "user_search";
	}
	
	@GetMapping(MANAGER_DELETE_IMAGE_PATH)
	private String showImageList(@RequestParam(value="id") String id, Model model) {
		Student student = studentRepository.findOne(id);
		ArrayList<ImageContainer> imagesContainer = student.getImagesSet().stream()
				.sorted((x, y) -> (int)(y.getId()-x.getId()))
				.map(x -> new ImageContainer(x,student))
				.collect(Collectors.toCollection(ArrayList::new));
    	model.addAttribute("images", imagesContainer);
    	model.addAttribute("code", id);
    	return "user_deleteimage";
	}
	
	@PostMapping(MANAGER_DELETE_IMAGE_PATH)
	@ResponseBody
	private String deleteImage(@RequestParam(value="id") String id, @RequestParam(value="imageid") String imageid, Model model) {
		Student student = studentRepository.findOne(id);
		student.removeImage(imageRepository.findOne(Long.parseLong(imageid)));
		studentRepository.save(student);
    	return "user_deleteimage";
	}

	
	@GetMapping(MANAGER_IMAGE_LIST_PATH)
	@ResponseBody
	public List<ImageContainer> getImageList(@RequestParam(value="id") String id, HttpServletRequest request, Model model) {
		Student student = studentRepository.findOne(id);
		Set<Image> images = student.getImagesSet();
		ArrayList<ImageContainer> imagesContainer = images.stream()
				.sorted((x, y) -> (int)(y.getId()-x.getId()))
				.map(x -> new ImageContainer(x,student))
				.collect(Collectors.toCollection(ArrayList::new));


		return imagesContainer;
	}

	@PostMapping(CREATE_IMAGECOLLECTION_PATH)
	@ResponseBody
	public String createImageCollection(HttpServletRequest request, @RequestParam(value = "images") String images,
			Model model) {
		HttpSession session = request.getSession();
		Object openCodeObject = session.getAttribute("openCode");

		String openId = openCodeObject.toString();
		Customer customer = custRepo.findOneByOpenCode(openId);
		ImageCollection imageCollection = new ImageCollection();
		List<String> imagesWithId = Arrays.asList(images.split(","));
		List<Product> productList = productRepository.getImageCollectionProductList();
		Product product = productList.get(0);
		if(imagesWithId.size()!=product.getNumberOfPic()){
			return "请选择"+product.getNumberOfPic()+"张作品！";
		}
		Set<Image> imageList = new HashSet<>();
		for (String id : imagesWithId) {
			imageList.add(imageRepository.findOne(Long.parseLong(id)));
		}

		imageCollection.setImageCollection(imageList);
		List<Product> products = productRepository.getImageCollectionProductList();
		imageCollection.setProduct(products.get(0));
		imageCollection.setCollectionName("作品集");
		String nameList="";
		for(Image image:imageList){
			nameList+=image.getImageName()+",";
		}
		nameList = nameList.substring(0, nameList.length()-1);
		imageCollection.setCollectionDescription(imageList.size() + "幅作品:"+nameList);
		ImageCollection entity = imageCollectionRepository.save(imageCollection);
		customer.getCart().addImageCollection(entity);
		custRepo.save(customer);
		return "请到购物车查看生成的作品集";
	}
}
