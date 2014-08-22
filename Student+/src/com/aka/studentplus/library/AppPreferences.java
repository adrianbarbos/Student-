package com.aka.studentplus.library;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppPreferences {
	
	private static final String FIRST_FIELD = "first_field";
	private static final String SECOND_FIELD = "second_field";
	private static final String REMEMBER_PASS = "remember_pass";
	
    private static final String APP_SHARED_PREFS = AppPreferences.class.getSimpleName(); 
    
    private SharedPreferences sharedPrefs;
    private Editor prefsEditor;

    public AppPreferences(Context context) {
        this.sharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
        this.prefsEditor = sharedPrefs.edit();
    }
    
    public boolean getRememberPassword(){
    	return sharedPrefs.getBoolean(REMEMBER_PASS, true);
    }
    
    public void setRememberPassword(boolean remember){
    	prefsEditor.putBoolean(REMEMBER_PASS, remember);
    	prefsEditor.commit();
    }
    
    public String getFirstField() {
    	
    	// Get preferences
    	
        return sharedPrefs.getString(FIRST_FIELD, "");
    }

    public void setFirstField(String text) {
    	
    	// Store preferences
    	
        prefsEditor.putString(FIRST_FIELD, text);
        prefsEditor.commit();
    }
    
    public void setSecondField(String text){
    	
    	// Store preferences
    	
    	prefsEditor.putString(SECOND_FIELD, text);
    	prefsEditor.commit();
    }
    
    public String getSecondField(){
    	
    	// Get preferences
    	
    	return sharedPrefs.getString(SECOND_FIELD, "");
    }
	
	
}
