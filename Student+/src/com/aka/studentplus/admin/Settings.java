package com.aka.studentplus.admin;

import com.aka.studentplus.R;
import com.aka.studentplus.Register;
import com.aka.studentplus.TnlDrawerLayout;
import com.aka.studentplus.followers.ContactSimpa;
import com.aka.studentplus.profile.ChangePassword;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class Settings extends Fragment {
	
	public Settings(){}
	
	@Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
        final Bundle savedInstanceState) {
     	
        
        View rootView = inflater.inflate(R.layout.settings, container, false);
        
        ImageView menu = (ImageView) rootView.findViewById(R.id.menu);		
		menu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TnlDrawerLayout.openMenu();
				
				
				
			}
		});
		
		RelativeLayout chgpassword = (RelativeLayout) rootView.findViewById(R.id.chgpassword);		
		chgpassword.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), ChangePassword.class);
				startActivity(intent);
				
				
				
			}
		});
		
		RelativeLayout followers = (RelativeLayout) rootView.findViewById(R.id.followers);		
		followers.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), ContactSimpa.class);
				startActivity(intent);
				
				
				
			}
		});
		
		RelativeLayout newscp = (RelativeLayout) rootView.findViewById(R.id.newscp);		
		newscp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), NewsCP.class);
				startActivity(intent);
				
				
				
			}
		});
		
		RelativeLayout add = (RelativeLayout) rootView.findViewById(R.id.addmember);		
		add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), Register.class);
				startActivity(intent);
				
				
				
			}
		});
		
		
		return rootView;

}
}
