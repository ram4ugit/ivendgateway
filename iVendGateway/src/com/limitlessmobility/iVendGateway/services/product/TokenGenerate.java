package com.limitlessmobility.iVendGateway.services.product;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONException;
import org.json.JSONObject;

public class TokenGenerate {

	String reqdata = "";
	String output = "";
	String jsonString = "";

	public String generateToken() throws JSONException {

		System.out.println("in Token Generate class");

		JSONObject jsonObject = new JSONObject();
		reqdata = jsonObject.put("Token", " ").toString();

		System.out.println(reqdata);

		try {

			byte[] postData = reqdata.getBytes(StandardCharsets.UTF_8);
			int postDataLength = postData.length;
			String request = "https://lynx.nayax.com/Operational/v1/token";
			URL myURL = new URL(request);
			System.out.println("url is: " + myURL);
			URLConnection connection = myURL.openConnection();
			HttpURLConnection myURLConnection = (HttpURLConnection) connection;

//			String userCredentials = "dually-app\\z1demo" + ":" + "Qwerty1234";

			 String userCredentials = "dually-app\\nklynx" + ":" + "faceBOOK@18";

			String basicAuth = "Basic " + new String(new Base64().encode(userCredentials.getBytes()));

			myURLConnection.setRequestProperty("Authorization", basicAuth);
			myURLConnection.setRequestMethod("POST");
			myURLConnection.setRequestProperty("Content-Type", "application/json");
			myURLConnection.setRequestProperty("Content-Length", Integer.toString(postDataLength));
			myURLConnection.setRequestProperty("Content-Language", "en-US");
			myURLConnection.setUseCaches(false);
			myURLConnection.setDoInput(true);
			myURLConnection.setDoOutput(true);

			try {
				DataOutputStream wr = new DataOutputStream(myURLConnection.getOutputStream());
				wr.writeBytes(reqdata);
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (myURLConnection.getResponseCode() != 200) {
				// throw new RuntimeException("Failed : HTTP error
				// code : " + conn.getResponseCode());

				System.out.println("responce code: " + myURLConnection.getResponseCode());
				System.out.println("responce message: " + myURLConnection.getResponseMessage());

				JSONObject jsonResponse = new JSONObject();

				jsonString = jsonResponse.put(myURLConnection.getResponseMessage(), myURLConnection.getResponseCode())
						.toString();

				System.out.println("response from if(!=200): " + jsonString);

			}

			BufferedReader br = new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));

			System.out.println("Output from Server.... \n");
			while ((output = br.readLine()) != null) {

				System.out.println("output: " + output);

				JSONObject jsonResponse = new JSONObject();

				jsonString = jsonResponse.wrap(output).toString();
				System.out.println("output: " + jsonString);

			}

			myURLConnection.disconnect();

		}

		catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("final: " + jsonString);

		JSONObject jsonObject2 = new JSONObject(jsonString);

		jsonString = jsonObject2.getString("Token");

		return jsonString;

	}

	public static void main(String[] args) throws JSONException {
		new TokenGenerate().generateToken();
	}

}
