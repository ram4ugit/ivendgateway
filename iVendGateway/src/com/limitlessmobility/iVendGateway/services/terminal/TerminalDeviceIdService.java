package com.limitlessmobility.iVendGateway.services.terminal;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.limitlessmobility.iVendGateway.controller.terminal.TerminalDeviceIdController;
import com.limitlessmobility.iVendGateway.model.terminal.TerminalDeviceIdModel;

@Controller
@RequestMapping("/v1")
public class TerminalDeviceIdService {

	// String result = "";

	@RequestMapping(value = "/Devices", method = RequestMethod.GET)
	@ResponseBody

	public String getTerminalDeviceId() {

		List<TerminalDeviceIdModel> terminalDeviceList = new ArrayList<TerminalDeviceIdModel>();
		TerminalDeviceIdController terminalDeviceIdController = new TerminalDeviceIdController();
		terminalDeviceList = terminalDeviceIdController.getTerminalDeviceId();

		Gson gson = new Gson();
		// convert your list to json
		String jsonCartList = gson.toJson(terminalDeviceList);

		return jsonCartList;

	}
}
