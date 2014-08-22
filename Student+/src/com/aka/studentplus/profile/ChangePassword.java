package com.aka.studentplus.profile;

import java.util.HashMap;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.aka.studentplus.R;
import com.aka.studentplus.library.ConnectionDetector;
import com.aka.studentplus.library.DatabaseHandler;
import com.aka.studentplus.library.HideKeyboard;
import com.aka.studentplus.library.UserFunctions;

public class ChangePassword extends Activity {

	EditText newpass, newpass2;
	Button changepass;
	Button cancel;
	LinearLayout back;

	LinearLayout chg;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// It's enough to remove the line
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// But if you want to display full screen (without action bar) write too

		setContentView(R.layout.changepassword);

		cancel = (Button) findViewById(R.id.btcancel);
		cancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {

				finish();
			}

		});

		newpass = (EditText) findViewById(R.id.newpass);
		newpass2 = (EditText) findViewById(R.id.newpass_re);
		changepass = (Button) findViewById(R.id.btchangepass);
		chg = (LinearLayout) findViewById(R.id.chgLayout);
		back = (LinearLayout) findViewById(R.id.back);

		chg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new HideKeyboard(ChangePassword.this, chg);

			}
		});

		changepass.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (new ConnectionDetector(ChangePassword.this).isConnected()) {

					final String x = newpass.getText().toString();
					final String y = newpass2.getText().toString();

					if (x.equals(y)) {

						new ProcessRegister().execute();

					} else {
						Toast.makeText(ChangePassword.this,
								"Parolele nu sunt identice!",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		});

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});

	}

	private class ProcessRegister extends AsyncTask<String, String, JSONObject> {

		private ProgressDialog pDialog;

		String newpas, email;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			DatabaseHandler db = new DatabaseHandler(getApplicationContext());

			HashMap<String, String> user = new HashMap<String, String>();
			user = db.getUserDetails();

			newpas = newpass.getText().toString();
			newpass2.getText().toString();
			email = user.get("email");

			pDialog = new ProgressDialog(ChangePassword.this);
			pDialog.setTitle("Contacting Servers");
			pDialog.setMessage("Getting Data ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected JSONObject doInBackground(String... args) {

			UserFunctions userFunction = new UserFunctions();
			JSONObject json = userFunction.chgPass(newpas, email);
			Log.d("Button", "Register");
			return json;

		}

		@Override
		protected void onPostExecute(JSONObject json) {
			
			pDialog.dismiss();
			finish();
			
		}
	}
}
