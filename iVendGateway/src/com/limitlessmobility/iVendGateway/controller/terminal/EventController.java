package com.limitlessmobility.iVendGateway.controller.terminal;

import com.limitlessmobil.ivendgateway.util.MySqlDate;
import com.limitlessmobility.iVendGateway.dao.EventLogDao;
import com.limitlessmobility.iVendGateway.dao.eventLogDaoImpl;
import com.limitlessmobility.iVendGateway.paytm.model.TerminalEventModel;

public class EventController {

	EventLogDao eventLogDao = new eventLogDaoImpl();
	MySqlDate mySqlDate = new MySqlDate();
	
	public boolean saveOrUpdateTerminalEventLog(TerminalEventModel terminalEventModel) {
		
		terminalEventModel.setPaymentSelectionDate(mySqlDate.getDate());
		
		
		boolean status = eventLogDao.saveTerminalEventLog(terminalEventModel);
		
		return status;
	}
}
