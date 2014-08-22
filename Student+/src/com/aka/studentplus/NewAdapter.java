package com.aka.studentplus;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


@SuppressWarnings("unchecked")
public class NewAdapter extends BaseExpandableListAdapter {

	public ArrayList<String> groupItem, tempChild;
	public int[] tempImages;
	public ArrayList<Object> Childtem = new ArrayList<Object>();
	public LayoutInflater minflater;
	public Activity activity;
	TextView grup;
	TextView sub;
	ImageView img;
	private ArrayList<int[]> imgs = new ArrayList<int[]>();
	private int[] images1 = {
			R.drawable.drumuri,
			R.drawable.drumuri,
			R.drawable.drumuri,
			R.drawable.drumuri};
	private int[] images2 = {
			R.drawable.drumuri,
			R.drawable.drumuri,
			R.drawable.drumuri,
			R.drawable.drumuri};
	private int[] images3 = {
			R.drawable.drumuri,
			R.drawable.violenta,
			R.drawable.salubrizare,
			R.drawable.petitie,
			R.drawable.alteprob};
	private int[] images4 = {
			R.drawable.drumuri,
			R.drawable.drumuri,
			R.drawable.drumuri,
			R.drawable.drumuri};
	
	private final Context context;

	public NewAdapter(Context context,ArrayList<String> grList, ArrayList<Object> childItem) {
		this.context = context;
		groupItem = grList;
		this.Childtem = childItem;
		imgs.add(images1);
		imgs.add(images2);
		imgs.add(images3);
		imgs.add(images4);
	}

	public void setInflater(LayoutInflater mInflater, Activity act) {
		this.minflater = mInflater;
		activity = act;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}


	@Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {
        tempChild = (ArrayList<String>) Childtem.get(groupPosition);
        tempImages = imgs.get(groupPosition);

        if (convertView == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.drawer_submenu_item,parent,false);
        }

        sub = (TextView) convertView.findViewById(R.id.sub);
        sub.setText(tempChild.get(childPosition));
        img = (ImageView) convertView.findViewById(R.id.image);
         
        img.setImageResource(tempImages[childPosition]);

        sub.setTag(tempChild.get(childPosition));
        convertView.setOnClickListener(new OnClickListener() {
        	
			@Override
			public void onClick(View v) {
				switch(groupPosition){
				case 0 : 
					grupa0(childPosition);
					break;
				case 1 :
					grupa1(childPosition);
					break;
				case 2 :
					grupa2(childPosition);
					break;
				case 3 : 
					grupa3(childPosition);
					break;
				default: break;
				}
				
			 	
			}
		});
        
        return convertView;
}

	

	@Override
	public int getChildrenCount(int groupPosition) {
		return ((ArrayList<String>) Childtem.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return null;
	}

	@Override
	public int getGroupCount() {
		return groupItem.size();
	}

	@Override
	public void onGroupCollapsed(int groupPosition) {
		super.onGroupCollapsed(groupPosition);
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		super.onGroupExpanded(groupPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	@Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.drawer_group_item,parent,false);
        }

        grup = (TextView) convertView.findViewById(R.id.group);
        grup.setText(groupItem.get(groupPosition));
        grup.setTag(groupItem.get(groupPosition));
        
        return convertView;
    }
	
	

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
	
	private void grupa0(int position){
		switch(position){
		case 0 : Toast.makeText(context, "10", Toast.LENGTH_SHORT).show();
		break;
		case 1 : Toast.makeText(context, "2", Toast.LENGTH_SHORT).show();
		break;
		case 2 : Toast.makeText(context, "3", Toast.LENGTH_SHORT).show();
		break;
		case 3 : Toast.makeText(context, "4", Toast.LENGTH_SHORT).show();
		break;
		default : break;		
		}
	}
	
	private void grupa1(int position){
		switch(position){
		case 0 : Toast.makeText(context, "5", Toast.LENGTH_SHORT).show();
		break;
		case 1 : Toast.makeText(context, "6", Toast.LENGTH_SHORT).show();
		break;
		case 2 : Toast.makeText(context, "7", Toast.LENGTH_SHORT).show();
		break;
		case 3 : Toast.makeText(context, "8", Toast.LENGTH_SHORT).show();
		break;
		default : break;		
		}
	}
	
	private void grupa2(int position){
		switch(position){
		case 0 : Toast.makeText(context, "10", Toast.LENGTH_SHORT).show();
		break;
		case 1 : Toast.makeText(context, "10", Toast.LENGTH_SHORT).show();
		break;
		case 2 : Toast.makeText(context, "11", Toast.LENGTH_SHORT).show();
		break;
		case 3 : Toast.makeText(context, "12", Toast.LENGTH_SHORT).show();
		break;
		case 4 : Toast.makeText(context, "13", Toast.LENGTH_SHORT).show();
		default : break;		
		}
	}
	
	private void grupa3(int position){
		switch(position){
		case 0 : Toast.makeText(context, "14", Toast.LENGTH_SHORT).show();
		break;
		case 1 : Toast.makeText(context, "15", Toast.LENGTH_SHORT).show();
		break;
		case 2 : Toast.makeText(context, "16", Toast.LENGTH_SHORT).show();
		break;
		case 3 : Intent intent = new Intent(context, Tutorial.class);
				 context.startActivity(intent);
				 ((Activity) context).finish();
		break;
		default : break;		
		}
	}


	
}
