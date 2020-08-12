package com.limitlessmobility.iVendGateway.services.terminal;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.limitlessmobility.iVendGateway.controller.terminal.TerminalMerchantIdController;
import com.limitlessmobility.iVendGateway.model.terminal.TerminalMerchantIdModel;

@Controller
@RequestMapping("/v1")
public class TerminalMerchantIdService {

	String result = "";

	@RequestMapping(value = "/Merchants", method = RequestMethod.GET)
	@ResponseBody
	public String getTerminalMerchantId() {

		List<TerminalMerchantIdModel> terminalMerchantList = new ArrayList<TerminalMerchantIdModel>();
		TerminalMerchantIdController terminalMerchantIdController = new TerminalMerchantIdController();
		terminalMerchantList = terminalMerchantIdController.getTerminalMerchantId();

		Gson gson = new Gson();
		// convert your list to json
		String jsonCartList = gson.toJson(terminalMerchantList);

		return jsonCartList;

	}

}
