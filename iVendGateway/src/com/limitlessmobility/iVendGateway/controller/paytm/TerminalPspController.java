package com.limitlessmobility.iVendGateway.controller.paytm;

import org.apache.log4j.Logger;

import com.limitlessmobility.iVendGateway.dao.TerminalPspDao;
import com.limitlessmobility.iVendGateway.dao.TerminalPspDaoImpl;
import com.limitlessmobility.iVendGateway.paytm.model.StatusError;
import com.limitlessmobility.iVendGateway.paytm.model.TerminalPsp;
import com.limitlessmobility.iVendGateway.paytm.model.TerminalPspSuper;

public class TerminalPspController {

	private static final Logger logger = Logger.getLogger(TerminalPspController.class);

	public TerminalPspSuper checkStatus(TerminalPsp terminalPsp) {

		TerminalPspDao terminalPspDao = new TerminalPspDaoImpl();
		TerminalPspSuper terminalPspSuper = new TerminalPspSuper();

		try {
			if (terminalPsp != null) {
				TerminalPsp mguid = terminalPspDao.checkStatus(terminalPsp);

				terminalPspSuper.setRequest(terminalPsp);

				terminalPsp.setRequestType("merchantTxnId");
				terminalPsp.setTxnType("withdraw");
				
				terminalPspSuper.setPlatformName("PayTM");
				terminalPspSuper.setOperationType("CHECK_TXN_STATUS");
				terminalPsp.setFlag(StatusError.SUCCESS);

			} else {
				terminalPsp.setFlag(StatusError._500);
			}
		} catch (Exception e) {

			e.printStackTrace();
			logger.error(e);

		}

		return terminalPspSuper;

	}

}
