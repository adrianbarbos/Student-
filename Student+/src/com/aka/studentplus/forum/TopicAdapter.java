package com.aka.studentplus.forum;

import java.util.List;

import com.aka.studentplus.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TopicAdapter extends BaseAdapter {
	
	private List<Topic> items;
	private Context context;
	private TextView subiect;
	private TextView timeStamp;
	private TextView creator;
	
	
	public TopicAdapter(Context context, List<Topic> items){
		this.context = context;
		this.items = items;
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Topic getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {

		if(view == null){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);			
			view = inflater.inflate(R.layout.topic_list_item, null);
		}
		subiect = (TextView) view.findViewById(R.id.topic);
		creator = (TextView) view.findViewById(R.id.creator);
		timeStamp = (TextView) view.findViewById(R.id.timestamp);
		
		subiect.setText(items.get(position).getSubiect());
		timeStamp.setText(items.get(position).getTimeStamp().split(" ")[0]);
		creator.setText(items.get(position).getCreator());
		
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Topic topic = items.get(position);
				Topic.setTopic(topic);
				Intent intent = new Intent(context, Forum.class);
				context.startActivity(intent);
				
			}
		});
		return view;
	}

}
