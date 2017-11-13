package com.edu.domain;

import java.util.Set;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String courseName;

	private String date;

	private String timeFrom;

	private String timeTo;
	
	private int maxSeat;

	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(
		      name="STUDENT_COURSE",
		      joinColumns= @JoinColumn(name="COURSE_ID", referencedColumnName="ID"),
		      inverseJoinColumns= @JoinColumn(name="STUDENT_ID", referencedColumnName="ID"))
	@JsonIgnore
    private Set<Student> studentsSet;
	
	@ManyToMany( fetch = FetchType.EAGER)
	@JsonIgnore
	@JoinTable(
		      name="RESERVED_STUDENT_RESERVED_COURSE",
		      joinColumns= @JoinColumn(name="RESERVED_COURSE_ID", referencedColumnName="ID"),
		      inverseJoinColumns= @JoinColumn(name="RESERVED_STUDENT_ID", referencedColumnName="ID"))
	private Set<Student> reservedStudentsSet;
	
	@ManyToMany( fetch = FetchType.EAGER)
	@JsonIgnore
	@JoinTable(
		      name="STUDENT_NO_SIGN_COURSE",
		      joinColumns= @JoinColumn(name="NO_SIGN_COURSE_ID", referencedColumnName="ID"),
		      inverseJoinColumns= @JoinColumn(name="STUDENT_ID", referencedColumnName="ID"))
	private Set<Student> studentNotSignSet;
	
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

	public Set<Student> getReservedStudentsSet() {
		return reservedStudentsSet;
	}

	public void setReservedStudentsSet(Set<Student> reservedStudentsSet) {
		this.reservedStudentsSet = reservedStudentsSet;
	}

	public Set<Student> getStudentNotSignSet() {
		return studentNotSignSet;
	}

	public void setStudentNotSignSet(Set<Student> studentNotSignSet) {
		this.studentNotSignSet = studentNotSignSet;
	}

	public Course() {

	}
	
	public Course(String courseName, String date, String timeFrom, String timeTo, int maxSeat) {
		this.courseName = courseName;
		this.date = date;
		this.timeFrom = timeFrom;
		this.timeTo = timeTo;
		this.maxSeat = maxSeat;
	}

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

	public int getMaxSeat() {
		return maxSeat;
	}

	public void setMaxSeat(int maxSeat) {
		this.maxSeat = maxSeat;
	}
    
}
