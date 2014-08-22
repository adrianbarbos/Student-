package com.aka.studentplus.library;

/**
 * Created by Raj Amal on 5/30/13.
 */




import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "member_csl";

    // Login table name
    private static final String TABLE_LOGIN = "login_member_csl";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_FIRSTNAME = "fname";
    private static final String KEY_LASTNAME = "lname";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_UNIVERSITY = "university";
    private static final String KEY_COLLEGE = "college";
    private static final String KEY_DEPARTMENT = "department";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_ADRESS = "adress";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_STATUT = "statut";
    private static final String KEY_BIRTHDAY = "birthday";
    private static final String KEY_UID = "uid";
    private static final String KEY_CREATED_AT = "created_at";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_FIRSTNAME + " TEXT,"
                + KEY_LASTNAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE,"
                + KEY_UNIVERSITY + " TEXT,"
                + KEY_COLLEGE + " TEXT,"
                + KEY_DEPARTMENT + " TEXT,"
                + KEY_PHONE + " TEXT,"
                + KEY_ADRESS + " TEXT,"
                + KEY_IMAGE + " TEXT,"
                + KEY_STATUT + " TEXT,"
                + KEY_BIRTHDAY + " TEXT,"
                + KEY_UID + " TEXT,"
                + KEY_CREATED_AT + " TEXT" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addUser(String fname, String lname, String email, String university, String college, String department, String phone, String adress, String statut, String birthday, String image, String uid, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FIRSTNAME, fname); // FirstName
        values.put(KEY_LASTNAME, lname); // LastName
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_UNIVERSITY, university); // UserName
        values.put(KEY_COLLEGE, college); // UserName
        values.put(KEY_DEPARTMENT, department); // UserName
        values.put(KEY_PHONE, phone); // UserName
        values.put(KEY_ADRESS, adress); // UserName
        values.put(KEY_IMAGE, image); // Image
        values.put(KEY_STATUT, statut);//statut
        values.put(KEY_BIRTHDAY, birthday);//birthday
        values.put(KEY_UID, uid); // Email
        values.put(KEY_CREATED_AT, created_at); // Created At

        // Inserting Row
        db.insert(TABLE_LOGIN, null, values);
        db.close(); // Closing database connection
    }


    /**
     * Getting user data from database
     * */
   public HashMap<String, String> getUserDetails(){
        HashMap<String,String> user = new HashMap<String,String>();
        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            user.put("fname", cursor.getString(1));
            user.put("lname", cursor.getString(2));
            user.put("email", cursor.getString(3));
            user.put("university", cursor.getString(4));
            user.put("college", cursor.getString(5));
            user.put("department", cursor.getString(6));
            user.put("phone", cursor.getString(7));
            user.put("adress", cursor.getString(8));
            user.put("image", cursor.getString(9));
            user.put("statut", cursor.getString(10));
            user.put("birthday", cursor.getString(11));
            user.put("uid", cursor.getString(12));
        }
        cursor.close();
        db.close();
        // return user
        return user;
    }






    /**
     * Getting user login status
     * return true if rows are there in table
     * */
    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_LOGIN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();

        // return row count
        return rowCount;
    }


    /**
     * Re crate database
     * Delete all tables and create them again
     * */
    public void resetTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_LOGIN, null, null);
        db.close();
    }
    
    public boolean isAdmin(){
    	String email = getUserDetails().get("email");
    	if(email.equalsIgnoreCase("admin")){
    		return true;
    	}
    	if(email.equalsIgnoreCase("adrian.barbos@trencadis.ro")){
    		return true;
    	}
    	if(email.equalsIgnoreCase("andrei.calbajos@trencadis.ro")){
    		return true;
    	}
    	return false;
    }

}
