package com.edu.utils;

import com.edu.domain.Image;

public interface ImageService {
    public Image find(Long id);

    public Image save(Image image);

}