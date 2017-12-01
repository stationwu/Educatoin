package com.edu.domain.dto;

import com.edu.domain.Course;

import lombok.Data;

@Data
public class CourseContainer {
    private long id;

    private String courseName;

    private String date;

    private String timeFrom;

    private String timeTo;

    private int maxSeat;

    private int reservedStudentCount;

    private String status;

    public CourseContainer() {}

    public CourseContainer(Course course, String status) {
        this.id = course.getId();
        this.courseName = course.getCourseName();
        this.date = course.getDate();
        this.timeFrom = course.getTimeFrom();
        this.timeTo = course.getTimeTo();
        this.reservedStudentCount = course.getReservedStudentsSet().size();
        this.maxSeat = course.getMaxSeat();
        this.status = status;
    }
}
