package com.aka.studentplus.news;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.ProgressDialog;
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
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.aka.studentplus.R;
import com.aka.studentplus.TnlDrawerLayout;
import com.aka.studentplus.admin.JSONParser;
import com.aka.studentplus.library.ConnectionDetector;
 
public class News extends Fragment {
	

	public News(){}
	
	TextView back;
	ListView lv;
 
    // Progress Dialog
    private ProgressDialog pDialog;
 
    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();
 
    ArrayList<HashMap<String, String>> newsList;
 
    // url to get all products list
    private static String url_all_products = "http://deltamg.zz9.us/www/news/api/get_all_story.php";
 
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_NEWS = "news";
    private static final String TAG_PID = "id";
    private static final String TAG_NAME = "headline";
    private static final String TAG_DATE = "timestamp";
    private static final String TAG_IMAGE = "image";
 
    // products JSONArray
    JSONArray news = null;
 
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
        final Bundle savedInstanceState) {
     	
        
        View rootView = inflater.inflate(R.layout.main, container, false);
        
        ImageView menu = (ImageView) rootView.findViewById(R.id.menu);		
		menu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TnlDrawerLayout.openMenu();
				
				
				
			}
		});
		
		LinearLayout refresh = (LinearLayout) rootView.findViewById(R.id.refresh);
		refresh.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(new ConnectionDetector(getActivity()).isConnected()){
					new LoadNews().execute();
				}
				
			}
		});
		
        
        /*back = (TextView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
        
        XmlResourceParser bk = getResources().getXml(R.drawable.back);
        try {
			ColorStateList csl = ColorStateList.createFromXml(getResources(), bk);
			back.setTextColor(csl);
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        */
 
        // Loading products in Background Thread
        new LoadNews().execute();
 
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
                        NewsDetails.class);
                // sending pid to next activity
                in.putExtra(TAG_PID, pid);
 
                // starting new activity and expecting some response back
                startActivityForResult(in, 100);
            }
        });
		return rootView;
 
    }
 

 
    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class LoadNews extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
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
            JSONObject json = jParser.makeHttpRequest(url_all_products, "GET", params);
 
            // Check your log cat for JSON reponse
            Log.d("All Products: ", json.toString());
 
            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);
 
                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    news = json.getJSONArray(TAG_NEWS);
 
                    // looping through All Products
                    for (int i = 0; i < news.length(); i++) {
                        JSONObject c = news.getJSONObject(i);
 
                        // Storing each json item in variable
                        String id = c.getString(TAG_PID);
                        String name = c.getString(TAG_NAME);
                        String date = c.getString(TAG_DATE);
                        String image = c.getString(TAG_IMAGE);
 
                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();
 
                        // adding each child node to HashMap key => value
                        
                        // convert yyyy-mm-dd hh.....   to dd luna yyyy
                        date = date.split(" ")[0];
                        String[] x = date.split("-");
                        String[] months = getResources().getStringArray(R.array.months);
                        String luna = months[Integer.parseInt(x[1])];
                        
                        date = x[2] + " " + luna + " " + x[0];
                        map.put(TAG_PID, id);
                        map.put(TAG_NAME, name);
                        map.put(TAG_DATE, date);
                        map.put(TAG_IMAGE, image);
 
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
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new Adapter(
                            getActivity(), newsList,
                            R.layout.news_list_item, new String[] { TAG_PID,
                                    TAG_NAME, TAG_DATE, TAG_IMAGE},
                            new int[] { R.id.id, R.id.headline, R.id.timestamp, R.id.image });
                    // updating listview
                    lv.setAdapter(adapter);
                }
            });
 
        }
 
    }
}