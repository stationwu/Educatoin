package com.edu.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "image")
public class ImageProperties {
    private int thumbnailMaxWidth = 200;
    private int thumbnailMaxHeight = 200;
    private int smallVersionMaxWidth = 600;
    private int smallVersionMaxHeight = 600;

    public int getThumbnailMaxWidth() {
        return thumbnailMaxWidth;
    }

    public void setThumbnailMaxWidth(int thumbnailMaxWidth) {
        this.thumbnailMaxWidth = thumbnailMaxWidth;
    }

    public int getThumbnailMaxHeight() {
        return thumbnailMaxHeight;
    }

    public void setThumbnailMaxHeight(int thumbnailMaxHeight) {
        this.thumbnailMaxHeight = thumbnailMaxHeight;
    }

    public int getSmallVersionMaxWidth() {
        return smallVersionMaxWidth;
    }

    public void setSmallVersionMaxWidth(int smallVersionMaxWidth) {
        this.smallVersionMaxWidth = smallVersionMaxWidth;
    }

    public int getSmallVersionMaxHeight() {
        return smallVersionMaxHeight;
    }

    public void setSmallVersionMaxHeight(int smallVersionMaxHeight) {
        this.smallVersionMaxHeight = smallVersionMaxHeight;
    }
}
