package com.limitlessmobility.iVendGateway.services.terminal;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.limitlessmobility.iVendGateway.controller.paytm.TerminalController;
import com.limitlessmobility.iVendGateway.paytm.model.Terminal;

@Controller
@RequestMapping("/v1")
public class TerminalRegistrationService {
	private static final Logger logger = Logger.getLogger(TerminalRegistrationService.class);

	String result;

	@RequestMapping(value = "/Terminal/Register", method = RequestMethod.POST)
	@ResponseBody
	public String terminalRegistration(@RequestBody Terminal terminal) {

		logger.info(terminal.getTerminalId());
		logger.info(terminal.getDeviceId());
		logger.info(terminal.getMerchantName());
		logger.info(terminal.getImei());
		logger.info(terminal.getTerminalMac());
		logger.info(terminal.getTerminalAddress());
		logger.info(terminal.getTerminalCountry());
		logger.info(terminal.getTerminalState());
		logger.info(terminal.getTerminalCity());
		logger.info(terminal.getTerminalLat());
		logger.info(terminal.getTerminalLng());
		logger.info(terminal.getStatus());

		TerminalController terminalController = new TerminalController();

		result = terminalController.terminalRegistration(terminal);
		System.out.println("done");
		return result;

	}

}
