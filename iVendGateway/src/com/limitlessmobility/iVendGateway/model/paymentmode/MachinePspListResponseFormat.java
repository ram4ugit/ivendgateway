package com.limitlessmobility.iVendGateway.model.paymentmode;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class MachinePspListResponseFormat {
	List<GetMachinePspResponse> pspList = new ArrayList<GetMachinePspResponse>();

	public List<GetMachinePspResponse> getPspList() {
		return pspList;
	}

	public void setPspList(List<GetMachinePspResponse> pspList) {
		this.pspList = pspList;
	}

	@Override
    public String toString() {
	    return new Gson().toJson(this);
    }
	
}
