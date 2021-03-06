package com.aka.studentplus.admin;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.aka.studentplus.R;
 
public class EditNews extends Activity {
 
    EditText txtHeadline;
    EditText txtStory;
    Button btnSave;
    Button btnDelete;
    
    LinearLayout back;
 
    String id;
 
    // Progress Dialog
    private ProgressDialog pDialog;
 
    // JSON parser class
    JSONParser jsonParser = new JSONParser();
 
    // single product url
    private static final String url_product_detials = "http://deltamg.zz9.us/www/news/api/get_story_detail.php";
 
    // url to update product
    private static final String url_update_product = "http://deltamg.zz9.us/www/news/api/update_news.php";
 
    // url to delete product
    private static final String url_delete_product = "http://deltamg.zz9.us/www/news/api/delete_story.php";
 
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_NEWS = "news";
    private static final String TAG_PID = "id";
    private static final String TAG_HEADLINE = "headline";
    private static final String TAG_STORY = "story";
 
    @Override 
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     // It's enough to remove the line
     		requestWindowFeature(Window.FEATURE_NO_TITLE);

        
        setContentView(R.layout.editnews);
        
        //for random error of target SDK version
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
 
        // save button
        btnSave = (Button) findViewById(R.id.btnSave);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        back = (LinearLayout) findViewById(R.id.back);
        
        back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
 
        // getting product details from intent
        Intent i = getIntent();
 
        // getting product id (pid) from intent
        id = i.getStringExtra(TAG_PID);
 
        // Getting complete product details in background thread
        new GetProductDetails().execute();
 
        // save button click event
        btnSave.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View arg0) {
                // starting background task to update product
                new SaveProductDetails().execute();
            }
        });
 
        // Delete button click event
        btnDelete.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View arg0) {
                // deleting product in background thread
                new DeleteProduct().execute();
            }
        });
 
    }
 
    /**
     * Background Async Task to Get complete product details
     * */
    class GetProductDetails extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditNews.this);
            pDialog.setMessage("Se incarca...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
        /**
         * Getting product details in background thread
         * */
        protected String doInBackground(String... params) {
 
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    // Check for success tag
                    int success;
                    try {
                        // Building Parameters
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("id", id));
 
                        // getting product details by making HTTP request
                        // Note that product details url will use GET request
                        JSONObject json = jsonParser.makeHttpRequest(
                                url_product_detials, "GET", params);
 
                        // check your log for json response
                        Log.d("Single Product Details", json.toString());
 
                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // successfully received product details
                            JSONArray productObj = json
                                    .getJSONArray(TAG_NEWS); // JSON Array
 
                            // get first product object from JSON Array
                            JSONObject product = productObj.getJSONObject(0);
 
                            // product with this pid found
                            // Edit Text
                            txtHeadline = (EditText) findViewById(R.id.inputHeadline);
                            txtStory = (EditText) findViewById(R.id.inputDesc);
 
                            // display product data in EditText
                            txtHeadline.setText(product.getString(TAG_HEADLINE));
                            txtStory.setText(product.getString(TAG_STORY));
 
                        }else{
                            Log.d("ddd", "ddd");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
 
            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once got all details
            pDialog.dismiss();
        }
    }
 
    /**
     * Background Async Task to  Save product Details
     * */
    class SaveProductDetails extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditNews.this);
            pDialog.setMessage("Se salveaza ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
        /**
         * Saving product
         * */
        protected String doInBackground(String... args) {
 
            // getting updated data from EditTexts
            String headline = txtHeadline.getText().toString();
            String story = txtStory.getText().toString();
 
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(TAG_PID, id));
            params.add(new BasicNameValuePair(TAG_HEADLINE, headline));
            params.add(new BasicNameValuePair(TAG_STORY, story));
 
            // sending modified data through http request
            // Notice that update product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_update_product,
                    "POST", params);
 
            // check json success tag
            try {
                int success = json.getInt(TAG_SUCCESS);
 
                if (success == 1) {
                    // successfully updated
                    Intent i = getIntent();
                    // send result code 100 to notify about product update
                    setResult(100, i);
                    finish();
                } else {
                    // failed to update product
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
 
            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product uupdated
            pDialog.dismiss();
        }
    }
 
    /*****************************************************************
     * Background Async Task to Delete Product
     * */
    class DeleteProduct extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditNews.this);
            pDialog.setMessage("Se sterge...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
        /**
         * Deleting product
         * */
        protected String doInBackground(String... args) {
 
            // Check for success tag
            int success;
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("id", id));
 
                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(
                        url_delete_product, "POST", params);
 
                // check your log for json response
                Log.d("Delete Product", json.toString());
 
                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // product successfully deleted
                    // notify previous activity by sending code 100
                    Intent i = getIntent();
                    // send result code 100 to notify about product deletion
                    setResult(100, i);
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
 
            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
 
        }
 
    }
}