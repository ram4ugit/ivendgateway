package com.limitlessmobility.iVendGateway.services.terminal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.limitlessmobility.iVendGateway.dao.common.CommonService;

@Controller
@RequestMapping(value="v2")
public class LogSendService {

	@RequestMapping(value = "sendLog", method = RequestMethod.POST)
	@ResponseBody
	public boolean sendLog(){
		
//		CommonService.sendTransactionLog();
		return false;
	}
	
	public static void main(String[] args) {
	    new LogSendService().sendLog();
    }
}
