package com.edu.domain;

import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "student")
public class Student {
    @Id
    @GenericGenerator(name = "student-id-sequence",
            strategy = "com.edu.domain.generator.StudentIdentifierGenerator",
            parameters = {@Parameter(name = "sequence_prefix", value = "M"),
                          @Parameter(name = "max_digits", value = "5")})
    @GeneratedValue(generator = "student-id-sequence",
            strategy = GenerationType.TABLE)
    protected String id;

    private String studentName;

    private int classPeriod;

    private int leftPeriods;

    private int donePeriods;

    @Column(updatable = false)
    private String birthday;

    @ManyToOne
    @JoinColumn(name = "CUSTOMER_ID")
    @JsonIgnore
    private Customer customer;

    @OneToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Image> imagesSet;

    @ManyToMany(cascade = {CascadeType.MERGE},
            fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinTable(name = "STUDENT_COURSE",
            joinColumns = @JoinColumn(name = "STUDENT_ID",
                    referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "COURSE_ID",
                    referencedColumnName = "ID"))
    private Set<Course> coursesSet;

    @ManyToMany(cascade = {CascadeType.MERGE},
            fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinTable(name = "RESERVED_STUDENT_RESERVED_COURSE",
            joinColumns = @JoinColumn(name = "RESERVED_STUDENT_ID",
                    referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "RESERVED_COURSE_ID",
                    referencedColumnName = "ID"))
    private Set<Course> reservedCoursesSet;

    @ManyToMany(cascade = {CascadeType.MERGE},
            fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinTable(name = "STUDENT_NO_SIGN_COURSE",
            joinColumns = @JoinColumn(name = "STUDENT_ID",
                    referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "NO_SIGN_COURSE_ID",
                    referencedColumnName = "ID"))
    private Set<Course> courseNotSignSet;

    private boolean isChild;

    public Student() {
    }

    public Student(String studentName, String birthday, int classPeriod,
                   int leftPeriods, int donePeriods, boolean isChild) {
        this.studentName = studentName;
        this.birthday = birthday;
        this.classPeriod = classPeriod;
        this.donePeriods = donePeriods;
        this.leftPeriods = leftPeriods;
        this.isChild = isChild;
    }

    public Student(String studentName, String birthday) {
        this.studentName = studentName;
        this.birthday = birthday;
        this.classPeriod = 0;
        this.donePeriods = 0;
        this.leftPeriods = 0;
        this.isChild = true;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public boolean isChild() {
        return isChild;
    }

    public void setChild(boolean isChild) {
        this.isChild = isChild;
    }

    public void addImage(Image image) {
        this.imagesSet.add(image);
    }
    
    public void removeImage(Image image) {
        this.imagesSet.remove(image);
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
    
    public void removeReservedCourse(Course reservedCourse) {
        this.reservedCoursesSet.remove(reservedCourse);
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

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getLeftPeriods() {
        return leftPeriods;
    }

    public void setLeftPeriods(int leftPeriods) {
        this.leftPeriods = leftPeriods;
    }

    public int getDonePeriods() {
        return donePeriods;
    }

    public void setDonePeriods(int donePeriods) {
        this.donePeriods = donePeriods;
    }

    @Override
    public String toString() {
        return "Student [id=" + id + ", studentName=" + studentName
                + ", classPeriod=" + classPeriod + ", leftPeriods="
                + leftPeriods + ", donePeriods=" + donePeriods + ", birthday="
                + birthday + ", customer=" + customer + ", imagesSet="
                + imagesSet + ", coursesSet=" + coursesSet
                + ", reservedCoursesSet=" + reservedCoursesSet
                + ", courseNotSignSet=" + courseNotSignSet + ", isChild="
                + isChild + "]";
    }

}
