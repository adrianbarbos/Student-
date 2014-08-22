package com.aka.studentplus.news;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aka.studentplus.R;
import com.aka.studentplus.admin.JSONParser;
import com.aka.studentplus.library.ImageLoader;
 
public class NewsDetails extends Activity {
 
    TextView txtHeadline;
    TextView txtStory;
    TextView txtDate;
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
    private static final String TAG_IMAGE = "image";
    private static final String TAG_DATE = "timestamp";
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     // It's enough to remove the line
     		requestWindowFeature(Window.FEATURE_NO_TITLE);

        
        setContentView(R.layout.newsdetails);
        
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        
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
            pDialog = new ProgressDialog(NewsDetails.this);
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
                            txtHeadline = (TextView) findViewById(R.id.inputHeadline2);
                            txtStory = (TextView) findViewById(R.id.inputDesc1);
                            txtDate = (TextView) findViewById(R.id.date);
 
                            // display product data in EditText
                            txtHeadline.setText(product.getString(TAG_HEADLINE));
                            txtStory.setText(product.getString(TAG_STORY));
                            

         	               String[] x = product.getString(TAG_DATE).split(" ");
         	               
         	               String dateName = x[0];
         	               
         	               x = dateName.split("-");
         	               String year = x[0];
         	               String month = x[1];
         	               String day = x[2];
         	               
         	               if(month.charAt(0) == '0'){
         	            	   month = month.substring(1, 2);
         	               }
         	               
         	               String[] monthsArray = getResources().getStringArray(R.array.months);
         	               
         	               int luna = Integer.parseInt(month);
         	               
         	               month = monthsArray[luna - 1];
         	               
         	               dateName = day + " " + month + " " + year;
                            
                            txtDate.setText(dateName);
                            

                    		//new DownloadImageTask((ImageView) findViewById(R.id.avatarHome))
                    				//.execute(user.get("image"));

                    		// Loader image - will be shown before loading image
                    		int loader = R.drawable.temp_img;

                    		// Imageview to show
                    		ImageView image = (ImageView) findViewById(R.id.imagebig);

                    		// Image url
                    		String image_url = product.getString(TAG_IMAGE);

                    		// ImageLoader class instance
                    		ImageLoader imgLoader = new ImageLoader(getApplicationContext());

                    		// whenever you want to load an image from url
                    		// call DisplayImage function
                    		// url - image url to load
                    		// loader - loader image, will be displayed before getting image
                    		// image - ImageView
                    		imgLoader.DisplayImage(image_url, loader, image);
 
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
            pDialog = new ProgressDialog(NewsDetails.this);
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
            pDialog = new ProgressDialog(NewsDetails.this);
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