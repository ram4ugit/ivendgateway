package com.limitlessmobility.iVendGateway.model.common;

import com.google.gson.Gson;

public class PaymentHistoryRequestModel {

	private int operationLocationId;
    private int customerId;
    private int customerLocationId;
    private String machineId;
    private String posId;
    private String paymentModeId;
    private String fromDate;
    private String toDate;                                                        
    private String transactionRefNo;
    private int pageSize;
    private String datatype;
    private int pageNo;
	public int getOperationLocationId() {
		return operationLocationId;
	}
	public void setOperationLocationId(int operationLocationId) {
		this.operationLocationId = operationLocationId;
	}
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public int getCustomerLocationId() {
		return customerLocationId;
	}
	public void setCustomerLocationId(int customerLocationId) {
		this.customerLocationId = customerLocationId;
	}
	public String getMachineId() {
		return machineId;
	}
	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}
	public String getPosId() {
		return posId;
	}
	public void setPosId(String posId) {
		this.posId = posId;
	}
	public String getPaymentModeId() {
		return paymentModeId;
	}
	public void setPaymentModeId(String paymentModeId) {
		this.paymentModeId = paymentModeId;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String getTransactionRefNo() {
		return transactionRefNo;
	}
	public void setTransactionRefNo(String transactionRefNo) {
		this.transactionRefNo = transactionRefNo;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public String getDatatype() {
		return datatype;
	}
	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
    
    
}
