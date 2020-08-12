package com.limitlessmobility.iVendGateway.services.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.scheduling.annotation.Scheduled;


public class ThreadServiceV2 {

	AutoRefundService autoRefundService = new AutoRefundService();
	AutoRefundServiceV2 autoRefundServiceV2 = new AutoRefundServiceV2();
	
	@Scheduled(fixedDelay = 1000*60*15)
	public void scheduleFixedDelayTask() {
		try{
		Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        System.out.println("Thread Start Time= "+sdf.format(cal.getTime()));
        autoRefundService.checkStatusRefundProcess();
		
        
        
        autoRefundService.getRefundList();
		
		//Select terminal_id from payment_refund
		//check terminal version (if terminal found in terminal table then terminal is v1
		//if(terminal_id=v1){
		
		try{
			autoRefundServiceV2.autoRefundProcess();
		} catch(Exception e){
			System.out.println("Thread Exception... v2.. "+e);
		}
		
		
		try{
			autoRefundService.autoRefundProcess();
		} catch(Exception e){
			System.out.println("Thread Exception...  v1.. "+e);
		}
		try{
			autoRefundServiceV2.directRefund();
		} catch(Exception e){
			
		}
		System.out.println("Thread End Time= "+ sdf.format(cal.getTime()) );
		} catch(Exception e){
			System.out.println("Thread Exception... "+e);
		}
	}
	
}
