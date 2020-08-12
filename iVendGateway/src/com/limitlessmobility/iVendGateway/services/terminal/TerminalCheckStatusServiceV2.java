package com.limitlessmobility.iVendGateway.services.terminal;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.limitlessmobility.iVendGateway.controller.paytm.TerminalPspController;
import com.limitlessmobility.iVendGateway.paytm.model.TerminalPsp;
import com.limitlessmobility.iVendGateway.paytm.model.TerminalPspSuper;
import com.limitlessmobility.iVendGateway.services.paytm.PayTmCheckStatus;
import com.limitlessmobility.iVendGateway.services.paytm.PayTmCheckStatusV2;

@Controller
@RequestMapping(value="v2/paytm")
public class TerminalCheckStatusServiceV2 {

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
		
		PayTmCheckStatusV2 payTmCheckStatus = new PayTmCheckStatusV2();
		String result = payTmCheckStatus.getPaytmCheckStatus(json.toString(),pspId, appId,terminalPsp.getTxnId(),terminalPsp.getPosId(),terminalPsp.getAmount());

		return result;

	}

}
