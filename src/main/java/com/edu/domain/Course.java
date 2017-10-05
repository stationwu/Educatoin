package com.edu.domain;

import java.util.Calendar;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "course")
public class Course extends BaseEntity{
	private String courseName;

	private String date;

	private String timeFrom;

	private String timeTo;

	@ManyToMany(cascade={CascadeType.MERGE,CascadeType.PERSIST},fetch=FetchType.EAGER)
	@JoinTable(
		      name="STUDENT_COURSE",
		      joinColumns= @JoinColumn(name="COURSE_ID", referencedColumnName="ID"),
		      inverseJoinColumns= @JoinColumn(name="STUDENT_ID", referencedColumnName="ID"))
	@JsonIgnore
    private Set<Student> studentsSet;
	
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
	
	public Set<Student> getStudentsSet() {
		return studentsSet;
	}

	public void setStudentsSet(Set<Student> studentsSet) {
		this.studentsSet = studentsSet;
	}

	public Course() {

	}
	
	public Course(String courseName, String date, String timeFrom, String timeTo) {
		this.courseName = courseName;
		this.date = date;
		this.timeFrom = timeFrom;
		this.timeTo = timeTo;
	}
	
}
