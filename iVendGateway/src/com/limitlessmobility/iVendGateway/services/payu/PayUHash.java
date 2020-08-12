package com.limitlessmobility.iVendGateway.services.payu;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PayUHash {

	public static String getHash(String input) throws NoSuchAlgorithmException, UnsupportedEncodingException {
	    MessageDigest md5 = MessageDigest.getInstance("SHA-512");
	    byte[] digest = md5.digest(input.getBytes("UTF-8"));
	    StringBuilder sb = new StringBuilder();
	    for (int i = 0; i < digest.length; ++i) {
	        sb.append(Integer.toHexString((digest[i] & 0xFF) | 0x100).substring(1, 3));
	    }
	    return sb.toString();
	}
}
