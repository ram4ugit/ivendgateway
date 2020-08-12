package com.limitlessmobility.iVendGateway.services.keyboard;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.limitlessmobility.iVendGateway.controller.keyboard.KeyboardController;

@Controller
@RequestMapping(value="/keyboard")
public class KeyboardService {

	/*@RequestMapping(value="/gettype", method=RequestMethod.POST)
	@ResponseBody
	public String getKeyboardType(@RequestBody String terminalIdString){
		JSONObject requestJsonObj = new JSONObject(terminalIdString);
		KeyboardController keyboardController = new KeyboardController();
		String terminlalId = requestJsonObj.getString("terminalId");
		if();
		keyboardController.getKeyboardType(terminalId);
	}*/
}
