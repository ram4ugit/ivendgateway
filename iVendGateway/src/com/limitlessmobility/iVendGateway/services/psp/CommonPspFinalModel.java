package com.limitlessmobility.iVendGateway.services.psp;

import java.util.List;

public class CommonPspFinalModel {

	private List<CommonPspGetResponse> available;
	private List<CommonPspGetResponse> selected;
	public List<CommonPspGetResponse> getAvailable() {
		return available;
	}
	public void setAvailable(List<CommonPspGetResponse> available) {
		this.available = available;
	}
	public List<CommonPspGetResponse> getSelected() {
		return selected;
	}
	public void setSelected(List<CommonPspGetResponse> selected) {
		this.selected = selected;
	}
	
	
	
}
