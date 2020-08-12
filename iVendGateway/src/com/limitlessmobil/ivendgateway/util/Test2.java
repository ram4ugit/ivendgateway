package com.limitlessmobil.ivendgateway.util;

import java.util.HashMap;
import java.util.Map;

public class Test2 {

	
	public static void main(String[] args) {
		
		String str = "mihpayid=10504661119&mode=DBQR&status=success&key=2f8SnB&txnid=123333&amount=1.00&addedon=2020-06-12+12%3A47%3A36&productinfo=Offline+Dynamic+QR&firstname=&lastname=&address1=&address2=&city=&state=&country=&zipcode=&email=&phone=&udf1=2700&udf2=01&udf3=&udf4=&udf5=&udf6=&udf7=&udf8=&udf9=&udf10=&card_token=&card_no=&field0=DYQ10504661119&field1=RAM+NARAYAN+ROY&field2=16368640439&field3=ramnarayanroy43%40okicici&field4=&field5=yellowqr.vendimanqr%40hdfcbank%21vendimanqr%21NA&field6=ICICI+Bank%21157801530427%21ICIC0001578%21918178547248&field7=APPROVED+OR+COMPLETED+SUCCESSFULLY%7C00&field8=&field9=SUCCESS%7CCompleted+Using+Callback&payment_source=payu&PG_TYPE=BQR&error=E000&error_Message=No+Error&net_amount_debit=1&unmappedstatus=captured&hash=a8ea9b6d0354df0800789ae4c019098d1b164bd19377ce6be4b09836d79fd1768d5a91a8d76ee0602c69ad9f212a28bce7867410cd15436417288f5d7a4f548e&bank_ref_no=016412481831&bank_ref_num=016412481831&bankcode=UPIDBQR&surl=https%3A%2F%2Fwww.payubiz.in%2F&curl=https%3A%2F%2Fwww.payubiz.in%2F&furl=https%3A%2F%2Fwww.payubiz.in%2F";
		String id = str.split("&")[1].split("=")[1];
		System.out.println(id);
		
		String string = "mihpayid=10504661119&mode=DBQR&status=success&key=2f8SnB&txnid=123333&amount=1.00&addedon=2020-06-12+12%3A47%3A36&productinfo=Offline+Dynamic+QR&firstname=&lastname=&address1=&address2=&city=&state=&country=&zipcode=&email=&phone=&udf1=2700&udf2=01&udf3=&udf4=&udf5=&udf6=&udf7=&udf8=&udf9=&udf10=&card_token=&card_no=&field0=DYQ10504661119&field1=RAM+NARAYAN+ROY&field2=16368640439&field3=ramnarayanroy43%40okicici&field4=&field5=yellowqr.vendimanqr%40hdfcbank%21vendimanqr%21NA&field6=ICICI+Bank%21157801530427%21ICIC0001578%21918178547248&field7=APPROVED+OR+COMPLETED+SUCCESSFULLY%7C00&field8=&field9=SUCCESS%7CCompleted+Using+Callback&payment_source=payu&PG_TYPE=BQR&error=E000&error_Message=No+Error&net_amount_debit=1&unmappedstatus=captured&hash=a8ea9b6d0354df0800789ae4c019098d1b164bd19377ce6be4b09836d79fd1768d5a91a8d76ee0602c69ad9f212a28bce7867410cd15436417288f5d7a4f548e&bank_ref_no=016412481831&bank_ref_num=016412481831&bankcode=UPIDBQR&surl=https%3A%2F%2Fwww.payubiz.in%2F&curl=https%3A%2F%2Fwww.payubiz.in%2F&furl=https%3A%2F%2Fwww.payubiz.in%2F";
		String[] couple = string.split("&");

		Map<String, String> testMap = new HashMap<String, String>();
		 for(int i =0; i < couple.length ; i++) {
			 try {
		    String[] items =couple[i].split("=");
		    testMap.put(items[0], items[1]);
		    System.out.println(items[0] +" = "+items[1]);
			 } catch(Exception e) {
				 
			 }
		    
		}
		 System.out.println(testMap);
		 System.out.println("The Value is: " + testMap.get("status"));
	}
	
}
