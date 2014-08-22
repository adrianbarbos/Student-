package com.aka.studentplus.forum;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.aka.studentplus.R;
import com.aka.studentplus.admin.JSONParser;
import com.aka.studentplus.library.ConnectionDetector;
import com.aka.studentplus.library.DatabaseHandler;

public class Forum extends Activity {
	
	LinearLayout back;
	ListView list;
	EditText editMsg;
	ImageView send;
	ImageView refresh;
	
	List<Message> items = new ArrayList<Message>();
	ForumAdapter adapter;
	
	String nume;
	String prenume;
	String uid;
	
	private Topic topic = Topic.getTopic();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.forum);
		
		TextView title = (TextView) findViewById(R.id.profilHome);
		title.setText(topic.getSubiect());
		
		DatabaseHandler db = new DatabaseHandler(Forum.this);
		nume = db.getUserDetails().get("fname");
		prenume = db.getUserDetails().get("lname");
		uid = db.getUserDetails().get("uid");
		
		back = (LinearLayout) findViewById(R.id.back);
		list = (ListView) findViewById(R.id.list);
		editMsg = (EditText) findViewById(R.id.edit_msg);
		send = (ImageView) findViewById(R.id.send);
		refresh = (ImageView) findViewById(R.id.refresh);
		
		if(new ConnectionDetector(Forum.this).isConnected()){
			new GetMsgs().execute();
		}
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onBackPressed();
				
			}
		});
		
		send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				send();
				
			}
		});
		
		refresh.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(new ConnectionDetector(Forum.this).isConnected()){
					new GetMsgs().execute();
				}
				
			}
		}); 
	}
	
	private void send(){
		if(!new ConnectionDetector(Forum.this).isConnected()){
			return;
		}
		String msg = editMsg.getText().toString();
		if (msg.length() == 0){
			return;
		}
		// send message
		Calendar c = Calendar.getInstance();
		String timeStamp = c.get(Calendar.DAY_OF_MONTH) + "-"
				+ c.get(Calendar.MONTH) + "-"
				+ c.get(Calendar.YEAR) + " "
				+ c.get(Calendar.HOUR_OF_DAY) + ":"
				+ c.get(Calendar.MINUTE);
		Message message = new Message(nume + " " + prenume, msg, timeStamp, true);
		new SendMsg(message, topic.getId(), uid);
		items.add(message);
		setAdapter();
		editMsg.setText(null);
	}
	
	private void setAdapter(){
		adapter = new ForumAdapter(Forum.this, items);
		list.setAdapter(adapter);
		list.setSelection(list.getCount());
	}
	
	@Override
	public void onBackPressed() {
		
		finish();
	}
	
	private class GetMsgs extends AsyncTask<String, String, String>{
		
		private static final String URL = "http://deltamg.zz9.us/www/forum/getmessages.php";
		private static final String UID = "uid";
		private static final String USER = "user";
		private static final String MESSAGE = "message";
		private static final String TIMESTAMP = "timestamp";
		private static final String ID = "id";
		private static final String MESSAGES = "messages";
		
		ProgressDialog dialog;
		
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(Forum.this);
			dialog.setMessage("Loading...");
			dialog.setCancelable(true);
			dialog.setIndeterminate(false);
			dialog.show();
		}
		
		@Override
		protected String doInBackground(String... params) {
			items = new ArrayList<Message>();
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			parameters.add(new BasicNameValuePair(ID, topic.getId()));
			
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = jsonParser.makeHttpRequest(URL, "POST", parameters);
			try {
				JSONArray jsonArray = jsonObject.getJSONArray(MESSAGES);
				for(int i = 0; i < jsonArray.length(); i++){
					jsonObject = jsonArray.getJSONObject(i);
					String msgUid = jsonObject.getString(UID);
					String message = jsonObject.getString(MESSAGE);
					String user = jsonObject.getString(USER);
					String timeStamp = jsonObject.getString(TIMESTAMP);
					String[] x = timeStamp.split(":");
					timeStamp = x[0] + ":" + x[1];
					Message msg = new Message(user, message, timeStamp, msgUid.equalsIgnoreCase(uid));
					items.add(msg);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
			setAdapter();
		}
		
	}
	
}
