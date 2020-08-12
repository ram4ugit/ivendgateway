package com.limitlessmobility.iVendGateway.controller.terminal;

import java.util.List;

import com.limitlessmobility.iVendGateway.dao.TerminalMerchantIdDao;
import com.limitlessmobility.iVendGateway.dao.TerminalMerchantIdDaoImpl;
import com.limitlessmobility.iVendGateway.model.terminal.TerminalMerchantIdModel;

public class TerminalMerchantIdController {

	public List<TerminalMerchantIdModel> getTerminalMerchantId() {

		TerminalMerchantIdDao terminalMerchantIdDao = new TerminalMerchantIdDaoImpl();
		return terminalMerchantIdDao.getTerminalMerchantId();
	}
}
