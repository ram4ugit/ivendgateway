package com.limitlessmobility.iVendGateway.model.wallet;

import com.google.gson.Gson;

public class WalletSettleAmountRequest {

	private Integer walletAccountId;
	private String transactionRefNo;
	private double amount;
	private String source;
	
	
	


	public String getSource() {
		return source;
	}






	public void setSource(String source) {
		this.source = source;
	}






	public Integer getWalletAccountId() {
		return walletAccountId;
	}






	public void setWalletAccountId(Integer walletAccountId) {
		this.walletAccountId = walletAccountId;
	}






	public String getTransactionRefNo() {
		return transactionRefNo;
	}






	public void setTransactionRefNo(String transactionRefNo) {
		this.transactionRefNo = transactionRefNo;
	}






	public double getAmount() {
		return amount;
	}






	public void setAmount(double amount) {
		this.amount = amount;
	}






	@Override
    public String toString() {
	    return new Gson().toJson(this);
    }
	
}
