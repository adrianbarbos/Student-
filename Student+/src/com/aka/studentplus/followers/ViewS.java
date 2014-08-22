package com.aka.studentplus.followers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.aka.studentplus.R;
import com.aka.studentplus.TnlDrawerLayout;
import com.aka.studentplus.admin.JSONParser;
 
public class ViewS extends Fragment {
	
	public ViewS(){}
 
    // Progress Dialog
    private static ListView lv;
 
    
 
    private static ArrayList<HashMap<String, String>> newsList;
 
    // url to get all products list
    private static String url_all_products = "http://deltamg.zz9.us/www/simpa/api/get_simpa.php";
 
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String SIMPATIZANTI = "simpatizanti";
    private static final String ID = "id";
    private static final String NAME = "nume";
    private static final String SURNAME = "prenume";
 
    
    private static Context context;
    
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
        final Bundle savedInstanceState) {
     	
        
        View rootView = inflater.inflate(R.layout.viewsimpatizanti, container, false);
        
        context = getActivity();
        
        ImageView menu = (ImageView) rootView.findViewById(R.id.menu);		
		menu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TnlDrawerLayout.openMenu();
				
				
				
			}
		});
		
		ImageView add = (ImageView) rootView.findViewById(R.id.add);
		add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), AddS.class);
				startActivity(intent);
				
			}
		});
 
    
        
        
 
        // Hashmap for ListView
        newsList = new ArrayList<HashMap<String, String>>();
 
        // Loading products in Background Thread
        new LoadAllProducts().execute();
 
        // Get listview
        lv = (ListView) rootView.findViewById(android.R.id.list);
 
        // on seleting single product
        // launching Edit Product Screen
        lv.setOnItemClickListener(new OnItemClickListener() {
 
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                // getting values from selected ListItem
                String pid = ((TextView) view.findViewById(R.id.id)).getText()
                        .toString();
 
                // Starting new intent
                Intent in = new Intent(getActivity(),
                        SDetails.class);
                // sending pid to next activity
                in.putExtra(ID, pid);
 
                // starting new activity and expecting some response back
                startActivityForResult(in, 100);
            }
        });
		return rootView;
 
    }
 
    // Response from Edit Product Activity
    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if result code 100
        if (resultCode == 100) {
            // if result code 100 is received
            // means user edited/deleted product
            // reload this screen again
            Intent intent = getActivity().getIntent();
            startActivity(intent);
        }
 
    }
 
    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    public static class LoadAllProducts extends AsyncTask<String, String, String> {

        private ProgressDialog pDialog;
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
        	// Hashmap for ListView
            newsList = new ArrayList<HashMap<String, String>>();
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
         // Creating JSON Parser object
            JSONParser jParser = new JSONParser();
            JSONObject json = jParser.makeHttpRequest(url_all_products, "POST", params);
 
            // Check your log cat for JSON reponse
            Log.d("All Products: ", json.toString());
 
            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);
 
                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    JSONArray news = json.getJSONArray(SIMPATIZANTI);
 
                    // looping through All Products
                    for (int i = 0; i < news.length(); i++) {
                        JSONObject c = news.getJSONObject(i);
 
                        // Storing each json item in variable
                        String id = c.getString(ID);
                        String name = c.getString(NAME);
                        String surname = c.getString(SURNAME);
 
                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();
 
                        // adding each child node to HashMap key => value
                        map.put(ID, id);
                        map.put(NAME, name);
                        map.put(SURNAME, surname);
 
                        // adding HashList to ArrayList
                        newsList.add(map);
                    }
                } else {
                    // no products found
                    // Launch Add New product Activity
         
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
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
           
                    ListAdapter adapter = new SimpleAdapter(
                            context, newsList,
                            R.layout.simpatizanti_list_item, new String[] { ID,
                                    NAME, SURNAME},
                            new int[] { R.id.id, R.id.nume, R.id.prenume });
                    // updating listview
                    lv.setAdapter(adapter);
                
            
 
        }
 
    }
    
    public static void refreshListAfterAdd(){
    }
    
}