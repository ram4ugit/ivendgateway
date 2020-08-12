package com.limitlessmobility.iVendGateway.dao;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeStamp {
	
	public static void main(String[] args) {
		
		java.util.Date today = new java.util.Date();
		
		java.util.Date date = new java.util.Date();
		java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());
		
		
	      
	      System.out.println( new java.sql.Timestamp(today.getTime()));
	      System.out.println(timestamp);
	}

}
