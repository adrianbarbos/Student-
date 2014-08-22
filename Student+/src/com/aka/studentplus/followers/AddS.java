package com.aka.studentplus.followers;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.aka.studentplus.R;
import com.aka.studentplus.admin.JSONParser;
 
public class AddS extends Activity {
 
    // Progress Dialog
    private ProgressDialog pDialog;
 
    JSONParser jsonParser = new JSONParser();
	EditText inputname;
    EditText inputsurname;
    EditText inputemail;
    EditText inputtel;
    AutoCompleteTextView inputlocalitate;
    EditText inputadresa;
    Spinner inputstudii;
    Spinner inputfacultate;
    String[] orase;
 
    // url to create new product
    private static String url_create_product = "http://deltamg.zz9.us/www/simpa/api/simpa_add.php";
 
    // JSON Node names
    @SuppressWarnings("unused")
	private static final String TAG_SUCCESS = "success";
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.addsimpatizanti);
 
        // Edit Text
        inputname = (EditText) findViewById(R.id.nume);
        inputsurname = (EditText) findViewById(R.id.prenume);
        inputemail = (EditText) findViewById(R.id.email);
        inputtel = (EditText) findViewById(R.id.telefon);

        inputlocalitate = (AutoCompleteTextView) findViewById(R.id.localitate);     
        
        inputadresa = (EditText) findViewById(R.id.adresa);
        
        inputstudii = (Spinner) findViewById(R.id.studii);
        inputfacultate = (Spinner) findViewById(R.id.facultate);
        
        LinearLayout back = (LinearLayout) findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});

        
        
        orase = getResources().getStringArray(R.array.localitati_maramures);
        inputlocalitate.setAdapter(new ArrayAdapter<String>(AddS.this, android.R.layout.simple_dropdown_item_1line, orase));
 
        // Create button
        Button btnCreateProduct = (Button) findViewById(R.id.add);
 
        // button click event
        btnCreateProduct.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View view) {
                // creating new product in background thread
                new CreateNewProduct().execute();
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
            pDialog = new ProgressDialog(AddS.this);
            pDialog.setMessage("Adaugare..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
        /**
         * Creating product
         * */
        String[] studiilist = getResources().getStringArray(R.array.studii);
        String[] facultatelist = getResources().getStringArray(R.array.university);
        
        protected String doInBackground(String... args) {
            String name = inputname.getText().toString();
            String surname = inputsurname.getText().toString();
            String email = inputemail.getText().toString();
            String tel = inputtel.getText().toString();
            String localitate = inputlocalitate.getText().toString();
            String adresa = inputadresa.getText().toString();
            String studii = studiilist[inputstudii.getSelectedItemPosition()];
            String facultate = facultatelist[inputfacultate.getSelectedItemPosition()];
 
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("nume", name));
            params.add(new BasicNameValuePair("prenume", surname));
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("tel", tel));
            params.add(new BasicNameValuePair("localitate", localitate));
            params.add(new BasicNameValuePair("adresa", adresa));
            params.add(new BasicNameValuePair("studii", studii));
            params.add(new BasicNameValuePair("facultate", facultate));
 
            // getting JSON Object
            // Note that create product url accepts POST method
            jsonParser.makeHttpRequest(url_create_product,"POST", params);
 
          
 
           
            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
            ViewS.LoadAllProducts x;
            x = new ViewS.LoadAllProducts();
            x.execute();
            finish();
        }
 
    }
}