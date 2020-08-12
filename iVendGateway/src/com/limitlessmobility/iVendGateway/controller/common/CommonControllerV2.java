package com.limitlessmobility.iVendGateway.controller.common;

import com.limitlessmobility.iVendGateway.dao.common.CommonDao;
import com.limitlessmobility.iVendGateway.dao.common.CommonDaoImpl;
import com.limitlessmobility.iVendGateway.model.common.ManualRefundRequest;
import com.limitlessmobility.iVendGateway.paytm.model.PaytmRefundRequestModelFinal;

public class CommonControllerV2 {

	CommonDao cdao = new CommonDaoImpl();
	
	public PaytmRefundRequestModelFinal getRefundList(){
		PaytmRefundRequestModelFinal paytmRefundRequestModelFinal = new PaytmRefundRequestModelFinal();
		
		cdao.getTerminalForRefund();
				
		
		return paytmRefundRequestModelFinal;
	}

	public void getTerminalForRefund(){
		
		cdao.getTerminalForRefund();
	}
	
	public void autoRefundProcessV2(){
		
		cdao.commonRefundProcessV2();
	}
	public void directRefund(){
		
		cdao.directRefund();
	}
	public void checkStatusRefundProcess(){
		
		cdao.autoCheckStatusForRefund();
	}
	
	public String manualRefundProcess(ManualRefundRequest refundRequest){
		
		return cdao.manualRefundProcess(refundRequest);
	}
	public String manualRefund(ManualRefundRequest refundRequest){
		
		return cdao.manualRefund(refundRequest);
	}
}
