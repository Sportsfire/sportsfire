package com.sportsfire;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

public class Player implements Parcelable{
    private String firstName;
    private String lastName;
    private String id;
    public Player(String _firstName,String _lastName, String _id, Context context){
        firstName = _firstName;
        lastName = _lastName;
        id = _id;
    }
   
    public Player(Parcel in){
    	readFromParcel(in);
    }
    
    public String getFirstName(){
        return firstName;
    }
    
    public String getLastName(){
        return lastName;
    }
    
  
    public String getName(){
        return lastName + ", " + firstName;
    }
    public String getID(){
    	return id;
    }

	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(firstName);
		dest.writeString(lastName);
		dest.writeString(id);
	}
	
	private void readFromParcel(Parcel in){
		firstName = in.readString();
		lastName = in.readString();
		id = in.readString();
	}
	
	public void setContext(Context context){
	}
	
   public static final Parcelable.Creator<Player> CREATOR =
   	new Parcelable.Creator<Player>() {
           public Player createFromParcel(Parcel in) {
               return new Player(in);
           }

           public Player[] newArray(int size) {
               return new Player[size];
           }
       };
}