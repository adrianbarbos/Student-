package com.aka.studentplus.events;

public class Eveniment {
	
	private String title;
	private int day;
	private int month;
	private int year;
	private String info;
	private String image;
	private String date;
	
	public Eveniment(String title, int day, int month, int year,
			String info, String image) {
		super();
		this.title = title;
		this.day = day;
		this.month = month;
		this.year = year;
		this.info = info;
		this.image = image;
		this.date = day + "-" + Events.months[month - 1] + "-" + year;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	
	
}
