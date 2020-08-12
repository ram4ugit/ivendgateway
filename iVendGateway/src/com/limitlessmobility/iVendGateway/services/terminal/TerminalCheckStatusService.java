package com.limitlessmobility.iVendGateway.services.terminal;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.limitlessmobility.iVendGateway.controller.paytm.TerminalPspController;
import com.limitlessmobility.iVendGateway.paytm.model.TerminalPsp;
import com.limitlessmobility.iVendGateway.paytm.model.TerminalPspSuper;
import com.limitlessmobility.iVendGateway.services.paytm.PayTmCheckStatus;

@Controller
@RequestMapping(value="v1/paytm")
public class TerminalCheckStatusService {

	private static final Logger logger = Logger.getLogger(TerminalCheckStatusService.class);

	@RequestMapping(value = "/checkStatus", method = RequestMethod.POST)
	@ResponseBody
	public String terminalRegistration(@RequestBody TerminalPsp terminalPsp) throws Exception {

		System.out.println("==checkStatus===");
//		logger.info(terminalPsp.getTerminalId());
		logger.info(terminalPsp.getTxnId());

		
		TerminalPspSuper terminalPspSuper = new TerminalPspSuper();
		TerminalPspController terminalPspController = new TerminalPspController();

		terminalPsp.setPspId("paytm");
		terminalPsp.setPaymentId("2");
		
		
		terminalPspSuper = terminalPspController.checkStatus(terminalPsp);

		System.out.println("done");
		
		terminalPspSuper.setChannel("POS");
		terminalPspSuper.setVersion("1.0");
		
		Gson gson = new Gson();
		String jsonString = gson.toJson(terminalPspSuper);
		JSONObject json = new JSONObject(jsonString);
		JSONObject requestJson = json.getJSONObject("request");
		String pspId = requestJson.getString("pspId");
		requestJson.remove("terminalId");
		requestJson.remove("pspId");
		requestJson.remove("paymentId");
		System.out.println(requestJson);
		String appId = requestJson.getString("appId");
		requestJson.remove("appId");
		requestJson.remove("posId");
		requestJson.remove("amount");
		
		
		json.put("request", requestJson);
		
		PayTmCheckStatus payTmCheckStatus = new PayTmCheckStatus();
		String result = payTmCheckStatus.getPaytmCheckStatus(json.toString(),pspId, appId,terminalPsp.getTxnId(),terminalPsp.getPosId(),terminalPsp.getAmount());

		return result;

	}

}
