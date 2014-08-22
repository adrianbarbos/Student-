package com.aka.studentplus.forum;

public class Topic {
	
	private String subiect;
	private String creator;
	private String timeStamp;
	private String id;
	
	private static Topic topic;
	
	public static Topic getTopic(){
		return Topic.topic;
	}
	
	public static void setTopic(Topic topic){
		Topic.topic = topic;
	}
	
	public Topic(String subiect, String creator, String timeStamp, String id) {
		super();
		this.subiect = subiect;
		this.creator = creator;
		this.timeStamp = timeStamp;
		this.id = id;
	}	
	
	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}



	public String getTimeStamp() {
		return timeStamp;
	}



	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}



	public String getSubiect() {
		return subiect;
	}

	public void setSubiect(String subiect) {
		this.subiect = subiect;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	

}
