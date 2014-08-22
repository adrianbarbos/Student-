package com.aka.studentplus;


import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.aka.studentplus.Config;
import com.aka.studentplus.R;
import com.aka.studentplus.Controller;
import com.aka.studentplus.admin.Settings;
import com.aka.studentplus.events.Events;
import com.aka.studentplus.followers.ViewS;
import com.aka.studentplus.forum.ChooseTopic;
import com.aka.studentplus.library.DatabaseHandler;
import com.aka.studentplus.library.ImageLoader;
import com.aka.studentplus.news.News;
import com.aka.studentplus.profile.Profile;
import com.aka.studentplus.users.Utilizatori;
import com.google.android.gcm.GCMRegistrar;


@SuppressLint("NewApi")
public class TnlDrawerLayout extends Activity{
	

	public static String name;
	public static String email;

	TextView lblMessage;
	Controller aController;

	// Asynctask
	AsyncTask<Void, Void, Void> mRegisterTask;

	private static DrawerLayout drawer;
	private ExpandableListView drawerList;
	private static ScrollView layout;
	
	private RelativeLayout noutati;
	private RelativeLayout evenimente;
	private RelativeLayout membri;
	private RelativeLayout simpatizanti;
	private RelativeLayout forum;
	private RelativeLayout setari;
	
	private LinearLayout logout;
	private ImageView avatar;
	private TextView profileName;
	private TextView profileType;
	@SuppressWarnings("unused")
	private ActionBarDrawerToggle actionBarDrawerToggle;
	
	private final static int on = Color.rgb(12, 20, 22);
	private final static int off = Color.rgb(21, 29, 32);

    private static FragmentManager fragmentManager;
	

	@SuppressLint("CutPasteId")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_drawer_layout_test);
		
		//GCM stuff

		DatabaseHandler db1 = new DatabaseHandler(getApplicationContext());

		/**
		 * Hashmap to load data from the Sqlite database
		 **/
		HashMap<String, String> user1 = new HashMap<String, String>();
		user1 = db1.getUserDetails();

		name = user1.get("fname");
		email = user1.get("lname");
		// Get Global Controller Class object (see application tag in
				// AndroidManifest.xml)
				aController = (Controller) getApplicationContext();

				// Make sure the device has the proper dependencies.
				GCMRegistrar.checkDevice(this);

				// Make sure the manifest permissions was properly set
				GCMRegistrar.checkManifest(this);

				lblMessage = (TextView) findViewById(R.id.lblMessage);

				// Register custom Broadcast receiver to show messages on activity
				registerReceiver(mHandleMessageReceiver, new IntentFilter(
						Config.DISPLAY_MESSAGE_ACTION));

				// Get GCM registration id
				final String regId = GCMRegistrar.getRegistrationId(this);

				// Check if regid already presents
				if (regId.equals("")) {

					// Register with GCM
					GCMRegistrar.register(this, Config.GOOGLE_SENDER_ID);

				} else {

					// Device is already registered on GCM Server
					if (GCMRegistrar.isRegisteredOnServer(this)) {

						// Skips registration.

					} else {

						// Try to register again, but not in the UI thread.
						// It's also necessary to cancel the thread onDestroy(),
						// hence the use of AsyncTask instead of a raw thread.

						final Context context = this;
						mRegisterTask = new AsyncTask<Void, Void, Void>() {

							@Override
							protected Void doInBackground(Void... params) {

								// Register on our server
								// On server creates a new user
								aController.register(context, name, email, regId);

								return null;
							}

							@Override
							protected void onPostExecute(Void result) {
								mRegisterTask = null;
							}

						};

						// execute AsyncTask
						mRegisterTask.execute(null, null, null);
					}
				}
				
				
				//end GCM
		
		
		
		
		
		fragmentManager = getFragmentManager();
		setGroupData();
		setChildGroupData();
		
		setSlidingActionBarEnabled(true);
		
		initDrawer();
		

		logout = (LinearLayout) findViewById(R.id.logout);
		profileName = (TextView) findViewById(R.id.profilename);
		profileType = (TextView) findViewById(R.id.type);
		avatar = (ImageView) findViewById(R.id.avatar);
		
		avatar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(TnlDrawerLayout.this, Profile.class);
				startActivity(intent);
				
			}
		});
	
		setLogOut(logout);
		
		setFragment(new News());
		
		DatabaseHandler db = new DatabaseHandler(getApplicationContext());

		/**
		 * Hashmap to load data from the Sqlite database
		 **/
		HashMap<String, String> user = new HashMap<String, String>();
		user = db.getUserDetails();

		profileName.setText(user.get("fname") + " " + user.get("lname"));
		profileType.setText(user.get("statut"));
		
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
	
	
	
	public static void setFragment(Fragment fragment){
		if (fragment != null) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment).commit();
 
            drawer.closeDrawers();
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
	}
	
	public static void openMenu(){
		drawer.openDrawer(layout);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.drawer_layout_test, menu);
		return true;
	}

	private void initDrawer() {
		drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		
		layout = (ScrollView) findViewById(R.id.left_drawer);
		drawerList = (ExpandableListView) findViewById(R.id.list);
		
		noutati = (RelativeLayout) findViewById(R.id.noutati);
		evenimente = (RelativeLayout) findViewById(R.id.evenimente);
		membri = (RelativeLayout) findViewById(R.id.membri);
		simpatizanti = (RelativeLayout) findViewById(R.id.simpatizanti);
		forum = (RelativeLayout) findViewById(R.id.forum);
		setari = (RelativeLayout) findViewById(R.id.setari);
		
		
		
		noutati.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TnlDrawerLayout.setFragment(new News());
				
				noutati.setBackgroundColor(on);
				evenimente.setBackgroundColor(off);
				membri.setBackgroundColor(off);
				simpatizanti.setBackgroundColor(off);
				forum.setBackgroundColor(off);
				setari.setBackgroundColor(off);
				
			}
		});		
		
		evenimente.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TnlDrawerLayout.setFragment(new Events());
				
				noutati.setBackgroundColor(off);
				evenimente.setBackgroundColor(on);
				membri.setBackgroundColor(off);
				simpatizanti.setBackgroundColor(off);
				forum.setBackgroundColor(off);
				setari.setBackgroundColor(off);
				
				
			}
		});	
		
		membri.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				TnlDrawerLayout.setFragment(new Utilizatori());
				
				noutati.setBackgroundColor(off);
				evenimente.setBackgroundColor(off);
				membri.setBackgroundColor(on);
				simpatizanti.setBackgroundColor(off);
				forum.setBackgroundColor(off);
				setari.setBackgroundColor(off);
				
				
			}
		});	
		
		simpatizanti.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				TnlDrawerLayout.setFragment(new ViewS());
				
				noutati.setBackgroundColor(off);
				evenimente.setBackgroundColor(off);
				membri.setBackgroundColor(off);
				simpatizanti.setBackgroundColor(on);
				forum.setBackgroundColor(off);
				setari.setBackgroundColor(off);
				
				
			}
		});	
		
		forum.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TnlDrawerLayout.setFragment(new ChooseTopic());
				
				noutati.setBackgroundColor(off);
				evenimente.setBackgroundColor(off);
				membri.setBackgroundColor(off);
				simpatizanti.setBackgroundColor(off);
				forum.setBackgroundColor(on);
				setari.setBackgroundColor(off);
				
				
			}
		});
		
		
		setari.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TnlDrawerLayout.setFragment(new Settings());
				
				noutati.setBackgroundColor(off);
				evenimente.setBackgroundColor(off);
				membri.setBackgroundColor(off);
				simpatizanti.setBackgroundColor(off);
				forum.setBackgroundColor(off);
				setari.setBackgroundColor(on);
				
				
			}
		});
		

		
		layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {				
				
			}
		});
		
		
		setSlidingActionBarEnabled(true);
		


		// actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer,
		// R.drawable.ic_drawer, R.string.open_drawer,
		// R.string.close_drawer) {
		// public void onDrawerClosed(View view) {
		// getActionBar().setSubtitle("open");
		// }
		//
		// /** Called when a drawer has settled in a completely open state. */
		// public void onDrawerOpened(View drawerView) {
		// getActionBar().setSubtitle("close");
		// }
		//
		// };
		//
		// drawer.setDrawerListener(actionBarDrawerToggle);

	}

	private void setSlidingActionBarEnabled(boolean b) {
		// TODO Auto-generated method stub
		
	}

	public void setGroupData() {
		groupItem.add("Info");
		groupItem.add("Contact");
		groupItem.add("Raporteaza");
		groupItem.add("Acceseaza");
	}

	ArrayList<String> groupItem = new ArrayList<String>();
	ArrayList<Object> childItem = new ArrayList<Object>();

	public void setChildGroupData() {
		/**
		 * Add Data For TecthNology
		 */
		ArrayList<String> child = new ArrayList<String>();
		child.add("Home");
		child.add("submenu");
		child.add("Ghid turistic");
		child.add("submenu");
		childItem.add(child);

		/**
		 * Add Data For Mobile
		 */
		child = new ArrayList<String>();
		child.add("submenu");
		child.add("submenu");
		child.add("submenu");
		child.add("submenu");
		childItem.add(child);
		/**
		 * Add Data For Manufacture
		 */
		child = new ArrayList<String>();
		child.add("O problema cu drumurile");
		child.add("Violenta stradala");
		child.add("Salubritate defectuoasa");
		child.add("Depune o petitie");
		child.add("Alta problema");
		childItem.add(child);
		/**
		 * Add Data For Extras
		 */
		child = new ArrayList<String>();
		child.add("submenu");
		child.add("submenu");
		child.add("submenu");
		child.add("Tutorial");
		childItem.add(child);
		
	}

	
	
	private void setLogOut(LinearLayout view){
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder dialog = new AlertDialog.Builder(TnlDrawerLayout.this);
				dialog.setMessage("Iesiti din aplicatie?");
				dialog.setPositiveButton("Da", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
						
					}
				});
				dialog.setNegativeButton("Nu", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						
					}
				});
				dialog.setCancelable(true);
				dialog.show();
			}
		});
		
		
	}
	
	
		
		

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
	    super.onWindowFocusChanged(hasFocus);
	    if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
	    	drawerList.setIndicatorBounds(drawerList.getRight()- 40, drawerList.getWidth());
	    } else {
	    	drawerList.setIndicatorBoundsRelative(drawerList.getRight()- 80, drawerList.getWidth());
	    }
	}
	
	@Override
	public void onBackPressed() {
	}
	
	// Create a broadcast receiver to get message and show on screen
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			String newMessage = intent.getExtras().getString(
					Config.EXTRA_MESSAGE);

			// Waking up mobile if it is sleeping
			aController.acquireWakeLock(getApplicationContext());

			// Display message on the screen
			lblMessage.append(newMessage + "\n");

			// Releasing wake lock
			aController.releaseWakeLock();
		}
	};

	@Override
	protected void onDestroy() {
		// Cancel AsyncTask
		if (mRegisterTask != null) {
			mRegisterTask.cancel(true);
		}
		try {
			// Unregister Broadcast Receiver
			unregisterReceiver(mHandleMessageReceiver);

			// Clear internal resources.
			GCMRegistrar.onDestroy(this);

		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
		super.onDestroy();
	}

}
