package com.edu.domain.dto;

import java.util.ArrayList;
import java.util.List;

public class CustomerContainer {
    private String openCode;

    private String name;

    private String mobilePhone;

    private String address;

    private List<ChildContainer> children = new ArrayList<>();

    public String getOpenCode() {
        return openCode;
    }

    public void setOpenCode(String openCode) {
        this.openCode = openCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<ChildContainer> getChildren() {
        return children;
    }

    public void setChildren(List<ChildContainer> children) {
        this.children = children;
    }

    public void setChild(int index, ChildContainer child) {
        this.children.set(index, child);
    }

    public void addChild(ChildContainer child) {
        this.children.add(child);
    }
}
