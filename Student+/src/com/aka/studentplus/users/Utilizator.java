package com.aka.studentplus.users;

public class Utilizator {
	
	private String name;
	private String surname;
	private String type;
	private String email;
	private String phone;
	private String university;
	private String college;
	private String department;
	private String address;
	private String image;
	private String birthday;
	
	public Utilizator(String name, String surname, String type, String email,
			String phone, String university, String college, String department,
			String address, String image, String birthday) {
		super();
		this.name = name;
		this.surname = surname;
		this.type = type;
		this.email = email;
		this.phone = phone;
		this.university = university;
		this.college = college;
		this.department = department;
		this.address = address;
		this.image = image;
		this.birthday = birthday;
	}
	
	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getUniversity() {
		return university;
	}
	public void setUniversity(String university) {
		this.university = university;
	}
	public String getCollege() {
		return college;
	}
	public void setCollege(String college) {
		this.college = college;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	
	

}
