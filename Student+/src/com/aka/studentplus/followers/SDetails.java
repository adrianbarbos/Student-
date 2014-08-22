package com.aka.studentplus.followers;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aka.studentplus.R;
import com.aka.studentplus.admin.JSONParser;
import com.aka.studentplus.library.Call;
import com.aka.studentplus.library.ConnectionDetector;
import com.aka.studentplus.library.SendEmail;
 
public class SDetails extends Activity {
 
    TextView numeView;
    TextView prenumeView;
    TextView emailView;
    TextView telefonView;
    TextView localitateView;
    TextView adresaView;
    TextView studiiView;
    TextView facultateView;
 
    String id;
 
    // Progress Dialog
    private ProgressDialog pDialog;
 
    // JSON parser class
    JSONParser jsonParser = new JSONParser();
 
    // single product url
    private static final String url_product_detials = "http://deltamg.zz9.us/www/simpa/api/get_simpa_detail.php";
 
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String SIMPATIZANTI = "simpatizanti";
    private static final String ID = "id";
    private static final String NUME = "nume";
    private static final String PRENUME = "prenume";
    private static final String EMAIL = "email";
    private static final String TELEFON = "tel";
    private static final String ADRESA = "adresa";
    private static final String LOCALITATE = "localitate";
    private static final String STUDII = "studii";
    private static final String FACULTATE = "facultate";
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     // It's enough to remove the line
     		requestWindowFeature(Window.FEATURE_NO_TITLE);

        
        setContentView(R.layout.viewsimpatizantidetails);
        
        LinearLayout back = (LinearLayout) findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
        
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
 
 
      
        // getting product id (pid) from intent
        id =getIntent().getExtras().getString(ID);
 
        // Getting complete product details in background thread
        if(new ConnectionDetector(SDetails.this).isConnected()){
        	new GetProductDetails().execute();
        }
 
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
            pDialog = new ProgressDialog(SDetails.this);
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
                        params.add(new BasicNameValuePair(ID, id));
                        
                        Log.d("parametrii", params.toString());
 
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
                                    .getJSONArray(SIMPATIZANTI); // JSON Array
                            
                            System.out.println(productObj.toString());
 
                            // get first product object from JSON Array
                            JSONObject product = productObj.getJSONObject(0);
 
                            // product with this pid found
                            // Edit Text
                            numeView = (TextView) findViewById(R.id.nume);
                            prenumeView = (TextView) findViewById(R.id.prenume);
                            emailView = (TextView) findViewById(R.id.email);
                            telefonView = (TextView) findViewById(R.id.telefon);
                            localitateView = (TextView) findViewById(R.id.localitate);
                            adresaView = (TextView) findViewById(R.id.adresa);
                            studiiView = (TextView) findViewById(R.id.studii);
                            facultateView = (TextView) findViewById(R.id.facultate);
 
                            // display product data in EditText
                            numeView.setText(product.getString(NUME));
                            prenumeView.setText(product.getString(PRENUME));
                            emailView.setText(product.getString(EMAIL));
                            telefonView.setText(product.getString(TELEFON));
                            localitateView.setText(product.getString(LOCALITATE));
                            adresaView.setText(product.getString(ADRESA));
                            studiiView.setText(product.getString(STUDII));
                            facultateView.setText(product.getString(FACULTATE));
                            
                            emailView.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									new SendEmail(SDetails.this, emailView.getText().toString());
									
								}
							});
                            
                            telefonView.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									telefonView.setOnClickListener(new OnClickListener() {
										
										@Override
										public void onClick(View v) {
											new Call(SDetails.this, telefonView.getText().toString());
											
										}
									});
									
								}
							});
 
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
 
   
}