package com.edu.domain;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "image")
public class Image extends BaseEntity {

	private String imageName;
	
	@Basic(fetch=FetchType.LAZY)
    @Column
    @NotNull
    @JsonIgnore
    @Lob
    private byte[] data;

    @Column
    @NotNull
    private String contentType;
    
    @Basic(fetch=FetchType.LAZY)
    @Column
    @JsonIgnore
    @Lob
    private byte[] thumbnail;
    
    private String date;
    
    public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
    
	public Image() {}
	
    public Image(byte[] data) {
        setData(data);
    }
    
    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
    }
}
