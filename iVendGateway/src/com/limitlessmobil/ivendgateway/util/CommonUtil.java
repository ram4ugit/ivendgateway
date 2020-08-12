package com.limitlessmobil.ivendgateway.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;



public class CommonUtil {
	
	public static String lowerCaseString(String s) {
		s = s.replaceAll("\\s+","");
		s = s.toLowerCase();
		return s;
	}

	public static boolean isNullOrEmpty(String str) {
        if(str != null && !str.isEmpty())
            return false;
        return true;
    }
	
	public static String getCurrentDate(){
		 DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	        Date date = new Date();
	        System.out.println(dateFormat.format(date));
	        return dateFormat.format(date);
	}
	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	        return true;
	    } catch(Exception e) {
	        return false;
	    }
	    
	}
	
	public static String sha512(String input) throws NoSuchAlgorithmException, UnsupportedEncodingException {
	    MessageDigest md5 = MessageDigest.getInstance("SHA-512");
	    byte[] digest = md5.digest(input.getBytes("UTF-8"));
	    StringBuilder sb = new StringBuilder();
	    for (int i = 0; i < digest.length; ++i) {
	        sb.append(Integer.toHexString((digest[i] & 0xFF) | 0x100).substring(1, 3));
	    }
	    return sb.toString();
	}
	
	public static String imageToBase64(String imagePath) {
		  String base64Image = "";
		  File file = new File(imagePath);
		  try (FileInputStream imageInFile = new FileInputStream(file)) {
		    // Reading a Image file from file system
		    byte imageData[] = new byte[(int) file.length()];
		    imageInFile.read(imageData);
		    base64Image = Base64.getEncoder().encodeToString(imageData);
		  } catch (FileNotFoundException e) {
		    System.out.println("Image not found" + e);
		  } catch (IOException ioe) {
		    System.out.println("Exception while reading the Image " + ioe);
		  }
		  
		  return base64Image;
		}
}
