package com.limitlessmobility.iVendGateway.model.wallet;

public class WalletBlockAmountRequest {

	private String WalletAccountId;
	private String amount;
	
	public String getWalletAccountId() {
		return WalletAccountId;
	}
	public void setWalletAccountId(String walletAccountId) {
		WalletAccountId = walletAccountId;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
}
