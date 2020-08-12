package com.limitlessmobility.iVendGateway.model.wallet;

import com.google.gson.Gson;

public class WalletCheckStatusRequest {

//	private String walletAccountId;
	private String referenceNo;
	
	
	
	public String getReferenceNo() {
		return referenceNo;
	}



	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}



	@Override
    public String toString() {
	    return new Gson().toJson(this);
    }
}
