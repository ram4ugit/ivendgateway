package com.limitlessmobility.iVendGateway.model.phonepe;

import com.google.gson.Gson;

public class Phoneperefundrequest {
    private String transactionid;
    
    private long amount;

	public final String getTransactionid() {
		return transactionid;
	}

	public final void setTransactionid(String transactionid) {
		this.transactionid = transactionid;
	}

	public final long getAmount() {
		return amount;
	}

	public final void setAmount(long amount) {
		this.amount = amount;
	}

	@Override
    public String toString() {
	    return new Gson().toJson(this);
    }


    
    
}
