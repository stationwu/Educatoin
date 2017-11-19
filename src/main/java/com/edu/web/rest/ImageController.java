package com.edu.web.rest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.servlet.http.HttpServletResponse;

import com.edu.storage.FileStorageService;
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

	@Autowired
	public ImageController(ImageRepository imageRepository,
						   FileStorageService fileStorageService) {
		this.imageRepository = imageRepository;
		this.storageService = fileStorageService;
	}

	@RequestMapping(path = PATH + "/{id}", method = RequestMethod.GET)
	public void getImage(@PathVariable Long id, HttpServletResponse resp) throws IOException {
		Image img = imageRepository.findOne(id);

		resp.setContentType(img.getContentType());

		Resource resource = storageService.load(img.getPath());
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
