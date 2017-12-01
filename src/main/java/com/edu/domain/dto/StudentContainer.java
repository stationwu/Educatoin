package com.edu.domain.dto;

import com.edu.domain.Student;

import lombok.Data;

@Data
public class StudentContainer {
    private String id;

    private String studentName;

    private int classPeriod;

    private int leftPeriods;

    private int donePeriods;

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
        this.classPeriod = student.getClassPeriod();
        this.donePeriods = student.getDonePeriods();
        this.leftPeriods = student.getLeftPeriods();
        this.birthday = student.getBirthday();
        this.parentName = student.getCustomer().getName();
        this.mobilePhone = student.getCustomer().getMobilePhone();
        this.address = student.getCustomer().getAddress();
    }

    @Override
    public String toString() {
        return "StudentContainer [id=" + id + ", studentName=" + studentName
                + ", classPeriod=" + classPeriod + ", leftPeriods="
                + leftPeriods + ", donePeriods=" + donePeriods + ", birthday="
                + birthday + ", parentName=" + parentName + ", mobilePhone="
                + mobilePhone + ", address=" + address + "]";
    }

}
