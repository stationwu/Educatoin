package com.edu.domain;

import java.util.Calendar;
import java.util.List;
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
    private List<Student> studentsSet;
	
	@ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.EAGER)
	@JsonIgnore
	@JoinTable(
		      name="RESERVED_STUDENT_RESERVED_COURSE",
		      joinColumns= @JoinColumn(name="RESERVED_COURSE_ID", referencedColumnName="ID"),
		      inverseJoinColumns= @JoinColumn(name="RESERVED_STUDENT_ID", referencedColumnName="ID"))
	private List<Student> reservedStudentsSet;
	
	@ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.EAGER)
	@JsonIgnore
	@JoinTable(
		      name="STUDENT_NO_SIGN_COURSE",
		      joinColumns= @JoinColumn(name="NO_SIGN_COURSE_ID", referencedColumnName="ID"),
		      inverseJoinColumns= @JoinColumn(name="STUDENT_ID", referencedColumnName="ID"))
	private List<Student> studentNotSignSet;
	
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
	
	public List<Student> getStudentsSet() {
		return studentsSet;
	}

	public void setStudentsSet(List<Student> studentsSet) {
		this.studentsSet = studentsSet;
	}

	public List<Student> getReservedStudentsSet() {
		return reservedStudentsSet;
	}

	public void setReservedStudentsSet(List<Student> reservedStudentsSet) {
		this.reservedStudentsSet = reservedStudentsSet;
	}

	public List<Student> getStudentNotSignSet() {
		return studentNotSignSet;
	}

	public void setStudentNotSignSet(List<Student> studentNotSignSet) {
		this.studentNotSignSet = studentNotSignSet;
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
