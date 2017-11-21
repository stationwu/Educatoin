package com.edu.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "customer",
        indexes = {
                @Index(name = "idx_open_code", columnList = "openCode", unique = true),
                @Index(name = "idx_phone_number", columnList = "mobilePhone", unique = true)
        }
)
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    private String openCode;

    @NotNull
    private String name;

    @NotNull
    private String mobilePhone;

    @NotNull
    private String address;
    
    private boolean isActivated;

    @OneToMany(cascade = { CascadeType.ALL }, mappedBy = "customer")
    @JsonIgnore
    private Set<Student> students = new HashSet<>();

    @OneToOne(cascade = { CascadeType.ALL })
    @JsonIgnore
    private ProductCart cart;

    @OneToMany(cascade = { CascadeType.ALL }, mappedBy = "customer")
    @JsonIgnore
    private Set<Order> orders;

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    public Customer() {
        this.cart = new ProductCart();
        cart.setCustomer(this);
    }

    public Customer(String openCode, String name, String mobilePhone, String address) {
        this.openCode = openCode;
        this.name = name;
        this.mobilePhone = mobilePhone;
        this.isActivated = false;
        this.address = address;
        this.cart = new ProductCart();
        this.cart.setCustomer(this);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOpenCode() {
        return openCode;
    }

    public void setOpenCode(String openCode) {
        this.openCode = openCode;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public ProductCart getCart() {
        return cart;
    }

    public void setCart(ProductCart cart) {
        this.cart = cart;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    public void addOrder(Order order) {
        this.orders.add(order);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        String str = "Customer.id:" + this.getId() + "/nCustomer.openCode:" + this.getOpenCode() + "/nCustomer.name:" + this.getName()
                + "/nCustomer.phone:" + this.getMobilePhone();
        return str;
    }
    
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public void addStudent(Student student){
		students.add(student);
	}

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean isActivated) {
        this.isActivated = isActivated;
    }
}
