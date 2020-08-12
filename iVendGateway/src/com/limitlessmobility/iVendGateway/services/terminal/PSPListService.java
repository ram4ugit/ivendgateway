package com.limitlessmobility.iVendGateway.services.terminal;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.limitlessmobility.iVendGateway.controller.terminal.PSPController;
import com.limitlessmobility.iVendGateway.psp.model.PSPModel;

@Controller
@RequestMapping(value="/terminal")
public class PSPListService {

	PSPController pspController = new PSPController();
	
	@RequestMapping(value = "/getpspList", method = RequestMethod.POST)
	@ResponseBody
	public String getPSPList(@RequestBody String deviceid, String terminalid) throws JSONException{
		JSONObject jsonResponse = new JSONObject();
//		String terminalId = pspController.getTerminalIdByDeviceId(deviceid);
//		List<String> listpspMerchantId = pspController.getPSPMerchantId(terminalId);
		List<PSPModel> pspList = new ArrayList<PSPModel>();
//		pspList = pspController.getpspList(listpspMerchantId);
		if(pspList.isEmpty()){
			jsonResponse.put("status", 1);
			
			PSPModel p = new PSPModel();
			p.setPaymentId("dynamic");
			p.setPspID("paytm");
			p.setImage("images/paytm.jpg");
			pspList.add(p);
			
			PSPModel pp = new PSPModel();
			pp.setPaymentId("dynamic");
			pp.setPspID("mobikwik");
			pp.setImage("images/mobikwik.jpg");
			pspList.add(pp);
			
			Gson gson = new Gson();
			String pspListJson = gson.toJson(pspList);
			jsonResponse.put("data", pspList);
			
			return jsonResponse.toString();
		} else {
			jsonResponse.put("status", 1);
			Gson gson = new Gson();
//			String pspListJson = gson.toJson(pspList);
			jsonResponse.append("data", pspList);
			return jsonResponse.toString();
		}
	}
	
	
}
