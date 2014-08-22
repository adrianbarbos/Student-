package com.aka.studentplus.forum;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.aka.studentplus.admin.JSONParser;

import android.os.AsyncTask;

public class SendMsg extends AsyncTask<String, String, String> {
	
	private Message message;	
	private String id;
	private String uid;
	private static final String URL = "http://deltamg.zz9.us/www/forum/sendmessage.php";
	private static final String ID = "id";
	private static final String MESSAGE = "message";
	private static final String USER = "user";
	private static final String UID = "uid";

	public SendMsg(Message message, String id, String uid) {
		super();
		this.message = message;
		this.id = id;
		this.uid = uid;
		this.execute();
	}

	@Override
	protected String doInBackground(String... params) {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		
		parameters.add(new BasicNameValuePair(MESSAGE, message.getMessage()));
		parameters.add(new BasicNameValuePair(ID, id));
		parameters.add(new BasicNameValuePair(USER, message.getNume()));
		parameters.add(new BasicNameValuePair(UID, uid));
		
		JSONParser jsonParser = new JSONParser();
		jsonParser.makeHttpRequest(URL, "POST", parameters);
		return null;
	}

}
