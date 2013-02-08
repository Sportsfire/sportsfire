package com.sportsfire.screening;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class FormValues implements Parcelable{
	private List<List<String>> values;
	public FormValues(List<List<String>> values){
		this.values = values;
	}
	
	public List<String> getHeader(){
		return values.get(0);
	}
	public List<List<String>> getValues(){
		return values;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
	}

}
