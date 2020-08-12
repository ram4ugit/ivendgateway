package com.limitlessmobility.iVendGateway.model.common;

import com.google.gson.Gson;

public class BlockRequest {

	private String walletId;
	private String walletKey;
	private String walletAccountId;
	private String walletAccountKey;
	private String amount;
	private String orderId;
	private long orderNo;
	private String currency;
	private String pspId;
	private String source;
	
	
	
	

	
	public String getSource() {
		return source;
	}



	public void setSource(String source) {
		this.source = source;
	}



	public String getWalletId() {
		return walletId;
	}



	public void setWalletId(String walletId) {
		this.walletId = walletId;
	}



	public String getWalletKey() {
		return walletKey;
	}



	public void setWalletKey(String walletKey) {
		this.walletKey = walletKey;
	}



	public String getWalletAccountId() {
		return walletAccountId;
	}



	public void setWalletAccountId(String walletAccountId) {
		this.walletAccountId = walletAccountId;
	}



	public String getWalletAccountKey() {
		return walletAccountKey;
	}



	public void setWalletAccountKey(String walletAccountKey) {
		this.walletAccountKey = walletAccountKey;
	}



	public String getAmount() {
		return amount;
	}



	public void setAmount(String amount) {
		this.amount = amount;
	}



	public String getOrderId() {
		return orderId;
	}



	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}



	public String getCurrency() {
		return currency;
	}



	public void setCurrency(String currency) {
		this.currency = currency;
	}



	public String getPspId() {
		return pspId;
	}



	public void setPspId(String pspId) {
		this.pspId = pspId;
	}

	


	public long getOrderNo() {
		return orderNo;
	}



	public void setOrderNo(long orderNo) {
		this.orderNo = orderNo;
	}



	@Override
    public String toString() {
	    return new Gson().toJson(this);
    }
	
}
