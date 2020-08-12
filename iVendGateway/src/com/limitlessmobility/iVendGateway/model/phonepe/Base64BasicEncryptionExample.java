package com.limitlessmobility.iVendGateway.model.phonepe;

import java.util.Base64;

public class Base64BasicEncryptionExample {

	public static void main(String[] args) {
		  // Getting encoder  
        Base64.Encoder encoder = Base64.getUrlEncoder();  
        // Encoding URL  
        String eStr = encoder.encodeToString("http://www.javatpoint.com/java-tutorial/".getBytes());  
        System.out.println("Encoded URL: "+eStr);  
        // Getting decoder  
        Base64.Decoder decoder = Base64.getUrlDecoder();  
        // Decoding URl  
        String dStr = new String(decoder.decode(eStr));  
        System.out.println("Decoded URL: "+dStr);

	}

}
