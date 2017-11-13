package com.edu.view;

import java.util.ArrayList;
import java.util.List;

public class CustomerSignUpForm {
    private String openCode;

    private String name;

    private String mobilePhone;

    private String address;

    private List<ChildForm> children = new ArrayList<>();

    public static class ChildForm {
        private String childName;

        private int classPeriod;

        private String birthday;

        public String getChildName() {
            return childName;
        }

        public void setChildName(String childName) {
            this.childName = childName;
        }

        public int getClassPeriod() {
            return classPeriod;
        }

        public void setClassPeriod(int classPeriod) {
            this.classPeriod = classPeriod;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }
    }

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

    public List<ChildForm> getChildren() {
        return children;
    }

    public void setChildren(List<ChildForm> children) {
        this.children = children;
    }

    public void setChild(int index, ChildForm child) {
        this.children.set(index, child);
    }

    public void addChild(ChildForm child) {
        this.children.add(child);
    }
}
