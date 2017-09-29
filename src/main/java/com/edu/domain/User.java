package com.edu.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.edu.domain.BaseEntity;

@Entity
@Table(name="user")
public class User  extends BaseEntity{
	private String openId;
	
	private String name;
	
	private int mobile;
	
	private String childName;
	
	private String childAge;
	
	private String address;
	
	public String getOpenId() {
		return openId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMobile() {
		return mobile;
	}

	public void setMobile(int mobile) {
		this.mobile = mobile;
	}

	public String getChildName() {
		return childName;
	}

	public void setChildName(String childName) {
		this.childName = childName;
	}

	public String getChildAge() {
		return childAge;
	}

	public void setChildAge(String childAge) {
		this.childAge = childAge;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
