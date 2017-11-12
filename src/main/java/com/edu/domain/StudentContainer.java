package com.edu.domain;

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

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public int getClassPeriod() {
		return classPeriod;
	}

	public void setClassPeriod(int classPeriod) {
		this.classPeriod = classPeriod;
	}

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
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

    public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
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

    @Override
    public String toString() {
        return "StudentContainer [id=" + id + ", studentName=" + studentName
                + ", classPeriod=" + classPeriod + ", leftPeriods="
                + leftPeriods + ", donePeriods=" + donePeriods + ", birthday="
                + birthday + ", parentName=" + parentName + ", mobilePhone="
                + mobilePhone + ", address=" + address + "]";
    }
	
	
}
