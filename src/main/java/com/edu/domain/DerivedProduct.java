package com.edu.domain;

import javax.persistence.*;

@Entity
@Table(name="derprod")
public class DerivedProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected long id;
    
    private String description;
    
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
