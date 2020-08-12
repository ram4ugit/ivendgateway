package com.limitlessmobility.iVendGateway.services.ccavenue;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import com.limitlessmobility.iVendGateway.services.paytm.PayTmCallbackService;
import com.paytm.pg.merchant.CheckSumServiceHelper;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import javax.xml.bind.DatatypeConverter;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import com.ccavenue.security.AesCryptUtil;


@Controller
public class ccavenueAPI {

	private static final Logger logger = Logger.getLogger(PayTmCallbackService.class);
	private static String wsURL="https://login.ccavenue.ae/apis/servlet/DoWebTrans";
	                             
    
	/**
	 * Method to call ccavenue api. Output is printed on console within this method.
	 * */
	public void callCCavenueApi(String pXmlData,String pAccessCode,String pCommand,String aesKey,String pRequestType,String pResponseType, String pVersion){
		String vResponse="",encXMLData="",encResXML="",decXML="";
		StringBuffer wsDataBuff=new StringBuffer();
		try {
			if(aesKey!=null && !aesKey.equals("") && pXmlData!=null && !pXmlData.equals("")){
				AesUtil aesUtil=new AesUtil(aesKey);
				encXMLData  = aesUtil.encrypt(pXmlData);
			}
			wsDataBuff.append("enc_request="+encXMLData+"&access_code="+pAccessCode+"&command="+pCommand+"&response_type="+pResponseType+"&request_type="+pRequestType+"&version="+pVersion);
			vResponse = processUrlConnectionReq(wsDataBuff.toString(), wsURL);
			if(vResponse!=null && !vResponse.equals("")){
				Map hm=tokenizeToHashMap(vResponse, "&", "=");
				encResXML = hm.containsKey("enc_response")?hm.get("enc_response").toString():"";
				String vStatus = hm.containsKey("status")?hm.get("status").toString():"";
				String vError_code = hm.containsKey("enc_error_code")?hm.get("enc_error_code").toString():"";
				if(vStatus.equals("1")){//If Api call failed
					System.out.println("enc_response : "+ encResXML);
					System.out.println("error_code : "+ vError_code);
					return;
				}
				if(!encResXML.equals("")) {
					AesUtil aesUtil=new AesUtil(aesKey);
					decXML = aesUtil.decrypt(encResXML);
					System.out.println("enc_response : "+decXML);					
					return;
				}
			}
		}
		catch (Exception e) {
			System.out.println("enc_response : "+ e.getMessage());
		}
	}
	
	public static String processUrlConnectionReq(String pBankData,String pBankUrl) throws Exception{
		URL	vUrl = null;
		URLConnection vHttpUrlConnection = null;
		DataOutputStream vPrintout = null;
		DataInputStream	vInput = null;
		StringBuffer vStringBuffer=null;
		vUrl = new URL(pBankUrl);

		if(vUrl.openConnection() instanceof HttpsURLConnection)
		{
			vHttpUrlConnection = (HttpsURLConnection)vUrl.openConnection();
		} 
		else if(vUrl.openConnection() instanceof com.sun.net.ssl.HttpsURLConnection)
		{
			vHttpUrlConnection = (com.sun.net.ssl.HttpsURLConnection)vUrl.openConnection();
		} else
		{
			vHttpUrlConnection = (URLConnection)vUrl.openConnection();
		}
		vHttpUrlConnection.setDoInput(true);
		vHttpUrlConnection.setDoOutput(true);
		vHttpUrlConnection.setUseCaches(false);
		vHttpUrlConnection.connect();
		vPrintout = new DataOutputStream (vHttpUrlConnection.getOutputStream());
		vPrintout.writeBytes(pBankData);
		vPrintout.flush();
		vPrintout.close();
		try {
			BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(vHttpUrlConnection.getInputStream()));
			vStringBuffer = new StringBuffer();
			String vRespData;
			while((vRespData = bufferedreader.readLine()) != null) 
				if(vRespData.length() != 0)
					vStringBuffer.append(vRespData.trim());
			bufferedreader.close();
			bufferedreader = null;
		}finally {  
			if (vInput != null)
				vInput.close();  
			if (vHttpUrlConnection != null)  
				vHttpUrlConnection = null;  
		}  
		return vStringBuffer.toString();
	}
	
	public static HashMap tokenizeToHashMap(String msg, String delimPairValue, String delimKeyPair){
		HashMap keyPair = new HashMap();
		ArrayList respList = new ArrayList();
		String part = "";
		StringTokenizer strTkn = new StringTokenizer(msg, delimPairValue,true);
		while (strTkn.hasMoreTokens())
		{
			part = (String)strTkn.nextElement(); 
			if(part.equals(delimPairValue)) {
				part=null; 
			}
			else {
				String str[]=part.split(delimKeyPair, 2);
				keyPair.put(str[0], str.length>1?(str[1].equals("")?null:str[1]):null);
			}
			if(part == null) continue;
			if(strTkn.hasMoreTokens()) strTkn.nextElement();
		}		
		return keyPair.size() > 0 ? keyPair : null;
	}

	
	@RequestMapping(value = "ccavenueAPI", method = RequestMethod.POST)
	@ResponseBody
	public String ccavenueAPI(@RequestBody String pXmlData) throws Exception {
		
		logger.info("ccavenueAPI is Calling..");
//		String pXmlData="<?xml version='1.0' encoding='UTF-8' standalone='yes'?><Order_Status_Query order_no='' reference_no='205000184724'/>";
		String pAccessCode="AVJJ02FC73AJ95JJJA";
		String aesKey="351EF2DD99D5CAB101FF1649B8A3361F";
		String pCommand="chargeSI";
		String pRequestType="xml";
		String pResponseType="xml";
		String pVersion="1.1";
		
		String finalResponse = new Tester().callCCavenueApi(pXmlData, pAccessCode, pCommand, aesKey, pRequestType, pResponseType, pVersion);
		
	
	
		
		return finalResponse;
		
		
		/*
		
		
		
		
		String working_key = "351EF2DD99D5CAB101FF1649B8A3361F"; //Shared by CCAVENUES
		String access_code = "AVJJ02FC73AJ95JJJA";
		
		AesCryptUtil aesUtil=new AesCryptUtil(working_key);
		String encXml = aesUtil.encrypt(request);
		
		SecretKey secKey = decodeKeyFromString(working_key);
//		SecretKey secKey = getSecretEncryptionKey();
		
		byte[] cipherText = encryptText(request, secKey);
		String s = bytesToHex(cipherText);
		
//		String decryptTest = decryptText(cipherText, secKey);
		String line="";
		
//		System.out.println("DecryptText.."+decryptTest);
		JSONObject finalRequest = new JSONObject();
		finalRequest.put("request_type", "JSON");
		finalRequest.put("access_code", access_code);
		finalRequest.put("command", "chargeSI");
		finalRequest.put("response_type", "JSON");
		finalRequest.put("enc_request", encXml);
//		finalRequest.put("version", "1.1");
		
		StringBuffer wsDataBuff=new StringBuffer();
		
		wsDataBuff.append("enc_request="+encXMLData+"&access_code="+pAccessCode+"&command="+pCommand+"&response_type="+pResponseType+"&request_type="+pRequestType+"&version="+pVersion);
		
		System.out.println("Final Request.."+finalRequest.toString());
		StringBuilder response = new StringBuilder();
		
		try {
			URL url = new URL("https://login.ccavenue.ae/apis/servlet/DoWebTrans");
			
			HttpURLConnection connection = null;
			connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("POST");

			connection.setRequestProperty("Content-Type","application/json");
		   
			//connection.setRequestProperty("Content-Length", Integer.toString((finalRequest.toString()).getBytes().length));
	
			connection.setUseCaches(false);
			connection.setDoOutput(true);
	       
	       try (DataOutputStream wr = new DataOutputStream (

	    		      connection.getOutputStream())) {
	    		  wr.writeBytes(finalRequest.toString());
	    		}
	       
	       
	       int responseCode = connection.getResponseCode();

	        //System.out.println(responseCode+"\tSuccess");

	        InputStream is;

	        if(responseCode == HttpURLConnection.HTTP_OK){
	        	is = connection.getInputStream();
	        }else {
	        	is = connection.getErrorStream();
	        }      
	              BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	              line="";
	              while((line = rd.readLine()) != null) {
	              //System.out.append(line);
	              //System.out.println("line.."+line);
	              response.append(line);
	              System.out.println("RESPONSE..."+response.toString());
	              System.out.append("\r");

	              }
	              rd.close(); is.close();     
		} 
	      catch (IOException e) {
			System.out.println("Exception in CCAVENUE class.."+e);
			e.printStackTrace();
		}
		try {

			
			URL url = new URL("https://login.ccavenue.ae/apis/servlet/DoWebTrans");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));
			
			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				System.out.println(output);
				response.append(output);
			}

			conn.disconnect();

		  } catch (MalformedURLException e) {

			e.printStackTrace();

		  } catch (IOException e) {

			e.printStackTrace();

		  }
		return response.toString();
	*/}
	
	
	
	
	
	
	public static SecretKey decodeKeyFromString(String keyStr) {
		  /* Decodes a Base64 encoded String into a byte array */
		  byte[] decodedKey = keyStr.getBytes();

		  /* Constructs a secret key from the given byte array */
		  SecretKey secretKey = new SecretKeySpec(decodedKey, 0,
		    decodedKey.length, "AES");
		  
		  
		 
		  return secretKey;
		 }
	
	
	
	
	public static SecretKey getSecretEncryptionKey() throws Exception{
		        KeyGenerator generator = KeyGenerator.getInstance("AES");
		        generator.init(128); // The AES key size in number of bits
		        SecretKey secKey = generator.generateKey();
		        return secKey;
		    }

	/*public static byte[] encryptText(String plainText,SecretKey secKey) throws Exception{
		        // AES defaults to AES/ECB/PKCS5Padding in Java 7
		        Cipher aesCipher = Cipher.getInstance("AES");
		        aesCipher.init(Cipher.ENCRYPT_MODE, secKey);
		        byte[] byteCipherText = aesCipher.doFinal(plainText.getBytes());
		        return byteCipherText;
		    }*/

	public static String decryptText(byte[] byteCipherText, SecretKey secKey) throws Exception {
		        // AES defaults to AES/ECB/PKCS5Padding in Java 7
		        Cipher aesCipher = Cipher.getInstance("AES");
		        aesCipher.init(Cipher.DECRYPT_MODE, secKey);
		        byte[] bytePlainText = aesCipher.doFinal(byteCipherText);
		        return new String(bytePlainText);
		    }

	private static String  bytesToHex(byte[] hash) {
		        return DatatypeConverter.printHexBinary(hash);
		    }

	/*public static void main(String[] args) throws Exception {
		
		        String plainText = "Hello World";
		
		        SecretKey secKey = getSecretEncryptionKey();
		
		        byte[] cipherText = encryptText(plainText, secKey);
		
		        String decryptedText = decryptText(cipherText, secKey);
		
		         
		        System.out.println("Original Text:" + plainText);
		        System.out.println("AES Key (Hex Form):"+bytesToHex(secKey.getEncoded()));
		        System.out.println("Encrypted Text (Hex Form):"+bytesToHex(cipherText));
		        System.out.println("Descrypted Text:"+decryptedText);
		         
		    }*/

}
