package com.limitlessmobility.iVendGateway.model.common;

import java.util.List;

import com.google.gson.Gson;

public class PaymentHistoryResponseModel {

	private String responseType;
	private int totalCount;
	private List<PaymentHistoryResponseData> data;
	public String getResponseType() {
		return responseType;
	}
	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	
	public List<PaymentHistoryResponseData> getData() {
		return data;
	}
	public void setData(List<PaymentHistoryResponseData> data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}
