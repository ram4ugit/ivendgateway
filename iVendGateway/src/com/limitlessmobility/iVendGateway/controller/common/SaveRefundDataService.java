package com.limitlessmobility.iVendGateway.controller.common;

import org.springframework.stereotype.Repository;

import com.limitlessmobility.iVendGateway.model.common.ManualRefundRequestData;
import com.limitlessmobility.iVendGateway.model.common.RefundData;

@Repository
public interface SaveRefundDataService {
	
	boolean saveRefundData(RefundData refundData);

//	boolean manualRefundNew(ManualRefundRequestData refundData);
}
