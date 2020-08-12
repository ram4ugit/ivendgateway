package com.limitlessmobility.iVendGateway.model.common;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class RefundStatusRequest {

	List<RefundSatusModel> refundStatusList = new ArrayList<RefundSatusModel>();

	public List<RefundSatusModel> getRefundStatusList() {
		return refundStatusList;
	}

	public void setRefundStatusList(List<RefundSatusModel> refundStatusList) {
		this.refundStatusList = refundStatusList;
	}

	@Override
    public String toString() {
	    return new Gson().toJson(this);
    }
}
