package com.limitlessmobility.iVendGateway.services.terminal;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.limitlessmobility.iVendGateway.controller.terminal.EventController;
import com.limitlessmobility.iVendGateway.paytm.model.EventLogs;
import com.limitlessmobility.iVendGateway.paytm.model.Terminal;
import com.limitlessmobility.iVendGateway.paytm.model.TerminalEventModel;

@Controller
@RequestMapping(value="terminallog")
public class TerminalEventService {

	private static final Logger logger = Logger.getLogger(TerminalCheckStatusService.class);
	EventController eventController = new EventController();
	
	@RequestMapping(value = "/geteventlog", method = RequestMethod.POST)
	@ResponseBody
	public boolean getTerminalEventLog(@RequestBody TerminalEventModel terminalEventModel) {
		logger.info("geteventlog...request.."+terminalEventModel);
		
		boolean status = eventController.saveOrUpdateTerminalEventLog(terminalEventModel);
		return status;
	}
}
