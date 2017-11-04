package com.edu.domain;

import java.time.LocalDate;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected long id;

	private String studentName;

	private int age;

	private String address;

	private int classPeriod;
	
    private String birthday;  

	@ManyToOne
    @JoinColumn(name = "CUSTOMER_ID")
    private Customer customer;

	@OneToMany(fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<Image> imagesSet;

    @ManyToMany(cascade = { CascadeType.MERGE }, fetch = FetchType.EAGER)
	@JsonIgnore
	@JoinTable(
		      name="STUDENT_COURSE",
		      joinColumns= @JoinColumn(name="STUDENT_ID", referencedColumnName="ID"),
		      inverseJoinColumns= @JoinColumn(name="COURSE_ID", referencedColumnName="ID"))
	private Set<Course> coursesSet;

	@ManyToMany(cascade = { CascadeType.MERGE }, fetch = FetchType.EAGER)
	@JsonIgnore
	@JoinTable(
		      name="RESERVED_STUDENT_RESERVED_COURSE",
		      joinColumns= @JoinColumn(name="RESERVED_STUDENT_ID", referencedColumnName="ID"),
		      inverseJoinColumns= @JoinColumn(name="RESERVED_COURSE_ID", referencedColumnName="ID"))
	private Set<Course> reservedCoursesSet;
	
	@ManyToMany(cascade = { CascadeType.MERGE }, fetch = FetchType.EAGER)
	@JsonIgnore
	@JoinTable(
		      name="STUDENT_NO_SIGN_COURSE",
		      joinColumns= @JoinColumn(name="STUDENT_ID", referencedColumnName="ID"),
		      inverseJoinColumns= @JoinColumn(name="NO_SIGN_COURSE_ID", referencedColumnName="ID"))
	private Set<Course> courseNotSignSet;

	private boolean isChild;
	
	public Student() {
	}
	
	public Student(String studentName, int age, String address, int classPeriod,
			boolean isChild) {
		this.studentName = studentName;
		this.age = age;
		this.address = address;
		this.classPeriod = classPeriod;
		this.isChild = isChild;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public boolean isChild() {
		return isChild;
	}

	public void setChild(boolean isChild) {
		this.isChild = isChild;
	}

	public void setImagesList(Set<Image> imagesSet) {
		this.imagesSet = imagesSet;
	}

	public void addImage(Image image) {
		this.imagesSet.add(image);
	}

	public Set<Course> getCoursesSet() {
		return coursesSet;
	}
	
	public void setCoursesSet(Set<Course> coursesSet) {
		this.coursesSet = coursesSet;
	}
	
	public void addCourse(Course course) {
		this.coursesSet.add(course);
	}

	public int getClassPeriod() {
		return classPeriod;
	}

	public void setClassPeriod(int classPeriod) {
		this.classPeriod = classPeriod;
	}

	public Set<Image> getImagesSet() {
		return imagesSet;
	}

	public void setImagesSet(Set<Image> imagesSet) {
		this.imagesSet = imagesSet;
	}
	
	public Set<Course> getReservedCoursesSet() {
		return reservedCoursesSet;
	}

	public void setReservedCoursesSet(Set<Course> reservedCoursesSet) {
		this.reservedCoursesSet = reservedCoursesSet;
	}
	
	public void addReservedCourse(Course reservedCourse) {
		this.reservedCoursesSet.add(reservedCourse);
	}

	public Set<Course> getCourseNotSignSet() {
		return courseNotSignSet;
	}

	public void setCourseNotSignSet(Set<Course> courseNotSignSet) {
		this.courseNotSignSet = courseNotSignSet;
	}

	public void addCourseNotSign(Course courseNotSign) {
		this.courseNotSignSet.add(courseNotSign);
	}

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
	
	@Override
	public String toString() {
		String str = "Student.id" + this.getId() + "/nStudent.name" + this.getStudentName()
                + "/nStudent.classperiod" + this.getClassPeriod();
		return str;
	}

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
}
