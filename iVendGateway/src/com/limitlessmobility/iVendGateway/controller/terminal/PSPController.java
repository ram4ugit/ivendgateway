package com.limitlessmobility.iVendGateway.controller.terminal;

import java.util.ArrayList;
import java.util.List;

import com.limitlessmobility.iVendGateway.dao.TerminalPspDao;
import com.limitlessmobility.iVendGateway.dao.TerminalPspDaoImpl;
import com.limitlessmobility.iVendGateway.psp.model.PSPModel;

public class PSPController {

	TerminalPspDao terminalpspdDao = new TerminalPspDaoImpl();
	
	public String getTerminalIdByDeviceId(String device){
		
		String terminalID = terminalpspdDao.getTerminalIdByDeviceID(device);
		
		return terminalID;
	}
	public List<String> getPSPMerchantId(String terminalID){
		List<String> pspMerchantList = new ArrayList<String>();
		pspMerchantList = terminalpspdDao.getpspMerchantID(terminalID);
		return pspMerchantList;
	}
	public List<PSPModel> getpspList(String pspMerchantID){
		
		List<PSPModel> pspList = new ArrayList<PSPModel>();
		pspList = terminalpspdDao.getpspList(pspMerchantID);
		
		return pspList;
	}
}
