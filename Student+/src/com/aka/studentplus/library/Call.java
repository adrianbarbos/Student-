package com.aka.studentplus.library;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class Call{

	public Call(Context context, String telefon) {
		super();
		try {
	        Intent callIntent = new Intent(Intent.ACTION_CALL);
	        callIntent.setData(Uri.parse("tel:" + telefon));
	        context.startActivity(callIntent);
	    } catch (ActivityNotFoundException activityException) {
	         Log.e("android call try", "Call failed");
	    }
	}
		
}
