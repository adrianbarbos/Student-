package com.aka.studentplus;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class Tutorial extends Activity {
	
	private ImageView i1;
	private ImageView i2;
	private ImageView i3;
	private ImageView i4;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.tutorial);
		
		ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
		LinearLayout skip = (LinearLayout) findViewById(R.id.skip_btn);
		i1 = (ImageView) findViewById(R.id.first_bullet);
		i2 = (ImageView) findViewById(R.id.second_bullet);
		i3 = (ImageView) findViewById(R.id.third_bullet);
		i4 = (ImageView) findViewById(R.id.forth_bullet);
		
		ImageAdapter adapter = new ImageAdapter(Tutorial.this);		
		viewPager.setAdapter(adapter);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				
				switch (position) {
				case 0: setBulletColor(i1,i2,i3,i4);					
					break;
				case 1: setBulletColor(i2,i1,i3,i4);		
					break;
				case 2: setBulletColor(i3,i2,i1,i4);		
					break;
				case 3 : setBulletColor(i4,i3,i2,i1);			
					break;

				default:
					break;
				}
				
				
			}
			
			
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int position) {
				
				
				
			}
		});
		
		skip.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Tutorial.this, TnlDrawerLayout.class);
				startActivity(intent);
				finish();
				
			}
		});
	}
	
	private void setBulletColor(ImageView i1,ImageView i2, ImageView i3, ImageView i4){
		
		
		
		Bullet bullet = new Bullet();
		bullet.setOn(i1);
		bullet.setOff1(i2);
		bullet.setOff2(i3);
		bullet.setOff3(i4);
		bullet.execute();
		
		
	}
	
	@Override
	public void onBackPressed() {
		
		Intent intent = new Intent(Tutorial.this, TnlDrawerLayout.class);
		startActivity(intent);
		finish();
		
	}
	
	private class Bullet extends AsyncTask<String, String, String>{
		private ImageView on;
		private ImageView off1;
		private ImageView off2;
		private ImageView off3;
		public void setOn(ImageView on) {
			this.on = on;
		}

		public void setOff1(ImageView off1) {
			this.off1 = off1;
		}

		public void setOff2(ImageView off2) {
			this.off2 = off2;
		}

		public void setOff3(ImageView off3) {
			this.off3 = off3;
		}


		@Override
		protected String doInBackground(String... params) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			on.setImageResource(R.drawable.bullet_on);
			off1.setImageResource(R.drawable.bullet_off);
			off2.setImageResource(R.drawable.bullet_off);
			off3.setImageResource(R.drawable.bullet_off);
			super.onPostExecute(result);
		}
		
		
	}
}
