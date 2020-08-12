package com.limitlessmobility.iVendGateway.dao.amazonpay;

import com.limitlessmobility.iVendGateway.model.amazonpay.CredentialData;
import com.limitlessmobility.iVendGateway.model.amazonpay.TerminalDetail;

public interface AmazonPayDao {

	
	public abstract String getMerchantOrderId(String txnId);
	
	public  CredentialData getAllCredentialDetail(String pspId , String terminalId);
	
	public  String getStoreIdForTerminal(String pspId , String terminalId);
	
	public TerminalDetail getTerminalDetail(String txnId);
	
	public boolean checkTxnId(String txnId);
	
	public abstract String getDeviceIDByTerminalId(String terminalId);
	
	public boolean updateTerminalDevice(String txnId,String deviceId,String terminalId,String appId);
	
	public String getTerminalIdByStroreId(String storeId);
}
