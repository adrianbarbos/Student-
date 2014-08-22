package com.aka.studentplus.admin;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.aka.studentplus.R;

public class AddNews extends Activity {

	// image upload
	Button btnselectpic;
	ImageView imageview;
	int serverResponseCode = 0;
	ProgressDialog dialog = null;

	private String upLoadServerUri = null;
	private String imagepath = null;
	private String imageName;

	// Progress Dialog
	private ProgressDialog pDialog;

	JSONParser jsonParser = new JSONParser();
	EditText inputHeadline;
	EditText inputDesc;
	
	LinearLayout back;

	// url to create new product
	private static String url_create_product = "http://deltamg.zz9.us/www/news/api/story_add.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addnews);
		
		//imageupload
        btnselectpic = (Button)findViewById(R.id.button_selectpic);
        imageview = (ImageView)findViewById(R.id.imageView_pic);
        
        back = (LinearLayout) findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
        
        btnselectpic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
	            intent.setType("image/*");
	            intent.setAction(Intent.ACTION_GET_CONTENT);
	            startActivityForResult(Intent.createChooser(intent, "Complete action using"), 1);
				
			}
		});
       
        upLoadServerUri = "http://deltamg.zz9.us/www/news/api/upload.php";

		// Edit Text
		inputHeadline = (EditText) findViewById(R.id.inputHeadline);
		inputDesc = (EditText) findViewById(R.id.inputDesc);

		// Create button
		Button btnCreateProduct = (Button) findViewById(R.id.btnCreateNews);

		// button click event
		btnCreateProduct.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				// creating new product in background thread
				 new Thread(new Runnable() {
	                 public void run() {
	                                     
	                      uploadFile(imagepath);
	                                              
	                 }
	               }).start(); 

		            pDialog = new ProgressDialog(AddNews.this);
		            pDialog.setMessage("Loading...");
		            pDialog.setIndeterminate(false);
		            pDialog.setCancelable(false);
		            pDialog.show();
				 
				 try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				 
				new CreateNewProduct().execute();
				
				pDialog.dismiss();
			}
		});
	}

	/**
	 * Background Async Task to Create new product
	 * */
	class CreateNewProduct extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(AddNews.this);
			pDialog.setMessage("Loading..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Creating product
		 * */
		protected String doInBackground(String... args) {
			String headline = inputHeadline.getText().toString();
			String story = inputDesc.getText().toString();
			String image = imageName;
			

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("headline", headline));
			params.add(new BasicNameValuePair("story", story));
			params.add(new BasicNameValuePair("image", image));

			// getting JSON Object
			// Note that create product url accepts POST method
			JSONObject json = jsonParser.makeHttpRequest(url_create_product,
					"POST", params);


			// check for success tag
			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// successfully created product
					Intent i = new Intent(AddNews.this,
							ViewNews.class);
					startActivity(i);

					// closing this screen
					finish();
				} else {
					// failed to create product
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
			// dismiss the dialog once done
			pDialog.dismiss();
		}

	}
	
	//image upload stuff
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    
    	if (requestCode == 1 && resultCode == RESULT_OK) {
            //Bitmap photo = (Bitmap) data.getData().getPath(); 
          
            Uri selectedImageUri = data.getData();
            imagepath = getPath(selectedImageUri);
            Bitmap bitmap=BitmapFactory.decodeFile(imagepath);
            imageview.setImageBitmap(bitmap);
	    	
	    }
    }
    	 public String getPath(Uri uri) {
    	        String[] projection = { MediaStore.Images.Media.DATA };
				@SuppressWarnings("deprecation")
				Cursor cursor = managedQuery(uri, projection, null, null, null);
    	        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
    	        cursor.moveToFirst();
    	        return cursor.getString(column_index);
    	    }
	    
    public int uploadFile(String sourceFileUri) {
          
    	  
    	  String fileName = sourceFileUri;
 
          HttpURLConnection conn = null;
          DataOutputStream dos = null;  
          String lineEnd = "\r\n";
          String twoHyphens = "--";
          String boundary = "*****";
          int bytesRead, bytesAvailable, bufferSize;
          byte[] buffer;
          int maxBufferSize = 1 * 1024 * 1024; 
          boolean isPictureTaken = (sourceFileUri != null);
          File sourceFile = null;
          if(isPictureTaken){
        	  sourceFile = new File(sourceFileUri); 
          }
          
          if (!isPictureTaken) {
        	  
	           
	           Log.e("uploadFile", "Source File not exist :"+imagepath);
	           
	           runOnUiThread(new Runnable() {
	               public void run() {
	               }
	           }); 
	           
	           return 0;
           
          }
          else
          {
	           try { 
	        	   
	            	 // open a URL connection to the Servlet
	               FileInputStream fileInputStream = new FileInputStream(sourceFile);
	               URL url = new URL(upLoadServerUri);
	               
	               // Open a HTTP  connection to  the URL
	               conn = (HttpURLConnection) url.openConnection(); 
	               conn.setDoInput(true); // Allow Inputs
	               conn.setDoOutput(true); // Allow Outputs
	               conn.setUseCaches(false); // Don't use a Cached Copy
	               conn.setRequestMethod("POST");
	               conn.setRequestProperty("Connection", "Keep-Alive");
	               conn.setRequestProperty("ENCTYPE", "multipart/form-data");
	               conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
	               conn.setRequestProperty("uploaded_file", fileName); 
	               
	               dos = new DataOutputStream(conn.getOutputStream());
	     
	               dos.writeBytes(twoHyphens + boundary + lineEnd); 
	               dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
	            		                     + fileName + "\"" + lineEnd);
	               
	               dos.writeBytes(lineEnd);
	     
	               // create a buffer of  maximum size
	               bytesAvailable = fileInputStream.available(); 
	     
	               bufferSize = Math.min(bytesAvailable, maxBufferSize);
	               buffer = new byte[bufferSize];
	     
	               // read file and write it into form...
	               bytesRead = fileInputStream.read(buffer, 0, bufferSize);  
	                 
	               while (bytesRead > 0) {
	            	   
	                 dos.write(buffer, 0, bufferSize);
	                 bytesAvailable = fileInputStream.available();
	                 bufferSize = Math.min(bytesAvailable, maxBufferSize);
	                 bytesRead = fileInputStream.read(buffer, 0, bufferSize);   
	                 
	                }
	     
	               // send multipart form data necesssary after file data...
	               dos.writeBytes(lineEnd);
	               dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
	     
	               // Responses from the server (code and message)
	               serverResponseCode = conn.getResponseCode();
	               
	               String[] x = fileName.split("/");
	               
	               imageName = x[x.length - 1];
	                
	               
	               if(serverResponseCode == 200){
	            	   
	                   runOnUiThread(new Runnable() {
	                        public void run() {
	              
	                        }
	                    });                
	               }    
	               
	               //close the streams //
	               fileInputStream.close();
	               dos.flush();
	               dos.close();
	                
	          } catch (MalformedURLException ex) {
	        	  
	              dialog.dismiss();  
	              ex.printStackTrace();
	              
	              runOnUiThread(new Runnable() {
	                  public void run() {
	                	 
	                  }
	              });
	              
	          } catch (Exception e) {
	        	  
	              
	              e.printStackTrace();
	              
	              runOnUiThread(new Runnable() {
	                  public void run() {
	                  }
	              });
	          }
	          
	          return serverResponseCode; 
	          
           } // End else block 
         }
}