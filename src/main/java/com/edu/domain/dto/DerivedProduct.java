package com.edu.domain.dto;

import javax.persistence.*;

import com.edu.domain.Image;
import com.edu.domain.Product;

@Entity
@Table(name="derprod")
public class DerivedProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected long id;
    @ManyToOne
	private Product product;
	
	@ManyToOne
	private Image image;

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
