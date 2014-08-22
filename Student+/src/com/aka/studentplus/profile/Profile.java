package com.aka.studentplus.profile;

import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aka.studentplus.R;
import com.aka.studentplus.library.DatabaseHandler;
import com.aka.studentplus.library.ImageLoader;

public class Profile extends Activity {

	LinearLayout back;
	TextView profileName;
	TextView profileType;
	TextView email;
	TextView university;
	TextView college;
	TextView department;
	TextView phone;
	TextView adress;
	TextView birthday;
	
	ImageView call;
	ImageView sms;
	ImageView emailbutton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// It's enough to remove the line
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.member_profile);

		back = (LinearLayout) findViewById(R.id.back);
		profileName = (TextView) findViewById(R.id.profilename);
		profileType = (TextView) findViewById(R.id.profiletype);
		email = (TextView) findViewById(R.id.email);
		university = (TextView) findViewById(R.id.university);
		college = (TextView) findViewById(R.id.college);
		department = (TextView) findViewById(R.id.department);
		phone = (TextView) findViewById(R.id.phone);
		adress = (TextView) findViewById(R.id.adress);
		birthday = (TextView) findViewById(R.id.birthday);
		
		call = (ImageView) findViewById(R.id.callbutton);
		call.setVisibility(View.GONE);
		sms = (ImageView) findViewById(R.id.smsbutton);
		sms.setVisibility(View.GONE);
		emailbutton = (ImageView) findViewById(R.id.emailbutton);
		emailbutton.setVisibility(View.GONE);
		
		
		back.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {

				finish();
			}

		});

		DatabaseHandler db = new DatabaseHandler(getApplicationContext());

		/**
		 * Hashmap to load data from the Sqlite database
		 **/
		HashMap<String, String> user = new HashMap<String, String>();
		user = db.getUserDetails();

		profileName.setText(user.get("fname") + " " + user.get("lname"));
		profileType.setText(user.get("statut"));
		email.setText(user.get("email"));
		university.setText(user.get("university"));
		college.setText(user.get("college"));
		department.setText(user.get("department"));
		phone.setText(user.get("phone"));
		adress.setText(user.get("adress"));
		birthday.setText(user.get("birthday"));

		//new DownloadImageTask((ImageView) findViewById(R.id.avatarHome))
				//.execute(user.get("image"));

		// Loader image - will be shown before loading image
		int loader = R.drawable.temp_img;

		// Imageview to show
		ImageView image = (ImageView) findViewById(R.id.avatar);

		// Image url
		String image_url = user.get("image");

		// ImageLoader class instance
		ImageLoader imgLoader = new ImageLoader(getApplicationContext());

		// whenever you want to load an image from url
		// call DisplayImage function
		// url - image url to load
		// loader - loader image, will be displayed before getting image
		// image - ImageView
		imgLoader.DisplayImage(image_url, loader, image);

	}
}
