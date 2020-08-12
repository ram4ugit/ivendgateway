package com.limitlessmobility.iVendGateway.services.common;

import java.util.Calendar;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value="/v1")
public class ThreadService extends Thread{
//	AutoRefundService autoRefundService = new AutoRefundService();
//	AutoRefundServiceV2 autoRefundServiceV2 = new AutoRefundServiceV2();
	
	/*@RequestMapping(value = "/startThread", method = RequestMethod.GET)
	@ResponseBody
	public void startThread() throws Exception {
		ThreadService threadService = new ThreadService();
		threadService.waitMethod();
	}

	private synchronized void waitMethod() {
		 
		while (true) {
			System.out.println("always running program ==> " + Calendar.getInstance().getTime());
			autoRefundService.checkStatusRefundProcess();
			autoRefundService.getRefundList();
			
			autoRefundServiceV2.autoRefundProcess();
//			autoRefundService.autoRefundProcess();
			try {
				this.wait(15*60*1000);
			} catch (InterruptedException e) {
 
				e.printStackTrace();
			}
		}
 
	}*/
	
	@RequestMapping(value = "/startRefundThread", method = RequestMethod.GET)
	@ResponseBody
	public String startRefundTest(){
		ThreadServiceV2 t = new ThreadServiceV2();
		t.scheduleFixedDelayTask();
		return "Success";
	}
	
	
	/*@Scheduled(cron = "0 * * * * ?")
	public void scheduleTaskWithCronExpression() {
	    System.out.println("Cron Task :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()));
	}*/
	
	/*@RequestMapping(value = "/autoRefundTest", method = RequestMethod.GET)
	@ResponseBody
	public String autoRefundTest() {
		try{
			autoRefundService.autoRefundProcess();
		}catch(Exception e) {
			System.out.println(e);
		}
		return "SUCCESS";
	}*/
}
