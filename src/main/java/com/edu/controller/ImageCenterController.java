package com.edu.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.edu.utils.WxUserOAuthHelper;
import me.chanjar.weixin.common.exception.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.edu.dao.ImageCollectionRepository;
import com.edu.dao.ImageRepository;
import com.edu.dao.StudentRepository;
import com.edu.domain.Image;
import com.edu.domain.ImageCollection;
import com.edu.domain.ImageContainer;
import com.edu.domain.Student;
import me.chanjar.weixin.mp.api.WxMpService;

@Controller
public class ImageCenterController {
	@Autowired
	private WxMpService wxMpService;

	@Autowired
	private StudentRepository repository;
	
	@Autowired
	private ImageRepository imageRepository;
	
	@Autowired
	private ImageCollectionRepository imageCollectionRepository;

    @Autowired
    private WxUserOAuthHelper oauthHelper;

    public final static String SESSION_OPENID_KEY = "openCode";

    public final static String IMAGE_COLLECTION_PATH = "/user/imagecollection";
    public final static String IMAGE_COLLECTION_CALLBACK_PATH = "/user/imagecollection/cb";

	@GetMapping("/user/image")
	public String getImages(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		Object openCodeObject = session.getAttribute("openCode");

		if (null == openCodeObject) {
			return "error_500";
		}

		String authCode = openCodeObject.toString();
		Student student = repository.findOneByOpenCode(authCode);
		if (student == null) {
			Student newStudent = new Student();
			newStudent.setOpenCode(authCode);
			model.addAttribute("student", newStudent);
			return "user_signup";
		} else {
			Set<Image> images = student.getImagesSet();
			ArrayList<ImageContainer> imagesContainer = images.stream()
					.sorted((x,y) -> y.getDate().compareTo(x.getDate()))
					.map(x -> new ImageContainer(x.getId(), x.getImageName(), x.getDate(), x.getCourse(), "/Images/"+x.getId(), "/Images/"+x.getId()+"/thumbnail"))
					.collect(Collectors.toCollection(ArrayList::new));

			model.addAttribute("images", imagesContainer);
			return "user_images";
		}
	}

	@GetMapping(IMAGE_COLLECTION_PATH)
    public String imageCollection(HttpServletRequest request, HttpSession session, Model model) {
        Object openIdInSession = session.getAttribute(SESSION_OPENID_KEY);

        if (openIdInSession == null) { // OAuth to get OpenID
            return oauthHelper.buildOAuth2RedirectURL(request, IMAGE_COLLECTION_PATH, IMAGE_COLLECTION_CALLBACK_PATH);
        } else {
            return doShowImageCollection((String) openIdInSession, model);
        }
    }

    @GetMapping(IMAGE_COLLECTION_CALLBACK_PATH)
    public String imageCollectionCallback(@RequestParam(value="code") String authCode, Model model, HttpSession session) {
        String openId = null;

        try {
            openId = oauthHelper.getOpenIdWhenOAuth2CalledBack(authCode, session);
        } catch (WxErrorException e) {
            e.printStackTrace();
            return "error_500";
        }

        return doShowImageCollection(openId, model);
    }

	private String doShowImageCollection(String openId, Model model) {
        if (openId == null) {
            return "error_500";
        }

		Student student = repository.findOneByOpenCode(openId);
		if (student == null) {
			Student newStudent = new Student();
			newStudent.setOpenCode(openId);
			model.addAttribute("student", newStudent);
			return "user_signup";
		} else {
			Set<Image> images = student.getImagesSet();
			ArrayList<ImageContainer> imagesContainer = images.stream()
					.sorted((x,y) -> y.getDate().compareTo(x.getDate()))
					.map(x -> new ImageContainer(x.getId() ,x.getImageName(), x.getDate(), x.getCourse(), "/Images/"+x.getId(), "/Images/"+x.getId()+"/thumbnail"))
					.collect(Collectors.toCollection(ArrayList::new));

			model.addAttribute("images", imagesContainer);
			model.addAttribute("code", openId);
			return "user_imagecollection";
		}
	}
	
	@PostMapping("/user/generateImagecollection")
	@ResponseBody
	public String createImageCollection(HttpServletRequest request, @RequestParam(value = "images") String images, Model model) {
		HttpSession session = request.getSession();
		Object openCodeObject = session.getAttribute("openCode");

		if (null == openCodeObject) {
			return "error_500";
		}

		String authCode = openCodeObject.toString();
		Student student = repository.findOneByOpenCode(authCode);
		ImageCollection imageCollection = new ImageCollection();
		List<String> imagesWithId = Arrays.asList(images.split(","));
		Set<Image> imageList = new HashSet<>();
		for(String id : imagesWithId){
			imageList.add(imageRepository.findOne(Long.parseLong(id)));
		}
		
		imageCollection.setImageCollection(imageList);
		imageCollection.setPrice(200d);
		imageCollection.setCollectionName("作品集");
		imageCollection.setCollectionDescription(imageList.size()+"幅作品");
		ImageCollection entity = imageCollectionRepository.save(imageCollection);
		student.getCart().addImageCollection(entity);
		repository.save(student);
		return "请到购物车查看生成的作品集";
	}
}
