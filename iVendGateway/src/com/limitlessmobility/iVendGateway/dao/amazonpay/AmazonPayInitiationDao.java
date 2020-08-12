package com.limitlessmobility.iVendGateway.dao.amazonpay;

import com.limitlessmobility.iVendGateway.model.amazonpay.AmazonPayCallbackModel;

public interface AmazonPayInitiationDao {

	public abstract boolean saveTransaction(AmazonPayCallbackModel amazonPayCallbackModel);

}
