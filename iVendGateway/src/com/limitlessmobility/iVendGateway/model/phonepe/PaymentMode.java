package com.limitlessmobility.iVendGateway.model.phonepe;

public class PaymentMode {
	private String mode;
	private int amount;
	private String utr;

	public String getMode() {
		return this.mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public int getAmount() {
		return this.amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getUtr() {
		return this.utr;
	}

	public void setUtr(String utr) {
		this.utr = utr;
	}
}
