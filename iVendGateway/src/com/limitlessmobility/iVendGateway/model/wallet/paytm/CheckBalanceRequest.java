package com.limitlessmobility.iVendGateway.model.wallet.paytm;

import com.google.gson.Gson;

public class CheckBalanceRequest {

	private CheckStatusHead head;
	private CheckStatusBody body;
	public CheckStatusHead getHead() {
		return head;
	}
	public void setHead(CheckStatusHead head) {
		this.head = head;
	}
	public CheckStatusBody getBody() {
		return body;
	}
	public void setBody(CheckStatusBody body) {
		this.body = body;
	}
	@Override
    public String toString() {
	    return new Gson().toJson(this);
    }
}