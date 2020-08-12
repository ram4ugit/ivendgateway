package com.limitlessmobility.iVendGateway.controller.paytm;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.limitlessmobility.iVendGateway.dao.TerminalDao;
import com.limitlessmobility.iVendGateway.dao.TerminalDaoImpl;
import com.limitlessmobility.iVendGateway.paytm.model.StatusError;
import com.limitlessmobility.iVendGateway.paytm.model.Terminal;

public class TerminalController {

	private static final Logger logger = Logger.getLogger(TerminalController.class);

	String result;

	public String terminalRegistration(Terminal terminal) {

		TerminalDao terminalDao = new TerminalDaoImpl();

		try {
			if (terminal.getTerminalId() != null && terminal.getTerminalId() != "") {

				result = terminalDao.terminalRegistration(terminal);

				System.out.println(StatusError.SUCCESS);

				terminal.setStatus(StatusError.SUCCESS);

			} else {

				JSONObject jsonObject = new JSONObject();

				result = jsonObject.put("status", "False").toString();

				result = jsonObject.put("message", "TerminalId Null or Empty Not Allowed").toString();

				System.out.println("StatusError._500");

				terminal.setStatus(StatusError._500);
			}
		} catch (Exception e) {

			e.printStackTrace();
			logger.error(e);

		}

		return result;
	}
}
