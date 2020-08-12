package com.limitlessmobil.ivendgateway.util;

import java.util.Map;

import org.json.JSONException;

import com.limitlessmobil.ivendgateway.dto.ResponseDTO;
import com.limitlessmobil.ivendgateway.dto.ResponseObj;


/**
 * @author RamN
 *
 */
public class ResponseUtility {

	/**
	 * 
	 */
	public ResponseUtility() {
		// TODO Auto-generated constructor stub
	}

	
	public static ResponseDTO sendResponse(int responseCode, String responseText, Map<String, Object> errors, String isRequestSuccess) {
		
		ResponseDTO responseDTO = new ResponseDTO();
		
		responseDTO.setErrors(errors);
		responseDTO.setStatus(isRequestSuccess);
		responseDTO.setStatusCode(responseCode);
		responseDTO.setMessage(responseText);
		
		return responseDTO;
		
	}
	
	public static String sendResponseString(int responseCode, String responseText, Map<String, Object> errors, String isRequestSuccess) throws JSONException {
		
		ResponseDTO responseDTO = new ResponseDTO();
		
		responseDTO.setErrors(errors);
		responseDTO.setStatus(isRequestSuccess);
		responseDTO.setStatusCode(responseCode);
		responseDTO.setMessage(responseText);
		
		
		return responseDTO.toString();
		
	}
	public static String sendResponseObj(int responseCode, String responseText, Map<String, Object> errors, String isRequestSuccess) {
		
		ResponseObj responseObj = new ResponseObj();
		
		responseObj.setStatus(isRequestSuccess);
		responseObj.setMessage(responseText);
		
		return responseObj.toString();
	}
}
