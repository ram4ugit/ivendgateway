package com.limitlessmobility.iVendGateway.model.amazonpay;

public class ProcessChargeServiceModel {

	private String customerIdentifier;

	private String customerIdentifierType;

	private String orderTotalAmount;

	private String orderTotalCurrencyCode;

	private String sellerOrderId;

	private String sellerId;

	private String storeId;

	private String isSandbox;

	private String posId;

	private String invoiceNumber;

	public String getCustomerIdentifier() {
		return customerIdentifier;
	}

	public void setCustomerIdentifier(String customerIdentifier) {
		this.customerIdentifier = customerIdentifier;
	}

	public String getCustomerIdentifierType() {
		return customerIdentifierType;
	}

	public void setCustomerIdentifierType(String customerIdentifierType) {
		this.customerIdentifierType = customerIdentifierType;
	}

	public String getOrderTotalAmount() {
		return orderTotalAmount;
	}

	public void setOrderTotalAmount(String orderTotalAmount) {
		this.orderTotalAmount = orderTotalAmount;
	}

	public String getOrderTotalCurrencyCode() {
		return orderTotalCurrencyCode;
	}

	public void setOrderTotalCurrencyCode(String orderTotalCurrencyCode) {
		this.orderTotalCurrencyCode = orderTotalCurrencyCode;
	}

	public String getSellerOrderId() {
		return sellerOrderId;
	}

	public void setSellerOrderId(String sellerOrderId) {
		this.sellerOrderId = sellerOrderId;
	}

	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getIsSandbox() {
		return isSandbox;
	}

	public void setIsSandbox(String isSandbox) {
		this.isSandbox = isSandbox;
	}

	public String getPosId() {
		return posId;
	}

	public void setPosId(String posId) {
		this.posId = posId;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

}
