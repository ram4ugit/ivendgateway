package com.limitlessmobility.iVendGateway.paytm.model;

import com.google.gson.Gson;

public class TerminalEventModel {

	private Integer id;
	private String appId;
	private String terminalId;
    private String telemetryId;

    private String trxId;
    //Product Selection
    private String paymentMode;
    private String paymentCode;
    private String vmcPaymentSelCode;
    private String vmcPaymentSelResponse;
    private String paymentSelectionDate;

    //Product Selection
    private String productCode;
    private String price;
    private String vmcProductSelCode;
    private String vmcProductSelRes;
    private String productSelectionDate;
    private String serverTrnId;

    private int status;
    private int isBarcodeGenerated;
    private int isPaymentDone;

    //VMC Status
    private String vmcProductVendStatus;
    private int isRefund;

    private String errorCode;
    private String txnGuid;
    private String paidAmount;
    private String pendingRefundAmount;
    
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}
	public String getTelemetryId() {
		return telemetryId;
	}
	public void setTelemetryId(String telemetryId) {
		this.telemetryId = telemetryId;
	}
	public String getTrxId() {
		return trxId;
	}
	public void setTrxId(String trxId) {
		this.trxId = trxId;
	}
	public String getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}
	public String getPaymentCode() {
		return paymentCode;
	}
	public void setPaymentCode(String paymentCode) {
		this.paymentCode = paymentCode;
	}
	public String getVmcPaymentSelCode() {
		return vmcPaymentSelCode;
	}
	public void setVmcPaymentSelCode(String vmcPaymentSelCode) {
		this.vmcPaymentSelCode = vmcPaymentSelCode;
	}
	public String getVmcPaymentSelResponse() {
		return vmcPaymentSelResponse;
	}
	public void setVmcPaymentSelResponse(String vmcPaymentSelResponse) {
		this.vmcPaymentSelResponse = vmcPaymentSelResponse;
	}
	public String getPaymentSelectionDate() {
		return paymentSelectionDate;
	}
	public void setPaymentSelectionDate(String paymentSelectionDate) {
		this.paymentSelectionDate = paymentSelectionDate;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getVmcProductSelCode() {
		return vmcProductSelCode;
	}
	public void setVmcProductSelCode(String vmcProductSelCode) {
		this.vmcProductSelCode = vmcProductSelCode;
	}
	public String getVmcProductSelRes() {
		return vmcProductSelRes;
	}
	public void setVmcProductSelRes(String vmcProductSelRes) {
		this.vmcProductSelRes = vmcProductSelRes;
	}
	public String getProductSelectionDate() {
		return productSelectionDate;
	}
	public void setProductSelectionDate(String productSelectionDate) {
		this.productSelectionDate = productSelectionDate;
	}
	public String getServerTrnId() {
		return serverTrnId;
	}
	public void setServerTrnId(String serverTrnId) {
		this.serverTrnId = serverTrnId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getIsBarcodeGenerated() {
		return isBarcodeGenerated;
	}
	public void setIsBarcodeGenerated(int isBarcodeGenerated) {
		this.isBarcodeGenerated = isBarcodeGenerated;
	}
	public int getIsPaymentDone() {
		return isPaymentDone;
	}
	public void setIsPaymentDone(int isPaymentDone) {
		this.isPaymentDone = isPaymentDone;
	}
	public String getVmcProductVendStatus() {
		return vmcProductVendStatus;
	}
	public void setVmcProductVendStatus(String vmcProductVendStatus) {
		this.vmcProductVendStatus = vmcProductVendStatus;
	}
	public int getIsRefund() {
		return isRefund;
	}
	public void setIsRefund(int isRefund) {
		this.isRefund = isRefund;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getTxnGuid() {
		return txnGuid;
	}
	public void setTxnGuid(String txnGuid) {
		this.txnGuid = txnGuid;
	}
	public String getPaidAmount() {
		return paidAmount;
	}
	public void setPaidAmount(String paidAmount) {
		this.paidAmount = paidAmount;
	}
	public String getPendingRefundAmount() {
		return pendingRefundAmount;
	}
	public void setPendingRefundAmount(String pendingRefundAmount) {
		this.pendingRefundAmount = pendingRefundAmount;
	}
	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
	
	
}
