package com.limitlessmobility.iVendGateway.services.terminal;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.limitlessmobility.iVendGateway.controller.terminal.TerminalIdController;
import com.limitlessmobility.iVendGateway.paytm.model.Terminal;

@Controller
@RequestMapping("/v1")
public class TerminalIdService {

	@RequestMapping(value = "/Terminal/Id", method = RequestMethod.POST)
	@ResponseBody

	public String getTerminalDetailsId(@RequestBody Terminal terminal) throws JSONException {

		List<Terminal> terminalList = new ArrayList<Terminal>();
		TerminalIdController terminalIdController = new TerminalIdController();
		terminalList = terminalIdController.getTerminalList(terminal);

		Gson gson = new Gson();
		String jsonCartList = gson.toJson(terminalList);

		if (terminalList.isEmpty()) {
			System.out.println("finally empty table");
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("message", "Empty Table");

			return jsonObject.toString();
		}

		return jsonCartList;

	}
}
