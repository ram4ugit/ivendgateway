package com.limitlessmobility.iVendGateway.controller.keyboard;

import com.limitlessmobility.iVendGateway.dao.keyboard.KeyboardDao;
import com.limitlessmobility.iVendGateway.dao.keyboard.KeyboardDaoImpl;

public class KeyboardController {

	KeyboardDao kdao = new KeyboardDaoImpl();
	
	public String getKeyboardType(String terminalId){
		return terminalId=kdao.getKeyboardType(terminalId);
	}
}
