package com.aka.studentplus.forum;

import java.util.List;

import com.aka.studentplus.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ForumAdapter extends BaseAdapter {
	
	private Context context;
	private List<Message> items;	
	private TextView textView;
	private TextView userView;
	private TextView timeView;

	public ForumAdapter(Context context, List<Message> items) {
		super();
		this.context = context;
		this.items = items;
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Message getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		Message msg = items.get(position);
		
		if(view == null){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			if(msg.isMyMessage()){
				view = inflater.inflate(R.layout.my_message, null);
			}else{
				view = inflater.inflate(R.layout.other_message, null);
			}
			textView = (TextView) view.findViewById(R.id.message);
			userView = (TextView) view.findViewById(R.id.user);
			timeView = (TextView) view.findViewById(R.id.timestamp);
			textView.setText(msg.getMessage());
			userView.setText(msg.getNume());
			timeView.setText(msg.getTimeStamp());
		}
		return view;
	}

}
