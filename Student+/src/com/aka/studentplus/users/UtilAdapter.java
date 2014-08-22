package com.aka.studentplus.users;

import java.util.List;

import com.aka.studentplus.R;
import com.aka.studentplus.library.ImageLoader;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class UtilAdapter extends BaseAdapter {
	
	private List<Utilizator> items;
	private TextView nameView;
	private TextView typeView;
	private ImageView imageView;
	private Context context;
	private ImageLoader imageLoader;
	
	public UtilAdapter(Context context, List<Utilizator> items){
		super();
		this.context = context;
		this.items = items;
		this.imageLoader = new ImageLoader(context);
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Utilizator getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if(view == null){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.utilizator_list_item, null);
		}
		
		Utilizator utilizator = items.get(position);
		imageView = (ImageView) view.findViewById(R.id.image);
		nameView = (TextView) view.findViewById(R.id.name);
		typeView = (TextView) view.findViewById(R.id.statut);
		
		nameView.setText(utilizator.getName() + " " + utilizator.getSurname());
		typeView.setText(utilizator.getType());
		imageLoader.DisplayImage(utilizator.getImage(), R.drawable.temp_img, imageView);
		return view;
	}

}
