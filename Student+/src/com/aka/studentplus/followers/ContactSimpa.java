package com.aka.studentplus.followers;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.aka.studentplus.R;
import com.aka.studentplus.admin.JSONParser;
import com.aka.studentplus.library.ConnectionDetector;
import com.aka.studentplus.library.SMS;
import com.aka.studentplus.library.SendEmail;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ContactSimpa extends Activity {
	
	CheckBox selectAll;
	Button sms;
	Button email;
	ListView list;
	
	LinearLayout back;
	
	List<Simpatizant> items = new ArrayList<Simpatizant>();
	ContactSimpaAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.contact_simpatizanti);
		
		back = (LinearLayout) findViewById(R.id.back);
		selectAll = (CheckBox) findViewById(R.id.select_all);
		sms = (Button) findViewById(R.id.sms);
		email = (Button) findViewById(R.id.email);
		list = (ListView) findViewById(R.id.list);
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		
		selectAll.setChecked(true);
		selectAll.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				for(int i = 0; i < list.getCount(); i++){
					View view = list.getChildAt(i);
					CheckBox checkBox = (CheckBox) view.findViewById(R.id.select);
					checkBox.setChecked(isChecked);
				}
				
			}
		});
		
		sms.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sendSMSToAll();
				
			}
		});
		
		email.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sendEmailToAll();
				
			}
		});
		
		if(new ConnectionDetector(ContactSimpa.this).isConnected()){
			new GetSimpa().execute();
		}
	}
	
	private void sendEmailToAll(){
		List<String> emailList = new ArrayList<String>();
		for(int i = 0; i < items.size(); i++){
			View view = list.getChildAt(i);
			CheckBox checkBox = (CheckBox) view.findViewById(R.id.select);
			if(checkBox.isChecked()){
				emailList.add(items.get(i).getEmail());
			}
		}
		String[] emails = new String[emailList.size()];
		for(int i = 0; i < emails.length; i++){
			emails[i] = emailList.get(i);
		}
		new SendEmail(ContactSimpa.this, emails);
	}
	
	private void sendSMSToAll(){
		List<String> phonesList = new ArrayList<String>();
		for(int i = 0; i < items.size(); i++){
			View view = list.getChildAt(i);
			CheckBox checkBox = (CheckBox) view.findViewById(R.id.select);
			if(checkBox.isChecked()){
				phonesList.add(items.get(i).getTelefon());
			}
		}
		String[] phones = new String[phonesList.size()];
		for(int i = 0; i < phones.length; i++){
			phones[i] = phonesList.get(i);
		}
		smsDialog(phones);
	}
	
	private void smsDialog(final String[] phones){

		final Dialog dialog = new Dialog(ContactSimpa.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.sms_dialog);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setCancelable(true);
		final TextView textView = (TextView) dialog.findViewById(R.id.text);
		Button ok = (Button) dialog.findViewById(R.id.trimite);
		Button cancel = (Button) dialog.findViewById(R.id.anuleaza);
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				
			}
		});
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String text = textView.getText().toString();
				if(text.length() == 0){
					Toast.makeText(ContactSimpa.this, "Scrieti un mesaj", Toast.LENGTH_SHORT).show();
					return;
				}
				SMS.sendSMS(ContactSimpa.this, phones, text);
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	
	private class GetSimpa extends AsyncTask<String, String, String>{
		
		private static final String URL = "http://deltamg.zz9.us/www/simpa/api/get_simpa.php";
		private static final String NUME = "nume";
		private static final String PRENUME = "prenume";
		private static final String EMAIL = "email";
		private static final String TELEFON = "tel";
		private static final String SIMPATIZANTI = "simpatizanti";
		
		ProgressDialog dialog;
		
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(ContactSimpa.this);
			dialog.setMessage("Se incarca...");
			dialog.setCancelable(true);
			dialog.setIndeterminate(false);
			dialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = jsonParser.makeHttpRequest(URL, "POST", parameters);
			try {
				JSONArray jsonArray = jsonObject.getJSONArray(SIMPATIZANTI);
				
				for(int i = 0; i < jsonArray.length(); i++){
					jsonObject = jsonArray.getJSONObject(i);
					String nume = jsonObject.getString(NUME);
					String prenume = jsonObject.getString(PRENUME);
					String email = jsonObject.getString(EMAIL);
					String telefon = jsonObject.getString(TELEFON);
					Simpatizant simpatizant = new Simpatizant(nume, prenume, telefon, email);
					items.add(simpatizant);
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
	
	private void setAdapter(){
		adapter = new ContactSimpaAdapter(ContactSimpa.this, items);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				CheckBox select = (CheckBox) view.findViewById(R.id.select);
				select.setChecked(!select.isChecked());
				
				
			}
		});
	}
	
}
