package com.limitlessmobil.ivendgateway.util;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class Base64Encoder {

	public static String getBase64(String str) throws UnsupportedEncodingException{
		String base64encodedString = Base64.getEncoder().encodeToString(str.getBytes("utf-8"));
	         System.out.println("Base64 Encoded String (Basic) :" + base64encodedString);
	         return base64encodedString;
	}	
}
