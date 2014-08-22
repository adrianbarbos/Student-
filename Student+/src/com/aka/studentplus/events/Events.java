package com.aka.studentplus.events;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.aka.studentplus.R;
import com.aka.studentplus.TnlDrawerLayout;
import com.aka.studentplus.admin.JSONParser;
import com.aka.studentplus.library.ConnectionDetector;

public class Events extends Fragment {
	
	public Events(){}

	ImageView next;
	ImageView previous;
	Button monthPicker;
	
	ListView list;
	EventsAdapter adapter;
	List<Eveniment> items = new ArrayList<Eveniment>();
	Sort sort;
	
	public static String[] months;
	
	 @Override
	    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
	        final Bundle savedInstanceState) {
	     	
	        
	        View rootView = inflater.inflate(R.layout.evenimente, container, false);
	        
	        ImageView menu = (ImageView) rootView.findViewById(R.id.menu);		
			menu.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					TnlDrawerLayout.openMenu();
					
					
					
				}
			});
		
		next = (ImageView) rootView.findViewById(R.id.next);
		previous = (ImageView) rootView.findViewById(R.id.previous);
		monthPicker = (Button) rootView.findViewById(R.id.month_picker);
		
		months = getResources().getStringArray(R.array.months);
		
		list = (ListView) rootView.findViewById(R.id.list);
		
		if(new ConnectionDetector(getActivity()).isConnected()){
			new GetEvents().execute();
		}
		next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				nextMonth();
				
			}
		});
		
		previous.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				previousMonth();
				
			}
		});
		return rootView;
		
	}
	
	private void nextMonth(){
		String date = monthPicker.getText().toString();
		String monthString = date.split(", ")[0];
		int month = 0;
		for(int i = 0; i < months.length; i++){
			if(months[i].equalsIgnoreCase(monthString)){
				month = i;
				break;
			}
		}
		int year = Integer.parseInt(date.split(", ")[1]);
		Calendar c = Calendar.getInstance();
		c.set(Calendar.MONTH, month);
		c.set(Calendar.YEAR, year);
		c.add(Calendar.MONTH, 1);
		month = c.get(Calendar.MONTH);
		year = c.get(Calendar.YEAR);
		monthPicker.setText(months[month] + ", " + year);
		items = sort.sortByMonth(month, year);
		adapter = new EventsAdapter(getActivity(), items);
		list.setAdapter(adapter);
	}
	
	private void previousMonth(){
		String date = monthPicker.getText().toString();
		String monthString = date.split(", ")[0];
		int month = 0;
		for(int i = 0; i < months.length; i++){
			if(months[i].equalsIgnoreCase(monthString)){
				month = i;
				break;
			}
		}
		int year = Integer.parseInt(date.split(", ")[1]);
		Calendar c = Calendar.getInstance();
		c.set(Calendar.MONTH, month);
		c.set(Calendar.YEAR, year);
		c.add(Calendar.MONTH, -1);
		month = c.get(Calendar.MONTH);
		year = c.get(Calendar.YEAR);
		monthPicker.setText(months[month] + ", " + year);
		items = sort.sortByMonth(month, year);
		adapter = new EventsAdapter(getActivity(), items);
		list.setAdapter(adapter);
	}
	
	private class GetEvents extends AsyncTask<String, String, String>{
		
		private ProgressDialog dialog;
		private int success = 0;
		
		private static final String URL = "http://deltamg.zz9.us/www/events/get_events.php";
		private static final String EVENTS = "events";
		private static final String SUCCESS = "success";
		private static final String TITLE = "title";
		private static final String DAY = "day";
		private static final String MONTH = "month";
		private static final String YEAR = "year";
		private static final String CONTENT = "content";
		private static final String IMAGE = "img";
		
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(getActivity());
			dialog.setMessage("Loading...");
			dialog.setCancelable(true);
			dialog.setIndeterminate(false);
			dialog.show();
		}

		@Override
		protected String doInBackground(String... arg0) {
			
			JSONParser jsonParser = new JSONParser();
			
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			
			JSONObject jsonObject = jsonParser.makeHttpRequest(URL, "GET", parameters);
			
			try {
				success = jsonObject.getInt(SUCCESS);
				
				if(success == 1){
					JSONArray jsonArray = jsonObject.getJSONArray(EVENTS);
					
					for(int i = 0; i < jsonArray.length(); i++){
						jsonObject = jsonArray.getJSONObject(i);
						String title = jsonObject.getString(TITLE);
						String content = jsonObject.getString(CONTENT);
						String image = jsonObject.getString(IMAGE);
						int day = jsonObject.getInt(DAY);
						int month = jsonObject.getInt(MONTH);
						int year = jsonObject.getInt(YEAR);
						
						Eveniment eveniment = new Eveniment(title, day, month, year, content, image);
						
						items.add(eveniment);
					}
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			dialog.dismiss();
			if(success != 1){
				Toast.makeText(getActivity(), "Eroare de conexiune", Toast.LENGTH_SHORT).show();
			}else{
				sort = new Sort(items);
				items = sort.sortByThisMonth();
				Calendar c = Calendar.getInstance();
				int month = c.get(Calendar.MONTH);
				int year = c.get(Calendar.YEAR);
				monthPicker.setText(months[month] + ", " + year);
				adapter = new EventsAdapter(getActivity(), items);
				list.setAdapter(adapter);
			}
		}
		
	}

	
}
