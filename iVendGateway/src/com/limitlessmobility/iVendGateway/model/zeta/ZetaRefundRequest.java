package com.limitlessmobility.iVendGateway.model.zeta;

/*
 * This model is used for zeta refund
 */
public class ZetaRefundRequest {

	private String transactionId;
    private long amount;
	
    public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public long getAmount() {
		return amount;
	}
	public void setAmount(long amount) {
		this.amount = amount;
	}
}
