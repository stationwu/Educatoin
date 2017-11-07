package com.edu.web.rest;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	public ImageController(ImageRepository imageRepository) {
		this.imageRepository = imageRepository;
	}

	@RequestMapping(path = PATH + "/{id}", method = RequestMethod.GET)
	public void getImage(@PathVariable Long id, HttpServletResponse resp) throws IOException {
		Image img = imageRepository.findOne(id);

		resp.setContentType(img.getContentType());
		resp.getOutputStream().write(img.getData());
	}

	@RequestMapping(path = PATH + "/{id}/thumbnail", method = RequestMethod.GET)
	public void getThumbnail(@PathVariable Long id, HttpServletResponse resp) throws IOException {
		Image img = imageRepository.findOne(id);

		resp.setContentType(img.getContentType());
		resp.getOutputStream().write(img.getThumbnail());
	}

}
