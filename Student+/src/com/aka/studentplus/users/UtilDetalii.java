package com.aka.studentplus.users;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aka.studentplus.R;
import com.aka.studentplus.library.Call;
import com.aka.studentplus.library.ImageLoader;
import com.aka.studentplus.library.SMS;
import com.aka.studentplus.library.SendEmail;

public class UtilDetalii extends Activity {
	
	ImageLoader imageLoader;
	ImageView imageView;
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
	ImageView emailbtn;
	
	Utilizator utilizator;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.member_profile);
		
		imageLoader = new ImageLoader(UtilDetalii.this);
		
		utilizator = Utilizatori.getUtilizator();
		
		imageView = (ImageView) findViewById(R.id.avatar);
		
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
		sms = (ImageView) findViewById(R.id.smsbutton);
		emailbtn = (ImageView) findViewById(R.id.emailbutton);
				

		imageLoader.DisplayImage(utilizator.getImage(), R.drawable.temp_img, imageView);
	

		profileName.setText(utilizator.getName() + " " + utilizator.getSurname());
		profileType.setText(utilizator.getType());
		email.setText(utilizator.getEmail());
		university.setText(utilizator.getUniversity());
		college.setText(utilizator.getCollege());
		department.setText(utilizator.getDepartment());
		phone.setText(utilizator.getPhone());
		adress.setText(utilizator.getAddress());
		birthday.setText(utilizator.getBirthday());
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			finish();
				
			}
		});
		
		call.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new Call(UtilDetalii.this, phone.getText().toString());
				
			}
		});
		
		
		sms.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final String number = phone.getText().toString();
				final Dialog dialog = new Dialog(UtilDetalii.this);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.sms_dialog);
				dialog.setCancelable(true);
				dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
				Button ok = (Button) dialog.findViewById(R.id.trimite);
				Button cancel = (Button) dialog.findViewById(R.id.anuleaza);
				final TextView textView = (TextView) dialog.findViewById(R.id.text);
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
							Toast.makeText(UtilDetalii.this, "Mesajul este gol", Toast.LENGTH_SHORT).show();
							return;
						}
						SMS.sendSMS(UtilDetalii.this, number, text);
						dialog.dismiss();
					}
				});
				dialog.show();
				
				
			}
		});
		
		
		
		emailbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new SendEmail(UtilDetalii.this, email.getText().toString());
				
			}
		});
			
	}

}
