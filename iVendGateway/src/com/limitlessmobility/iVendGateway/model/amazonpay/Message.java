package com.limitlessmobility.iVendGateway.model.amazonpay;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Message {
	@JsonProperty("storeId")
	 private String storeId;
	
	@JsonProperty("amazonOrderId")
	 private String amazonOrderId;
	
	@JsonProperty("sellerOrderId")
	 private String sellerOrderId;
	
	@JsonProperty("orderTotalAmount")
	 private float orderTotalAmount;
	
	@JsonProperty("StatusObject")
	 Status status;
	
	@JsonProperty("transactionDate")
	 private float transactionDate;
	
	@JsonProperty("merchantId")
	 private String merchantId;


	 // Getter Methods 

	 public String getStoreId() {
	  return storeId;
	 }

	 public String getAmazonOrderId() {
	  return amazonOrderId;
	 }

	 public String getSellerOrderId() {
		return sellerOrderId;
	}

	public float getOrderTotalAmount() {
	  return orderTotalAmount;
	 }

	 public Status getStatus() {
	  return status;
	 }

	 public float getTransactionDate() {
	  return transactionDate;
	 }

	 public String getMerchantId() {
	  return merchantId;
	 }

	 // Setter Methods 

	 public void setStoreId(String storeId) {
	  this.storeId = storeId;
	 }

	 public void setAmazonOrderId(String amazonOrderId) {
	  this.amazonOrderId = amazonOrderId;
	 }

	 public void setSellerOrderId(String sellerOrderId) {
		this.sellerOrderId = sellerOrderId;
	}

	public void setOrderTotalAmount(float orderTotalAmount) {
	  this.orderTotalAmount = orderTotalAmount;
	 }

	 public void setStatus(Status statusObject) {
	  this.status = status;
	 }

	 public void setTransactionDate(float transactionDate) {
	  this.transactionDate = transactionDate;
	 }

	 public void setMerchantId(String merchantId) {
	  this.merchantId = merchantId;
}
}