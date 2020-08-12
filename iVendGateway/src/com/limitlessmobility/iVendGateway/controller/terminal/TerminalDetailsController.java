package com.limitlessmobility.iVendGateway.controller.terminal;

import java.util.List;

import com.limitlessmobility.iVendGateway.dao.TerminalDetailsDao;
import com.limitlessmobility.iVendGateway.dao.TerminalDetailsDaoImpl;
import com.limitlessmobility.iVendGateway.paytm.model.Terminal;

public class TerminalDetailsController {

	public List<Terminal> getTerminalDetails() {

		System.out.println("2: in controller");

		TerminalDetailsDao terminalDetailsDao = new TerminalDetailsDaoImpl();
		return terminalDetailsDao.getTerminalDetails();
	}

}
