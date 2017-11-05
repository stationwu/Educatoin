package com.edu.domain;

import java.time.LocalDate;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

public class StudentContainer {
	private String id;

	private String studentName;

	private int age;

	private int classPeriod;
	
    private String birthday;  
    
    private String parentName;
	
    private String mobilePhone;
    
    private String address;
    
	public StudentContainer() {
	}
	
	public StudentContainer(Student student) {
		super();
		this.id = student.getId();
		this.studentName = student.getStudentName();
		this.age = student.getAge();
		this.classPeriod = student.getClassPeriod();
		this.birthday = student.getBirthday();
		this.parentName = student.getCustomer().getName();
		this.mobilePhone = student.getCustomer().getMobilePhone();
		this.address = student.getCustomer().getAddress();
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getClassPeriod() {
		return classPeriod;
	}

	public void setClassPeriod(int classPeriod) {
		this.classPeriod = classPeriod;
	}

	@Override
	public String toString() {
		String str = "Student.id" + this.getId() + "/nStudent.name" + this.getStudentName()
                + "/nStudent.classperiod" + this.getClassPeriod();
		return str;
	}

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
