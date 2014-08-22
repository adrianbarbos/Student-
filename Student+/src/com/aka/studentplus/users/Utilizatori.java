package com.aka.studentplus.users;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.aka.studentplus.R;
import com.aka.studentplus.TnlDrawerLayout;
import com.aka.studentplus.admin.JSONParser;
import com.aka.studentplus.library.ConnectionDetector;

public class Utilizatori extends Fragment {

	public Utilizatori() {
	}

	ListView list;
	LinearLayout searchBtn;
	EditText searchBox;

	List<Utilizator> items = new ArrayList<Utilizator>();
	UtilAdapter adapter;
	
	SortUsers sort;

	private static Utilizator utilizator;

	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.view_utilizatori, container,
				false);
		ImageView menu = (ImageView) rootView.findViewById(R.id.menu);
		menu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TnlDrawerLayout.openMenu();

			}
		});

		list = (ListView) rootView.findViewById(R.id.list);
		searchBtn = (LinearLayout) rootView.findViewById(R.id.search);
		searchBox = (EditText) rootView.findViewById(R.id.search_box);

		searchBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (searchBox.isShown()) {
					searchBox.setVisibility(View.GONE);
				} else {
					searchBox.setVisibility(View.VISIBLE);
				}

			}
		});
		
		setSearchBox(searchBox);

		if (new ConnectionDetector(getActivity()).isConnected()) {
			new GetUtil().execute();
		}
		return rootView;
	}

	private void setAdapter() {
		adapter = new UtilAdapter(getActivity(), items);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				setUtilizator(items.get(position));
				Intent intent = new Intent(getActivity(), UtilDetalii.class);
				startActivity(intent);

			}
		});
	}

	private class GetUtil extends AsyncTask<String, String, String> {

		private static final String URL = "http://deltamg.zz9.us/www/members/get_users.php";
		private static final String NAME = "firstname";
		private static final String SURNAME = "lastname";
		private static final String ADDRESS = "address";
		private static final String PHONE = "phone";
		private static final String EMAIL = "email";
		private static final String COLLEGE = "college";
		private static final String UNIVERSITY = "university";
		private static final String DEPARTMENT = "department";
		private static final String IMAGE = "image";
		private static final String TYPE = "type";
		private static final String BIRTHDAY = "birthday";
		private static final String USERS = "users";

		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(getActivity());
			dialog.setMessage("Loading...");
			dialog.setCancelable(true);
			dialog.setIndeterminate(false);
			dialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = jsonParser.makeHttpRequest(URL, "POST",
					parameters);
			try {
				JSONArray jsonArray = jsonObject.getJSONArray(USERS);

				for (int i = 0; i < jsonArray.length(); i++) {
					jsonObject = jsonArray.getJSONObject(i);
					String name = jsonObject.getString(NAME);
					String surname = jsonObject.getString(SURNAME);
					String address = jsonObject.getString(ADDRESS);
					String phone = jsonObject.getString(PHONE);
					String email = jsonObject.getString(EMAIL);
					String college = jsonObject.getString(COLLEGE);
					String university = jsonObject.getString(UNIVERSITY);
					String type = jsonObject.getString(TYPE);
					String department = jsonObject.getString(DEPARTMENT);
					String image = jsonObject.getString(IMAGE);
					String birthday = jsonObject.getString(BIRTHDAY);

					Utilizator utilizator = new Utilizator(name, surname, type,
							email, phone, university, college, department,
							address, image, birthday);

					items.add(utilizator);

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
			setAdapter();
		}

	}

	public static void setUtilizator(Utilizator utilizator) {
		Utilizatori.utilizator = utilizator;
	}

	public static Utilizator getUtilizator() {
		return utilizator;
	}
	
	private void setSearchBox(EditText searchBox){
		sort = new SortUsers();
		searchBox.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {
				String name = s.toString();
				if (name.length() == 0){
					setAdapter();
					return;
				}
				sort = new SortUsers();
				sort.setName(name);
				sort.execute();
				
				
			}
		});
	}
	
	private class SortUsers extends AsyncTask<String, String, String>{
		
		private String name;
		private List<Utilizator> sortList = new ArrayList<Utilizator>();
		
		public void setName(String name){
			this.name = name;
			sortList = new ArrayList<Utilizator>();
		}

		@Override
		protected String doInBackground(String... params) {
			for(int i = 0; i < items.size(); i++){
				Utilizator utilizator = items.get(i);
				String fullName = utilizator.getName() + " " + utilizator.getSurname();
				if(Pattern.compile(Pattern.quote(name), Pattern.CASE_INSENSITIVE).matcher(fullName).find()){
					sortList.add(utilizator);
				}
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			adapter = new UtilAdapter(getActivity(), sortList);
			list.setAdapter(adapter);
			list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View view, int position,
						long arg3) {
					setUtilizator(sortList.get(position));
					Intent intent = new Intent(getActivity(), UtilDetalii.class);
					startActivity(intent);
					
				}
			});
		}
		
	}
	
}
