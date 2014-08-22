package com.aka.studentplus.events;

import com.aka.studentplus.R;
import com.aka.studentplus.library.ImageLoader;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewEvent extends Activity {
	
	ImageView imageView;
	TextView titleView;
	TextView dateView;
	TextView contentView;
    LinearLayout back;
	
	ImageLoader imageLoader;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.newsdetails);
		
		back = (LinearLayout) findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		
		imageLoader = new ImageLoader(ViewEvent.this);
		
		titleView = (TextView) findViewById(R.id.inputHeadline2);
		dateView = (TextView) findViewById(R.id.date);
		contentView = (TextView) findViewById(R.id.inputDesc1);
		imageView = (ImageView) findViewById(R.id.imagebig);
		
		String title = getIntent().getExtras().getString("title");
		String date = getIntent().getExtras().getString("date");
		String content = getIntent().getExtras().getString("content");
		String image = getIntent().getExtras().getString("image");
		
		titleView.setText(title);
		dateView.setText(date);
		contentView.setText(content);
		
		if(image.length() > 0){
			imageLoader.DisplayImage(image, R.drawable.temp_img, imageView);
			System.out.println(image);
			imageView.setVisibility(View.VISIBLE);
		}
	}

}
