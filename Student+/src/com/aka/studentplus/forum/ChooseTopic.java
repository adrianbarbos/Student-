package com.aka.studentplus.forum;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.aka.studentplus.R;
import com.aka.studentplus.TnlDrawerLayout;
import com.aka.studentplus.admin.JSONParser;
import com.aka.studentplus.library.ConnectionDetector;
import com.aka.studentplus.library.DatabaseHandler;

import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseTopic extends Fragment {
	
	public ChooseTopic(){}
	
	LinearLayout addTopic;
	ListView list;
	
	List<Topic> items = new ArrayList<Topic>();
	TopicAdapter adapter;
	
	DatabaseHandler db;
	
	 @Override
	    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
	        final Bundle savedInstanceState) {
	     	
	        
	        View rootView = inflater.inflate(R.layout.choose_topic, container, false);
	        
	        ImageView menu = (ImageView) rootView.findViewById(R.id.menu);		
			menu.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					TnlDrawerLayout.openMenu();
					
					
					
				}
			});
		
		db = new DatabaseHandler(getActivity());
		
		addTopic = (LinearLayout) rootView.findViewById(R.id.add_topic);
		list = (ListView) rootView.findViewById(R.id.list);
		
		if(!db.isAdmin()){
			addTopic.setVisibility(View.GONE);
		}
		
		
		
		addTopic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				addTopic();
				
			}
		});
		
		if(new ConnectionDetector(getActivity()).isConnected()){
			new GetTopics().execute();
		}
		return rootView;
		
	}
	
	private void addTopic(){
		final Dialog dialog = new Dialog(getActivity());
		dialog.setContentView(R.layout.sms_dialog);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setCancelable(true);
		final TextView subiectView = (TextView) dialog.findViewById(R.id.text);
		Button cancel = (Button) dialog.findViewById(R.id.anuleaza);
		Button ok = (Button) dialog.findViewById(R.id.trimite);
		ok.setText("Create");
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				
			}
		});
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String subiect = subiectView.getText().toString();
				if(subiect.length() == 0){
					return;
				}
				if(subiect.length() > 20){
					Toast.makeText(getActivity(), "Topic name must be 20 characters maximum", Toast.LENGTH_LONG).show();
					return;
				}
				if(new ConnectionDetector(getActivity()).isConnected()){
					new AddTopic(subiect).execute();
					dialog.dismiss();
				}				
			}
		});
		dialog.show();
	}
	
	private class GetTopics extends AsyncTask<String, String, String>{
		
		private ProgressDialog dialog;
		
		private static final String URL = "http://deltamg.zz9.us/www/forum/gettopics.php";		
		private static final String NUME_TOPIC = "topic";
		private static final String ID = "topictable";
		private static final String CREATOR = "user";
		private static final String TIMESTAMP = "timestamp";
		private static final String TOPIC_ARRAY = "topics";
		
		
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(getActivity());
			dialog.setMessage("Loading...");
			dialog.setCancelable(true);
			dialog.setIndeterminate(false);
			dialog.show();
			items = new ArrayList<Topic>();
		}

		@Override
		protected String doInBackground(String... params) {
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = jsonParser.makeHttpRequest(URL, "POST", parameters);
			Log.d("vaca suprema", jsonObject.toString());
			try {
				JSONArray jsonArray = jsonObject.getJSONArray(TOPIC_ARRAY);
				for(int i = 0; i < jsonArray.length(); i++){
					jsonObject = jsonArray.getJSONObject(i);
					String numeTopic = jsonObject.getString(NUME_TOPIC);
					String id = jsonObject.getString(ID);
					String creator = jsonObject.getString(CREATOR);
					String timeStamp = jsonObject.getString(TIMESTAMP);
					Topic topic = new Topic(numeTopic, creator, timeStamp, id);
					items.add(topic);
					
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
			adapter = new TopicAdapter(getActivity(), items);
			list.setAdapter(adapter);
		}
		
	}
	
	private class AddTopic extends AsyncTask<String, String, String>{
		
		private String numeTopic;
		
		private static final String URL = "http://deltamg.zz9.us/www/forum/inserttopic.php";
		private static final String USER = "user";
		private static final String TABLE ="table";
		private static final String TOPIC = "topic";
		
		
		
		public AddTopic(String numeTopic){
			super();
			this.numeTopic = numeTopic;
		}
		
		ProgressDialog dialog;
		
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(getActivity());
			dialog.setMessage("Se incarca...");
			dialog.setCancelable(true);
			dialog.setIndeterminate(false);
			dialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			// trimite "f_" + data + ora + min
			String timeStamp;
			Calendar c = Calendar.getInstance();
			timeStamp = "f_" 
					+ c.get(Calendar.DAY_OF_MONTH) 
					+ c.get(Calendar.MONTH) 
					+ c.get(Calendar.YEAR) 
					+ c.get(Calendar.HOUR_OF_DAY) 
					+ c.get(Calendar.MINUTE)
					+ c.get(Calendar.SECOND);
			
			parameters.add(new BasicNameValuePair(TABLE, timeStamp));
			// numele topicului
			parameters.add(new BasicNameValuePair(TOPIC, numeTopic));
			// numele utilizatorului
			String nume = db.getUserDetails().get("fname") + " " + db.getUserDetails().get("lname");
			parameters.add(new BasicNameValuePair(USER, nume));
			
			Log.d("futai", parameters.toString());
			
			JSONParser jsonParser = new JSONParser();
			jsonParser.makeHttpRequest(URL, "POST", parameters);
			
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
			new GetTopics().execute();
		}
		
	}
	
}
