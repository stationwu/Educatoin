package com.edu.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import com.edu.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.edu.domain.Image;

@Entity
@Table(name = "student")
public class Student extends BaseEntity {
	private String openCode;

	private String studentName;

	@NotNull
	@Digits(integer = 11, fraction = 0)
	private int mobilePhone;

	private int age;

	private String address;

	private int classPeriod;

	@OneToMany(fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<Image> imagesSet;

	@ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.EAGER)
	@JsonIgnore
	@JoinTable(
		      name="STUDENT_COURSE",
		      joinColumns= @JoinColumn(name="STUDENT_ID", referencedColumnName="ID"),
		      inverseJoinColumns= @JoinColumn(name="COURSE_ID", referencedColumnName="ID"))
	private Set<Course> coursesSet;

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

	public int getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(int mobilePhone) {
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

	public Set<Image> getImagesSet() {
		return imagesSet;
	}

	public void addImage(Image image) {
		this.imagesSet.add(image);
	}

	public Set<Course> getCoursesSet() {
		return coursesSet;
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

	public String toString() {
		String str = "user.id" + this.getId() + "/nuser.openCode:" + this.getOpenCode() + "/nuser.name" + this.getStudentName()
				+ "/nuser.phone" + this.getMobilePhone() + "/nuser.classperiod" + this.getClassPeriod();
		return str;
	}
}
