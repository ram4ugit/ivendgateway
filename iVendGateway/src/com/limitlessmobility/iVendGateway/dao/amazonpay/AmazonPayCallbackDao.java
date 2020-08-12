package com.limitlessmobility.iVendGateway.dao.amazonpay;

import com.limitlessmobility.iVendGateway.model.amazonpay.AmazonPayCallbackModel;

/**
 * DAO Class
 **/

public interface AmazonPayCallbackDao {

	public abstract boolean eventLogSave(AmazonPayCallbackModel amazonPayCallbackModel);

}
