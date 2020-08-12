package com.limitlessmobility.iVendGateway.dao;

import java.util.List;

import com.limitlessmobility.iVendGateway.paytm.model.Terminal;

public interface TerminalIdDao {

	List<Terminal> getDetails(Terminal terminal);

}
