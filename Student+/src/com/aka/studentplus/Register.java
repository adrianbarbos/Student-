package com.aka.studentplus;

import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aka.studentplus.admin.Settings;
import com.aka.studentplus.library.ConnectionDetector;
import com.aka.studentplus.library.DatabaseHandler;
import com.aka.studentplus.library.HideKeyboard;
import com.aka.studentplus.library.UserFunctions;

public class Register extends Activity {

	/**
	 * JSON Response node names.
	 **/

	private static String KEY_SUCCESS = "success";
	private static String KEY_UID = "uid";
	private static String KEY_FIRSTNAME = "fname";
	private static String KEY_LASTNAME = "lname";
	private static String KEY_UNIVERSITY = "university";
	private static String KEY_COLLEGE = "college";
	private static String KEY_DEPARTMENT = "department";
	private static String KEY_PHONE = "phone";
	private static String KEY_ADRESS = "adress";
	private static String KEY_IMAGE = "image";
	private static String KEY_EMAIL = "email";
	private static String KEY_CREATED_AT = "created_at";
	private static String KEY_ERROR = "error";
	private static String KEY_STATUT = "statut";
	private static String KEY_BIRTHDAY = "birthday";

	/**
	 * Defining layout items.
	 **/

	EditText inputFirstName;
	EditText inputLastName;
	// EditText inputUniversity;
	// EditText inputCollege;
	// EditText inputDepartment;
	EditText inputPhone;
	EditText inputAdress;
	EditText inputEmail;
	EditText inputPassword;
	TextView inputDataNasterii;
	Button btnRegister;
	TextView registerErrorMsg;

	Spinner inputUniversity;
	Spinner inputCollege;
	Spinner inputDepartment;
	Spinner inputStatut;
	ImageView back;

	LinearLayout reg;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// It's enough to remove the line
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// But if you want to display full screen (without action bar) write too

		setContentView(R.layout.register);

		/**
		 * Defining all layout items
		 **/
		inputFirstName = (EditText) findViewById(R.id.editText1);
		inputLastName = (EditText) findViewById(R.id.editText2);
		// inputUniversity = (EditText) findViewById(R.id.editText5);
		inputUniversity = (Spinner) findViewById(R.id.editText5);
		// inputCollege = (EditText) findViewById(R.id.editText6);
		inputCollege = (Spinner) findViewById(R.id.editText6);
		// inputDepartment = (EditText) findViewById(R.id.editText7);
		inputDataNasterii = (TextView) findViewById(R.id.data_nasterii);
		inputDepartment = (Spinner) findViewById(R.id.editText7);
		inputStatut = (Spinner) findViewById(R.id.statut);
		inputPhone = (EditText) findViewById(R.id.editText3);
		inputAdress = (EditText) findViewById(R.id.editText8);
		inputEmail = (EditText) findViewById(R.id.editText4);
		inputPassword = (EditText) findViewById(R.id.editText9);
		btnRegister = (Button) findViewById(R.id.register);
		registerErrorMsg = (TextView) findViewById(R.id.register_error);
		reg = (LinearLayout) findViewById(R.id.registerLayout);
		back = (ImageView) findViewById(R.id.back);

		reg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new HideKeyboard(Register.this, reg);

			}
		});
		
		inputDataNasterii.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				OnDateSetListener callBack = new OnDateSetListener() {
					
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear,
							int dayOfMonth) {
						String dataNasterii = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
						inputDataNasterii.setText(dataNasterii);
						
					}
				};
				DatePickerDialog dialog = new DatePickerDialog(Register.this, callBack, Calendar.getInstance().get(Calendar.YEAR), 0, 1);
				dialog.setCancelable(true);
				dialog.show();
			}
		});

		/**
		 * Button which Switches back to the login screen on clicked
		 **/

		inputUniversity.setAdapter(new ArrayAdapter<String>(Register.this,
				R.layout.spinner_row, R.id.spinner_custom, getResources()
						.getStringArray(R.array.university_list)));
		inputCollege.setAdapter(new ArrayAdapter<String>(Register.this,
				R.layout.spinner_row, R.id.spinner_custom, getResources()
						.getStringArray(R.array.college_list)));
		inputDepartment.setAdapter(new ArrayAdapter<String>(Register.this,
				R.layout.spinner_row, R.id.spinner_custom, getResources() 
						.getStringArray(R.array.department_list)));
		inputStatut.setAdapter(new ArrayAdapter<String>(Register.this, 
				R.layout.spinner_row, R.id.spinner_custom, getResources()
				.getStringArray(R.array.statut_array)));

		Button login = (Button) findViewById(R.id.bktologin);
		login.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				finish();
			}

		});
		
		
back.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		finish();
		
	}
});
		

		/**
		 * Register Button click event. A Toast is set to alert when the fields
		 * are empty. Another toast is set to alert Username must be 5
		 * characters.
		 **/

		btnRegister.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (new ConnectionDetector(Register.this).isConnected()) {
					Log.d("crapa", "aici1");
					if ((inputPassword.getText().toString().length() > 0)
							&& (inputUniversity.getSelectedItemPosition() != 0)
							&& (inputCollege.getSelectedItemPosition() != 0)
							&& (inputDepartment.getSelectedItemPosition() != 0)
							&& (inputFirstName.getText().toString().length() > 0)
							&& (inputLastName.getText().toString().length() > 0)
							&& (inputEmail.getText().toString().length() > 0)
							&& (inputPhone.getText().toString().length() > 0)
							&& (inputAdress.getText().toString().length() > 0)
							&& (inputStatut.getSelectedItemPosition() != 0)
							&& (inputDataNasterii.getText().toString().length() > 0)) {
						Log.d("crapa", "aici2");
						if(new ConnectionDetector(Register.this).isConnected()){
							new ProcessRegister().execute();
						}

					} else {
						Toast.makeText(
								getApplicationContext(),
								"Unul sau mai multe campuri sunt necompletate!",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}

	/**
	 * Async Task to check whether internet connection is working
	 **/

	private class ProcessRegister extends AsyncTask<String, String, JSONObject> {

		/**
		 * Defining Process dialog
		 **/
		private ProgressDialog pDialog;

		String email, password, fname, lname, university, college, department,
				phone, adress, image, statut, birthday;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			fname = inputFirstName.getText().toString();
			lname = inputLastName.getText().toString();
			email = inputEmail.getText().toString();

			int position = inputUniversity.getSelectedItemPosition();
			university = getResources().getStringArray(R.array.university_list)[position];
			int positionc = inputCollege.getSelectedItemPosition();
			college = getResources().getStringArray(R.array.college_list)[positionc];
			int positiond = inputDepartment.getSelectedItemPosition();
			department = getResources().getStringArray(R.array.department_list)[positiond];
			int positions = inputStatut.getSelectedItemPosition();
			statut = getResources().getStringArray(R.array.statut_array)[positions];
			

			// college = inputCollege.getText().toString();
			// department = inputDepartment.getText().toString();
			birthday = inputDataNasterii.getText().toString();
			phone = inputPhone.getText().toString();
			adress = inputAdress.getText().toString();
			password = inputPassword.getText().toString();
			image = "imagine";
			pDialog = new ProgressDialog(Register.this);
			pDialog.setTitle("Conectare");
			pDialog.setMessage("Se creeaza contul ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected JSONObject doInBackground(String... args) {

			UserFunctions userFunction = new UserFunctions();
			JSONObject json = userFunction.registerUser(fname, lname, email,
					university, college, department, phone, adress, password, statut, birthday,
					image);

			return json;

		}

		@Override
		protected void onPostExecute(JSONObject json) {
			/**
			 * Checks for success message.
			 **/
			try {

				registerErrorMsg.setText("");
				String res = json.getString(KEY_SUCCESS);

				String red = json.getString(KEY_ERROR);

				if (res.equals("1")) {

					registerErrorMsg.setText("Inregistrare reusita!");

					DatabaseHandler db = new DatabaseHandler(
							getApplicationContext());
					JSONObject json_user = json.getJSONObject("user");

					/**
					 * Removes all the previous data in the SQlite database
					 **/

					UserFunctions logout = new UserFunctions();
					logout.logoutUser(getApplicationContext());
					db.addUser(json_user.getString(KEY_FIRSTNAME),
							json_user.getString(KEY_LASTNAME),
							json_user.getString(KEY_EMAIL),
							json_user.getString(KEY_UNIVERSITY),
							json_user.getString(KEY_COLLEGE),
							json_user.getString(KEY_DEPARTMENT),
							json_user.getString(KEY_PHONE),
							json_user.getString(KEY_ADRESS),
							json_user.getString(KEY_IMAGE),
							json_user.getString(KEY_STATUT),
							json_user.getString(KEY_BIRTHDAY),
							json_user.getString(KEY_UID),
							json_user.getString(KEY_CREATED_AT));
					/**
					 * Stores registered data in SQlite Database Launch
					 * Registered screen
					 **/

					Intent registered = new Intent(Register.this,
							Settings.class);

					/**
					 * Close all views before launching Registered screen
					 **/
					registered.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					pDialog.dismiss();
					startActivity(registered);

					finish();
				}

				else if (Integer.parseInt(red) == 2) {
					pDialog.dismiss();
					registerErrorMsg.setText("Cont existent!");
				} else if (Integer.parseInt(red) == 3) {
					pDialog.dismiss();
					registerErrorMsg.setText("Email nevalid!");
				}

			} catch (JSONException e) {
				e.printStackTrace();

			}
		}
	}

}
