package com.edu.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.edu.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.edu.domain.Image;

@Entity
@Table(name = "student")
public class Student extends BaseEntity {
	private String openCode;

	private String studentName;

	private String mobilePhone;

	private int age;

	private String address;

	private int classPeriod;

	@OneToMany(fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<Image> imagesSet;

	@ManyToMany(cascade = { CascadeType.MERGE }, fetch = FetchType.EAGER)
	@JsonIgnore
	@JoinTable(
		      name="STUDENT_COURSE",
		      joinColumns= @JoinColumn(name="STUDENT_ID", referencedColumnName="ID"),
		      inverseJoinColumns= @JoinColumn(name="COURSE_ID", referencedColumnName="ID"))
	private Set<Course> coursesSet;

	@ManyToMany(cascade = { CascadeType.MERGE }, fetch = FetchType.EAGER)
	@JsonIgnore
	@JoinTable(
		      name="RESERVED_STUDENT_RESERVED_COURSE",
		      joinColumns= @JoinColumn(name="RESERVED_STUDENT_ID", referencedColumnName="ID"),
		      inverseJoinColumns= @JoinColumn(name="RESERVED_COURSE_ID", referencedColumnName="ID"))
	private Set<Course> reservedCoursesSet;
	
	@ManyToMany(cascade = { CascadeType.MERGE }, fetch = FetchType.EAGER)
	@JsonIgnore
	@JoinTable(
		      name="STUDENT_NO_SIGN_COURSE",
		      joinColumns= @JoinColumn(name="STUDENT_ID", referencedColumnName="ID"),
		      inverseJoinColumns= @JoinColumn(name="NO_SIGN_COURSE_ID", referencedColumnName="ID"))
	private Set<Course> courseNotSignSet;
	
	@OneToOne(cascade = { CascadeType.ALL })
	private ProductCart cart;
	
	private boolean isChild;
	
	@OneToMany(cascade = { CascadeType.ALL }, mappedBy = "student")
	private Set<Order> orders;
	
	public Student(){
		this.cart = new ProductCart();
	}
	
	public Student(String openCode, String studentName, String mobilePhone, int age, String address, int classPeriod,
			ProductCart cart, boolean isChild) {
		this.openCode = openCode;
		this.studentName = studentName;
		this.mobilePhone = mobilePhone;
		this.age = age;
		this.address = address;
		this.classPeriod = classPeriod;
		this.cart = cart;
		this.isChild = isChild;
	}

	public String getOpenCode() {
		return openCode;
	}

	public void setOpenCode(String openCode) {
		this.openCode = openCode;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public ProductCart getCart() {
		return cart;
	}

	public void setCart(ProductCart cart) {
		this.cart = cart;
	}

	public boolean isChild() {
		return isChild;
	}

	public void setChild(boolean isChild) {
		this.isChild = isChild;
	}

	public void setImagesList(Set<Image> imagesSet) {
		this.imagesSet = imagesSet;
	}

	public void addImage(Image image) {
		this.imagesSet.add(image);
	}

	public Set<Course> getCoursesSet() {
		return coursesSet;
	}
	
	public void setCoursesSet(Set<Course> coursesSet) {
		this.coursesSet = coursesSet;
	}
	
	public void addCourse(Course course) {
		this.coursesSet.add(course);
	}

	public int getClassPeriod() {
		return classPeriod;
	}

	public void setClassPeriod(int classPeriod) {
		this.classPeriod = classPeriod;
	}

	public Set<Image> getImagesSet() {
		return imagesSet;
	}

	public void setImagesSet(Set<Image> imagesSet) {
		this.imagesSet = imagesSet;
	}

	
	public Set<Course> getReservedCoursesSet() {
		return reservedCoursesSet;
	}

	public void setReservedCoursesSet(Set<Course> reservedCoursesSet) {
		this.reservedCoursesSet = reservedCoursesSet;
	}
	
	public void addReservedCourse(Course reservedCourse) {
		this.reservedCoursesSet.add(reservedCourse);
	}

	public Set<Course> getCourseNotSignSet() {
		return courseNotSignSet;
	}

	public void setCourseNotSignSet(Set<Course> courseNotSignSet) {
		this.courseNotSignSet = courseNotSignSet;
	}

	public void addCourseNotSign(Course courseNotSign) {
		this.courseNotSignSet.add(courseNotSign);
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
	
	@Override
	public String toString() {
		String str = "user.id" + this.getId() + "/nuser.openCode:" + this.getOpenCode() + "/nuser.name" + this.getStudentName()
				+ "/nuser.phone" + this.getMobilePhone() + "/nuser.classperiod" + this.getClassPeriod();
		return str;
	}
}
