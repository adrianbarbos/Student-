package com.aka.studentplus.forum;

public class Message {

	private String nume;
	private String message;
	private String timeStamp;
	private boolean isMyMessage;
	
	public Message(String nume, String message, String timeStamp, boolean isMyMessage) {
		super();
		this.nume = nume;
		this.message = message;
		this.timeStamp = timeStamp;
		this.isMyMessage = isMyMessage;
	}	
	
	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public boolean isMyMessage() {
		return isMyMessage;
	}

	public void setMyMessage(boolean isMyMessage) {
		this.isMyMessage = isMyMessage;
	}



	public String getNume() {
		return nume;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}


	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	

}
