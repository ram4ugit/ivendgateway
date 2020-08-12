package com.limitlessmobility.iVendGateway.services.common;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.limitlessmobil.ivendgateway.util.HttpStatusModal;
import com.limitlessmobil.ivendgateway.util.JsonValidator;
import com.limitlessmobility.iVendGateway.controller.paymentmode.PaymentModeController;
import com.limitlessmobility.iVendGateway.model.paymentmode.PspDetailModal;

@Controller
@RequestMapping(value="/v1")
public class CommonQRService {
	
	PaymentModeController paymentModeController = new PaymentModeController();
	private static final Logger logger = Logger.getLogger(CommonQRService.class);
	
	/*
	 * This API is used for get QR for all.
	 */
	@RequestMapping(value = "/getqr", method = RequestMethod.POST)
	@ResponseBody
	public String commonQRCode(@RequestBody String commonQrResuest) throws Exception {
		logger.info("CommonQRCode Api Start. Request:- "+commonQrResuest);
		JSONObject qrRequestJson = new JSONObject(commonQrResuest);
		
		String pspId = qrRequestJson.getString("pspId");
		PspDetailModal pspDetailModal = new PspDetailModal();
		pspDetailModal = paymentModeController.getPspDetailById(pspId);
		
		String qrRequest = qrRequestJson.toString();
		String line="";
		StringBuilder response=new StringBuilder();
		String uri = pspDetailModal.getPspApiUrl()+"getqr";

//		String uri = "http://localhost:8080/iVendGateway/v1/bharatqr/getqr";
		HttpURLConnection connection = null;
		try{
//		System.out.println("URL.."+uri);
		
			URL url = new URL(uri);
			
			connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("POST");
	        connection.setRequestProperty("Content-Type","application/json");
	        connection.setRequestProperty("Content-Length", Integer.toString(qrRequest.getBytes().length));
	        connection.setUseCaches(false);
	        connection.setDoOutput(true);
       
       try (DataOutputStream wr = new DataOutputStream (
    		   	connection.getOutputStream())) {
    		  	wr.writeBytes(qrRequest);
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
            	  response.append(line);
            	  System.out.append("\r");
              }
              rd.close(); is.close();     
	} catch (IOException e) {
//		System.out.println("Exception in DynamicQRService class.."+e);
		logger.error("Exception in DynamicQRService class.. "+e);
		e.printStackTrace();
	} finally{
		connection.disconnect();
	}
//		 System.out.println("RESPONSE..."+response.toString());
		JSONObject finalResponse = new JSONObject();
		
		boolean responseValidator = JsonValidator.isJSONValid(response.toString());
		if(responseValidator){
			JSONObject responseObj = new JSONObject(response.toString());
			finalResponse.put("responseObj", responseObj);
			finalResponse.put("message", "null");
			finalResponse.put("status", HttpStatusModal.OK);
			return finalResponse.toString();
		} else {
			finalResponse.put("responseObj", "null");
			finalResponse.put("message", HttpStatusModal.ERROR_FORMAT);
			finalResponse.put("status", HttpStatusModal.ERROR);
			return finalResponse.toString();
		}
	}
	
	@RequestMapping(value = "/checkStatus", method = RequestMethod.POST)
	@ResponseBody
	public String commonCheckStatus(@RequestBody String checkStatusRequest) throws Exception {
		System.out.println("====commonCheckStatus===");
		JSONObject qrRequestJson = new JSONObject(checkStatusRequest);
		
		String pspId = qrRequestJson.getString("pspId");
		System.out.println("====commonCheckStatus..pspId==="+pspId);
		PspDetailModal pspDetailModal = new PspDetailModal();
			pspDetailModal = paymentModeController.getPspDetailById(pspId);
		
		String qrRequest = qrRequestJson.toString();
		String line="";
		StringBuilder response=new StringBuilder();
		String uri = pspDetailModal.getPspApiUrl()+"checkStatus";

		HttpURLConnection connection = null;
		try{
			System.out.println("URL.."+uri);
		URL url = new URL(uri);
		
		connection = (HttpURLConnection)url.openConnection();
		connection.setRequestMethod("POST");

        connection.setRequestProperty("Content-Type","application/json");
		
        connection.setRequestProperty("Content-Length", Integer.toString(qrRequest.getBytes().length));
        connection.setUseCaches(false);

        connection.setDoOutput(true);
       
       try (DataOutputStream wr = new DataOutputStream (

    		      connection.getOutputStream())) {
    		  wr.writeBytes(qrRequest);
    		}
       
       
       int responseCode = connection.getResponseCode();

        //System.out.println(responseCode+"\tSuccess");

        InputStream is;

        if(responseCode == HttpURLConnection.HTTP_OK){

        is = connection.getInputStream();

        }else {

        is = connection.getErrorStream();

        }
              /*try (BufferedReader rd = new BufferedReader(new InputStreamReader(is))) { response = new StringBuilder(); // or StringBuffer if not Java 5+ String line = ""; while((line = rd.readLine()) != null) 
              {

           //System.out.println("line.."+line);
              response.append(line);

              response.append('\r');

          }           }*/ // or StringBuffer if not Java 5+
              
              BufferedReader rd = new BufferedReader(new InputStreamReader(is));
              line="";
              while((line = rd.readLine()) != null) {
              //System.out.append(line);
              //System.out.println("line.."+line);
              response.append(line);
              
//              System.out.println("RESPONSE..."+response.toString());
              System.out.append("\r");

              }
              rd.close(); is.close();     
	} catch (IOException e) {
		System.out.println("Exception in DynamicQRService class.."+e);
		e.printStackTrace();
	} finally {
		connection.disconnect();
	}
		JSONObject finalResponse = new JSONObject();
		
		boolean responseValidator = JsonValidator.isJSONValid(response.toString());
		
//		System.out.println("Response Error"+response.toString());
		if(responseValidator){
			JSONObject responseObj = new JSONObject(response.toString());
			finalResponse.put("responseObj", responseObj);
			finalResponse.put("message", "null");
			finalResponse.put("status", HttpStatusModal.OK);
			
			JSONObject checkEmptyJson = new JSONObject(response.toString());
			System.out.println(checkEmptyJson);
			if(checkEmptyJson.getString("status").toString().equalsIgnoreCase("FAILURE")){
				finalResponse.put("responseObj", "null");
				finalResponse.put("message", HttpStatusModal.ERROR_FORMAT);
				finalResponse.put("status", HttpStatusModal.ERROR);
			}
			
			return finalResponse.toString();
		} else {
			finalResponse.put("responseObj", "null");
			finalResponse.put("message", HttpStatusModal.ERROR_FORMAT);
			finalResponse.put("status", HttpStatusModal.ERROR);
			return finalResponse.toString();
		}
	}
	
}
