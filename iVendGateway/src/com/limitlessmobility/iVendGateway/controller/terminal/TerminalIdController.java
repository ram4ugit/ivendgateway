package com.limitlessmobility.iVendGateway.controller.terminal;

import java.util.List;

import com.limitlessmobility.iVendGateway.dao.TerminalIdDao;
import com.limitlessmobility.iVendGateway.dao.TerminalIdDaoImpl;
import com.limitlessmobility.iVendGateway.paytm.model.Terminal;

public class TerminalIdController {

	public List<Terminal> getTerminalList(Terminal terminal) {

		TerminalIdDao terminalIdDao = new TerminalIdDaoImpl();

		return terminalIdDao.getDetails(terminal);

	}

}
