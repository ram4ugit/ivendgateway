package com.limitlessmobil.ivendgateway.util;

public class HexadecimaToInteger {

	public String getHexToInt(String hexValue) {
		int value = Integer.parseInt(hexValue, 16);
		return String.valueOf(value);  
	}
	
	
	
	public static void main(String[] args) {
		String s = new HexadecimaToInteger().getHexToInt("030F");
		System.out.println(s);
	}
}
