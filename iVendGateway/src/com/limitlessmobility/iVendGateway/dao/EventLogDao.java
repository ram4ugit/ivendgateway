package com.limitlessmobility.iVendGateway.dao;

import com.limitlessmobility.iVendGateway.paytm.model.EventLogs;
import com.limitlessmobility.iVendGateway.paytm.model.PaytmRefundRequest;
import com.limitlessmobility.iVendGateway.paytm.model.PaytmRefundRequestModelFinal;
import com.limitlessmobility.iVendGateway.paytm.model.TerminalEventModel;

public abstract interface EventLogDao {

	public abstract boolean saveEventLog(EventLogs eventLog);
	public abstract boolean saveTerminalEventLog(TerminalEventModel terminalEvent);
}
