package com.aka.studentplus.events;

import java.util.List;

import com.aka.studentplus.R;
import com.aka.studentplus.library.ImageLoader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class EventsAdapter extends BaseAdapter {
	
	private List<Eveniment> items;
	private Context context;
	
	private TextView dataView;
	private TextView titleView;
	private ImageView imageView;
	
	private ImageLoader imageLoader;
	
	public EventsAdapter(Context context, List<Eveniment> items){
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
	public Eveniment getItem(int arg0) {
		return items.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
			if(view == null){
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.news_list_item, null);
			}
		 
	        
	        titleView = (TextView) view.findViewById(R.id.headline);
	        dataView = (TextView) view.findViewById(R.id.timestamp);
	        imageView = (ImageView) view.findViewById(R.id.image);
	        
	        final Eveniment ev = items.get(position);
	        
	        titleView.setText(ev.getTitle());
	        dataView.setText(ev.getDate());
	        
	        imageLoader.DisplayImage(ev.getImage(), R.drawable.temp_img, imageView);
	        
	        view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, ViewEvent.class);
					intent.putExtra("title", ev.getTitle());
					intent.putExtra("date", ev.getDate());
					intent.putExtra("content", ev.getInfo());
					intent.putExtra("image", ev.getImage());
					context.startActivity(intent);
				}
			});
	        
	        
		return view;
	}

}
