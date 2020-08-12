package com.limitlessmobility.iVendGateway.model.phonepe;

import java.util.ArrayList;

public class Data
{
	private String transactionId;

	private String merchantId;

	private String providerReferenceId;

	private int amount;

	private String paymentState;

	private String payResponseCode;

	private ArrayList<PaymentMode> paymentModes;

	private TransactionContext transactionContext;

	public String getTransactionId() {
		return this.transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getMerchantId() {
		return this.merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getProviderReferenceId() {
		return this.providerReferenceId;
	}

	public void setProviderReferenceId(String providerReferenceId) {
		this.providerReferenceId = providerReferenceId;
	}

	public int getAmount() {
		return this.amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getPaymentState() {
		return this.paymentState;
	}

	public void setPaymentState(String paymentState) {
		this.paymentState = paymentState;
	}

	public String getPayResponseCode() {
		return this.payResponseCode;
	}

	public void setPayResponseCode(String payResponseCode) {
		this.payResponseCode = payResponseCode;
	}

	public ArrayList<PaymentMode> getPaymentModes() {
		return this.paymentModes;
	}

	public void setPaymentModes(ArrayList<PaymentMode> paymentModes) {
		this.paymentModes = paymentModes;
	}

	public TransactionContext getTransactionContext() {
		return this.transactionContext;
	}

	public void setTransactionContext(TransactionContext transactionContext) {
		this.transactionContext = transactionContext;
	}
}

