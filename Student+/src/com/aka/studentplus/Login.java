package com.aka.studentplus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aka.studentplus.admin.JSONParser;
import com.aka.studentplus.library.AppPreferences;
import com.aka.studentplus.library.ConnectionDetector;
import com.aka.studentplus.library.DatabaseHandler;
import com.aka.studentplus.library.HideKeyboard;
import com.aka.studentplus.library.UserFunctions;

public class Login extends Activity {

	RelativeLayout layout;
	EditText email;
	EditText password;
	TextView forgot_password;
	TextView register_text;
	TextView register_why;
	Button login;	
	CheckBox rememberView;

	AppPreferences global;
	

	/**
	 * Called when the activity is first created.
	 */
	private static String KEY_SUCCESS = "success";
	private static String KEY_UID = "uid";
	private static String KEY_UNIVERSITY = "university";
	private static String KEY_COLLEGE = "college";
	private static String KEY_DEPARTMENT = "department";
	private static String KEY_PHONE = "phone";
	private static String KEY_ADRESS = "adress";
	private static String KEY_FIRSTNAME = "fname";
	private static String KEY_LASTNAME = "lname";
	private static String KEY_IMAGE = "image";
	private static String KEY_EMAIL = "email";
	private static String KEY_CREATED_AT = "created_at";
	private static String KEY_STATUT = "type";
	private static String KEY_BIRTHDAY = "birthday";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		
		global = new AppPreferences(Login.this);

		layout = (RelativeLayout) findViewById(R.id.logLayout);
		email = (EditText) findViewById(R.id.email);
		password = (EditText) findViewById(R.id.pword);
		forgot_password = (TextView) findViewById(R.id.forgot_password);
		login = (Button) findViewById(R.id.login);
		rememberView = (CheckBox) findViewById(R.id.remember);
		
		rememberView.setChecked(global.getRememberPassword());
		
		if(rememberView.isChecked()){
			email.setText(global.getFirstField());
			password.setText(global.getSecondField());
		}
		
		rememberView.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				global.setRememberPassword(rememberView.isChecked());
				
			}
		});

		// Layout hide keyboard
		layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				new HideKeyboard(Login.this, layout);

			}
		});

		

		// Forgot password TextView
		forgot_password.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				final Dialog dialog = new Dialog(Login.this);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.sms_dialog);
				dialog.setCancelable(true);
				dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
				final EditText text = (EditText) dialog.findViewById(R.id.text);
				text.setHint("Enter your email");
				Button cancel = (Button) dialog.findViewById(R.id.anuleaza);
				Button ok = (Button) dialog.findViewById(R.id.trimite);
				cancel.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						
					}
				});
				ok.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						retrievePass(text.getText().toString());
						dialog.dismiss();
						
					}
				});
				dialog.show();
			}
		});

		

		// Login button
		login.setOnClickListener(new View.OnClickListener() {



			public void onClick(View view) {
				if (new ConnectionDetector(Login.this).isConnected()) {

					if ((!email.getText().toString().equals(""))
							&& (!password.getText().toString().equals(""))) {
						new ProcessLogin().execute();
					} else if ((!email.getText().toString().equals(""))) {
						Toast.makeText(getApplicationContext(),
								"Parola necompletata!", Toast.LENGTH_SHORT)
								.show();
					} else if ((!password.getText().toString().equals(""))) {
						Toast.makeText(getApplicationContext(),
								"Email necompletat!", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(getApplicationContext(),
								"Fill in all the fields!!",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		});

	
		// Text color feedback white to gray
		XmlResourceParser whitetogray = getResources().getXml(
				R.drawable.on_press_white);
		try {
			ColorStateList white = ColorStateList.createFromXml(getResources(),
					whitetogray);
			forgot_password.setTextColor(white);
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	

	}
	
	/**
	 * Async Task to get and send data to My Sql database through JSON respone.
	 **/
	private class ProcessLogin extends AsyncTask<String, String, JSONObject> {

		private ProgressDialog pDialog;

		String inputemail, inputpassword;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			

			inputemail = email.getText().toString();
			inputpassword = password.getText().toString();
			pDialog = new ProgressDialog(Login.this);
			pDialog.setMessage("Loading...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected JSONObject doInBackground(String... args) {

			UserFunctions userFunction = new UserFunctions();
			JSONObject json = userFunction.loginUser(inputemail, inputpassword);
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject json) {
			try {

				String res = json.getString(KEY_SUCCESS);

				if (res.equals("1")) {
					pDialog.setMessage("Loading User Space");
					pDialog.setTitle("Getting Data");
					DatabaseHandler db = new DatabaseHandler(
							getApplicationContext());
					JSONObject json_user = json.getJSONObject("user");
					/**
					 * Clear all previous data in SQlite database.
					 **/
					UserFunctions logout = new UserFunctions();
					logout.logoutUser(getApplicationContext());
					Log.d("inainte de preluare", "aici");
					String fname = json_user.optString(KEY_FIRSTNAME);
					String lname = json_user.optString(KEY_LASTNAME);
					String email = json_user.optString(KEY_EMAIL);
					String uid = json_user.optString(KEY_UID);
					String created_at = json_user.optString(KEY_CREATED_AT);
					String image = json_user.optString(KEY_IMAGE);
					String university = json_user.optString(KEY_UNIVERSITY);
					String college = json_user.optString(KEY_COLLEGE);
					String department = json_user.optString(KEY_DEPARTMENT);
					String phone = json_user.optString(KEY_PHONE);
					String adress = json_user.optString(KEY_ADRESS);
					String statut = json_user.optString(KEY_STATUT);
					String birthday = json_user.optString(KEY_BIRTHDAY);
					System.out.println(uid);
					db.addUser(fname, lname, email, university, college,
							department, phone, adress, statut, birthday, image, uid, created_at);
					Log.d("dupa preluare", "success");
					/**
					 * If JSON array details are stored in SQlite it launches
					 * the User Panel.
					 **/
					Intent intent = new Intent(getApplicationContext(),
							TnlDrawerLayout.class);
					if(global.getRememberPassword()){
						global.setFirstField(inputemail);
						global.setSecondField(inputpassword);
					}else{
						global.setFirstField(null);
						global.setSecondField(null);
					}
					pDialog.dismiss();
					startActivity(intent);
					/**
					 * Close Login Screen
					 **/
				} else {

					pDialog.dismiss();
					Toast.makeText(Login.this, "User sau parola gresita!",
							Toast.LENGTH_SHORT).show();
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	private void retrievePass(String email){
		new RetrievePass(email).execute();
	}
	
	private class RetrievePass extends AsyncTask<String, String, String>{
		
		private static final String URL = "";
		private static final String EMAIL = "email";
		
		private String email;
		
		public RetrievePass(String email){
			super();
			this.email = email;
		}

		@Override
		protected String doInBackground(String... params) {
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			parameters.add(new BasicNameValuePair(EMAIL, email));
			JSONParser jsonParser = new JSONParser();
			jsonParser.makeHttpRequest(URL, "POST", parameters);
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			Toast.makeText(Login.this, "A new password will be sent to your email", Toast.LENGTH_LONG).show();
		}
		
	}
	
}