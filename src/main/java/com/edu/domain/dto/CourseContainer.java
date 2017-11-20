package com.edu.domain.dto;

import com.edu.domain.Course;

public class CourseContainer {
    private long id;

    private String courseName;

	private String date;

	private String timeFrom;

	private String timeTo;
	
	private int maxSeat;
	
	private int reservedStudentCount;
	
	private String status;

	public CourseContainer() {

	}
	
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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTimeFrom() {
		return timeFrom;
	}

	public void setTimeFrom(String timeFrom) {
		this.timeFrom = timeFrom;
	}

	public String getTimeTo() {
		return timeTo;
	}

	public void setTimeTo(String timeTo) {
		this.timeTo = timeTo;
	}

	public int getReservedStudentCount() {
		return reservedStudentCount;
	}

	public void setReservedStudentCount(int reservedStudentCount) {
		this.reservedStudentCount = reservedStudentCount;
	}

	public int getMaxSeat() {
		return maxSeat;
	}

	public void setMaxSeat(int maxSeat) {
		this.maxSeat = maxSeat;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
