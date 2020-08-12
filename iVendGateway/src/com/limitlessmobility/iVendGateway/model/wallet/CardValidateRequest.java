package com.limitlessmobility.iVendGateway.model.wallet;

import com.google.gson.Gson;

public class CardValidateRequest {

	private String terminalId;
	private String orderId;
	private String cardNo;
	private double amount;
	private String productId;
	
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
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
