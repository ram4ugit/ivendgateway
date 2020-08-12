package com.limitlessmobility.iVendGateway.dao;

import com.limitlessmobility.iVendGateway.paytm.model.Terminal;

public interface TerminalDao {
	public abstract String terminalRegistration(Terminal terminal);
}
