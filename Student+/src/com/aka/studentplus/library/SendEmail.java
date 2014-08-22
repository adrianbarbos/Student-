package com.aka.studentplus.library;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class SendEmail {

	public SendEmail(Context context, String email) {
		super();
		String[] x = new String[1];
		x[0] = email;
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("message/rfc822");
		intent.putExtra(Intent.EXTRA_EMAIL  , x);
		try {
		    context.startActivity(Intent.createChooser(intent, "Send mail..."));
		} catch (android.content.ActivityNotFoundException ex) {
		    Toast.makeText(context, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
		}
	}
	
	public SendEmail(Context context, String[] emails){
		super();
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("message/rfc822");
		intent.putExtra(Intent.EXTRA_EMAIL, emails);
		try {
		    context.startActivity(Intent.createChooser(intent, "Send mail..."));
		} catch (android.content.ActivityNotFoundException ex) {
		    Toast.makeText(context, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
		}
	}
	
}
