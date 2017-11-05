package com.edu.domain;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String openCode;

    private String name;

    private String mobilePhone;
    
    private String address;

    @OneToMany(cascade = { CascadeType.ALL }, mappedBy = "customer")
    private Set<Student> students;

    @OneToOne(cascade = { CascadeType.ALL })
    private ProductCart cart;

    @OneToMany(cascade = { CascadeType.ALL }, mappedBy = "customer")
    private Set<Order> orders;

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    public Customer() {
        cart = new ProductCart();
    }

    public Customer(String openCode, String name, String mobilePhone) {
        this.openCode = openCode;
        this.name = name;
        this.mobilePhone = mobilePhone;
        this.cart = new ProductCart();
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
}
