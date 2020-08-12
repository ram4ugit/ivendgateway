package com.limitlessmobility.iVendGateway.dao.common;

import com.limitlessmobility.iVendGateway.model.common.PaymentStatusRequest;
import com.limitlessmobility.iVendGateway.model.common.PaymentStatusResponse;

public interface PaymentStatusDao {

	public String paymentStatus(PaymentStatusRequest p);
}
