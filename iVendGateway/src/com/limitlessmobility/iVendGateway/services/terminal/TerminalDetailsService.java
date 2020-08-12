package com.limitlessmobility.iVendGateway.services.terminal;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.google.gson.Gson;
import com.limitlessmobility.iVendGateway.controller.terminal.TerminalDetailsController;
import com.limitlessmobility.iVendGateway.paytm.model.Terminal;

@Controller
@RequestMapping("/v1")
public class TerminalDetailsService {

	@RequestMapping(value = "/Terminals", method = RequestMethod.GET)
	@ResponseBody

	public String getTerminalDetails() throws JSONException {

		System.out.println("1: in service");

		List<Terminal> terminalDetailList = new ArrayList<Terminal>();
		TerminalDetailsController terminalDetailsController = new TerminalDetailsController();
		terminalDetailList = terminalDetailsController.getTerminalDetails();

		Gson gson = new Gson();
		String jsonCartList = gson.toJson(terminalDetailList);

		if (terminalDetailList.isEmpty()) {
			System.out.println("finally empty table");
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("message", "Empty Table");

			return jsonObject.toString();
		}

		return jsonCartList;

	}
}
