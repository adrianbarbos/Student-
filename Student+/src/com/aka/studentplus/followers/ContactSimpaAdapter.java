package com.aka.studentplus.followers;

import java.util.ArrayList;
import java.util.List;

import com.aka.studentplus.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class ContactSimpaAdapter extends BaseAdapter {
	
	private Context context;
	private List<Simpatizant> items = new ArrayList<Simpatizant>();	
	
	private CheckBox select;	
	private TextView numeView;	
	private TextView prenumeView;	
	
	public ContactSimpaAdapter(Context context, List<Simpatizant> items) {
		super();
		this.context = context;
		this.items = items;
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Simpatizant getItem(int arg0) {
		return items.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {		
		if(view == null){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.contact_simpa_list_item, null);
		}
		
		select = (CheckBox) view.findViewById(R.id.select);	
		numeView = (TextView) view.findViewById(R.id.nume);	
		prenumeView = (TextView) view.findViewById(R.id.prenume);	
		
		Simpatizant curent = items.get(position);	
		
		select.setChecked(true);	
		numeView.setText(curent.getNume() + " ");	
		prenumeView.setText(curent.getPrenume() + " ");	
		
		return view;
	}

}
