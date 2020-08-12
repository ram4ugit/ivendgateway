package com.limitlessmobility.iVendGateway.model.sodexo;

import java.util.ArrayList;
import java.util.List;

public class Payload {

	private String transactionState;
	private String purchaseRequestId;
	private String purchaseTransactionId;
	private String purchaseRequestTime;
	private MerchantInfo merchantInfo =new MerchantInfo() ;
	private Amount amount=new Amount();
	private List<Purpose> purposes = new ArrayList<Purpose>();
	private CaptureInfo captureInfo=new CaptureInfo();

	public String getTransactionState() {
		return transactionState;
	}

	public void setTransactionState(String transactionState) {
		this.transactionState = transactionState;
	}

	public String getPurchaseRequestId() {
		return purchaseRequestId;
	}

	public void setPurchaseRequestId(String purchaseRequestId) {
		this.purchaseRequestId = purchaseRequestId;
	}

	public String getPurchaseTransactionId() {
		return purchaseTransactionId;
	}

	public void setPurchaseTransactionId(String purchaseTransactionId) {
		this.purchaseTransactionId = purchaseTransactionId;
	}

	public String getPurchaseRequestTime() {
		return purchaseRequestTime;
	}

	public void setPurchaseRequestTime(String purchaseRequestTime) {
		this.purchaseRequestTime = purchaseRequestTime;
	}

	public MerchantInfo getMerchantInfo() {
		return merchantInfo;
	}

	public void setMerchantInfo(MerchantInfo merchantInfo) {
		this.merchantInfo = merchantInfo;
	}

	public Amount getAmount() {
		return amount;
	}

	public void setAmount(Amount amount) {
		this.amount = amount;
	}

	public List<Purpose> getPurposes() {
		return purposes;
	}

	public void setPurposes(List<Purpose> purposes) {
		this.purposes = purposes;
	}

	public CaptureInfo getCaptureInfo() {
		return captureInfo;
	}

	public void setCaptureInfo(CaptureInfo captureInfo) {
		this.captureInfo = captureInfo;
	}

}
