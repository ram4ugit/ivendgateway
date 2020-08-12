package com.limitlessmobility.iVendGateway.controller.terminal;

import java.util.List;

import com.limitlessmobility.iVendGateway.dao.TerminalDeviceIdDao;
import com.limitlessmobility.iVendGateway.dao.TerminalDeviceIdDaoImpl;
import com.limitlessmobility.iVendGateway.model.terminal.TerminalDeviceIdModel;

public class TerminalDeviceIdController {

	public List<TerminalDeviceIdModel> getTerminalDeviceId() {

		TerminalDeviceIdDao terminalDeviceIdDao = new TerminalDeviceIdDaoImpl();
		return terminalDeviceIdDao.getTerminalDeviceId();

	}

}
