package com.aka.studentplus.library;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Toast;

public class SMS extends Activity {
	 @Override
	    public void onCreate(Bundle savedInstanceState){
	        //...
	    }
	 
	    //---sends an SMS message to another device---
	    public static void sendSMS(Context context, String phoneNumber, String message){        
	        PendingIntent pi = PendingIntent.getActivity(context, 0,
	            new Intent(context, SMS.class), 0);                
	        SmsManager sms = SmsManager.getDefault();
	        
	        sms.sendTextMessage(phoneNumber, null, message, pi, null); 
	        
	    }
	    
	    public static void sendSMS(final Context context, final String[] phoneNumbers, final String message){
	    	
	    	class Send extends AsyncTask<String, String, String>{
	    		
	    		ProgressDialog dialog;
	    		int i;
	    		
	    		@Override
	    		protected void onPreExecute() {
	    			dialog = new ProgressDialog(context);
	    			dialog.setMessage("Se trimite...");
	    			dialog.setCancelable(true);
	    			dialog.setIndeterminate(false);
	    			dialog.show();
	    		}
	    		
				@Override
				protected String doInBackground(String... params) {
					String number;
			    	for(i = 0; i < phoneNumbers.length; i++){
			    		number = phoneNumbers[i];
			    		sendSMS(context, number, message);
			    	}					
			    	return null;
				}
				
				@Override
				protected void onPostExecute(String result) {
					if(i == phoneNumbers.length){
						Toast.makeText(context, "Mesajele au fost trimise", Toast.LENGTH_SHORT).show();
					}
					dialog.dismiss();
				}
	    		
	    	}
	    	
	    	new Send().execute();   	
	    	
	    	
	    }
	    
	    
}
