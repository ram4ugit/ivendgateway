package com.limitlessmobility.iVendGateway.dao.common;

import com.limitlessmobility.iVendGateway.model.common.ConfigDetail;
import com.limitlessmobility.iVendGateway.model.common.OperatorDetail;
import com.limitlessmobility.iVendGateway.model.common.RefundDbModel;


public interface CommonCredentialDao {
	
	public OperatorDetail getPspConfigDetail(int operatorId, String pspId);
	
	public OperatorDetail getPspConfigPhonepeStatic(int operatorId, String pspId);
	
	public OperatorDetail getPspConfigDetailForWallet(int operatorId, String pspId);

	public ConfigDetail getConfigDetail(String telemetryId, String pspId);
	
	public int getOperatorId(String telemetryId, String pspId);
	
	public String getStoreId(String telemetryId, String pspId);
	
	public RefundDbModel getRefundDetailsByOrderId(String orderId);
}
