package com.aka.studentplus.events;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Sort {

	List<Eveniment> items;
	
	public Sort(List<Eveniment> items){
		super();
		this.items = items;
	}
	
	public List<Eveniment> sortByThisMonth(){
		Calendar c = Calendar.getInstance();
		int month = c.get(Calendar.MONTH);
		int year = c.get(Calendar.YEAR);		
		return sortByMonth(month, year);
	}
	
	public List<Eveniment> sortByMonth(int month, int year){
		List<Eveniment> monthList = new ArrayList<Eveniment>();
		month++;
		for(int i = 0; i < items.size(); i++){
			Eveniment ev = items.get(i);
			if(month == ev.getMonth()
					&& year == ev.getYear()){
				monthList.add(ev);
			}
		}
		
		return monthList;
		
	}
	
}
