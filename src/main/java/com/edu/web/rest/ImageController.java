package com.edu.web.rest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.edu.dao.CustomerRepository;
import com.edu.errorhandler.ResourceNotFoundException;
import com.edu.storage.FileStorageService;
import com.edu.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.edu.dao.ImageRepository;
import com.edu.domain.Image;

@RestController
@ExposesResourceFor(Image.class)
public class ImageController {
	public static final String PATH = "/Images";
	private final ImageRepository imageRepository;
	private final FileStorageService storageService;
	private final CustomerRepository customerRepository;

	@Autowired
	public ImageController(ImageRepository imageRepository,
						   FileStorageService fileStorageService,
						   CustomerRepository customerRepository) {
		this.imageRepository = imageRepository;
		this.storageService = fileStorageService;
		this.customerRepository = customerRepository;
	}

	@RequestMapping(path = PATH + "/{id}", method = RequestMethod.GET)
	public void getImage(@PathVariable Long id, HttpServletResponse resp, HttpSession session) throws IOException {
		Image img = imageRepository.findOne(id);

		if (img == null) {
			throw new ResourceNotFoundException("Image " + id + " does not exist");
		}

		// Interceptor already checked it's not null
		String openId = (String)session.getAttribute(Constant.SESSION_OPENID_KEY);
		String path = null;
		if (customerRepository.findOneByOpenCode(openId) != null) { // If it's customer, return the small size picture
			path = img.getSmallVersionPath();
		} else { // If it's admin user, return the full size picture
			path = img.getPath();
		}

		resp.setContentType(img.getContentType());

		Resource resource = storageService.load(path);
		File file = resource.getFile();
		byte[] content = Files.readAllBytes(file.toPath());

		resp.getOutputStream().write(content);
	}

	@RequestMapping(path = PATH + "/{id}/thumbnail", method = RequestMethod.GET)
	public void getThumbnail(@PathVariable Long id, HttpServletResponse resp) throws IOException {
		Image img = imageRepository.findOne(id);

		resp.setContentType(img.getContentType());

		Resource resource = storageService.load(img.getThumbnailPath());
		File file = resource.getFile();
		byte[] content = Files.readAllBytes(file.toPath());

		resp.getOutputStream().write(content);
	}

}
