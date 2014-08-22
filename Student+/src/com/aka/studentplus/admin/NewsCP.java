package com.aka.studentplus.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import com.aka.studentplus.R;
 
public class NewsCP extends Activity{
 
    Button btnViewNews;
    Button btnCreateNews;
    
    LinearLayout back;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     // It's enough to remove the line
     		requestWindowFeature(Window.FEATURE_NO_TITLE);

        
        setContentView(R.layout.newscp);
 
        // Buttons
        btnViewNews = (Button) findViewById(R.id.btnViewNews);
        btnCreateNews = (Button) findViewById(R.id.btnCreateNews);
        
        back = (LinearLayout) findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
 
        // view products click event
        btnViewNews.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View view) {
                // Launching All products Activity
                Intent i = new Intent(getApplicationContext(), ViewNews.class);
                startActivity(i);
 
            }
        });
 
        // view products click event
        btnCreateNews.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View view) {
                // Launching create new product activity
                Intent i = new Intent(getApplicationContext(), AddNews.class);
                startActivity(i);
 
            }
        });
    }
}