package com.limitlessmobility.iVendGateway.services.payu;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.limitlessmobil.ivendgateway.util.HttpStatusModal;
import com.limitlessmobility.iVendGateway.db.DbConfigration;
import com.limitlessmobility.iVendGateway.db.DbConfigrationImpl;
import com.limitlessmobility.iVendGateway.model.common.CommonCheckStatusReponse;
import com.limitlessmobility.iVendGateway.model.common.CommonCheckStatusRequest;

@Controller
@RequestMapping(value="v2/payu")
public class PayuCheckStatusService {

	DbConfigration dbConfig = new DbConfigrationImpl();
	
	@RequestMapping(value = "/checkStatus", method = RequestMethod.POST)
	@ResponseBody
	public CommonCheckStatusReponse checkStatusUsingDB(@RequestBody CommonCheckStatusRequest checkStatusRequest) {
		System.out.println("payu checkstatus service v2 "+checkStatusRequest.toString());
		CommonCheckStatusReponse response = new CommonCheckStatusReponse();

		try {

		boolean isConnected = true;
		Connection con = dbConfig.getCon();
		if (con == null) {
			isConnected = false;
		}

		try {

			if (isConnected) {
				System.out.println("Connection Object Created...");



					String query = "SELECT psp_transaction_id,order_id, auth_amount, paid_amount FROM payment_transactions WHERE order_id='"+checkStatusRequest.getTxnId()+"'";

					System.out.println("psptid : " +query);

					Statement stmt = con.createStatement();

					ResultSet rs = stmt.executeQuery(query);

					try {
						if (rs.next()) {

							response.setAmount(Double.parseDouble(rs.getString("auth_amount")));
							response.setStatus("success");
							response.setStatusCode("success");
							response.setStatusMessage("success");
						} else {
							//response.setAmount(Double.parseDouble(rs.getString("auth_amount")));
							response.setStatus("failure");
							response.setStatusCode("failure");
							response.setStatusMessage("failure");
						}

					} catch (Exception e) {

						System.out.println(e);
					}
			}
		} catch(Exception e) {
			
		}
} catch(Exception e) {
			
		}

		return response;
	}

	@RequestMapping(value = "/checkStatusUsingAPI", method = RequestMethod.POST)
	@ResponseBody
	public String checkStatusUsingAPI(@RequestBody String request) {
		String line="";
		StringBuilder response=new StringBuilder();
		try {
			String urlParameters = request;
//			String urlParameters = "key=vDy3i7&command=check_bqr_txn_status&hash=026bef8eb3df509194440235882af006af419747f721ea27a0cc757b4337106f54c67c654e27b39476194a2a23987e013ae9a63461e1e8cbe4fbf5a05d220a7c&var1={\"12345\"}";
			FileOutputStream out = null;
			String hash = "056d41942cc3c9a2531a8596d81f4121e830a517765bf07f68b9382012f9a349749f40e77dae4f3e455df8b821f38be9cbec84395a7ca51a600ee72a050ea501";
//			System.out.println(cdao.getPspMerchantKey(pspId,terminalId));
//			URL url = new URL("https://mobiletest.payu.in/merchant/postservice.php");
			
			byte[] postData = urlParameters.getBytes( StandardCharsets.UTF_8 );
			int postDataLength = postData.length;
			URL url = new URL("https://mobiletest.payu.in/merchant/postservice.php");
			HttpURLConnection conn= (HttpURLConnection) url.openConnection();           
			conn.setDoOutput(true);
			conn.setInstanceFollowRedirects(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
			conn.setRequestProperty("charset", "utf-8");
			
			conn.setRequestProperty("key", "vDy3i7");
			conn.setRequestProperty("hash", "a9c9e1c8eb1f02ac1a726a463645ade8494bdfd44999c72545678995569d8ca8f6ce29abd99f985f048e6138a93bb833648ea42cb2272a29e423bf82b52a5f24");
			conn.setRequestProperty("Content-Length", Integer.toString(postDataLength ));
			conn.setUseCaches(false);
			try(DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
			   wr.write( postData );
			}
			
			int responseCode = conn.getResponseCode();

			InputStream is;

	        if(responseCode == HttpURLConnection.HTTP_OK){

	        is = conn.getInputStream();

	        }else {

	        is = conn.getErrorStream();

	        }
	              
              BufferedReader rd = new BufferedReader(new InputStreamReader(is));
              line="";
              while((line = rd.readLine()) != null) {
	              response.append(line);
	              System.out.println("RESPONSE From PayTm..."+response.toString());
	              System.out.append("\r");
              }
	              
	     // resize smaller by 50%
//            double percent = 0.5;
//            ImageResizer.resize(imagePath, imagePath, percent);
            
              rd.close(); is.close();    
	       
            return response.toString();
		} catch (IOException e) {
			System.out.println("Exception in DynamicQRService class.."+e);
			e.printStackTrace();
		}
		System.out.println(response);
		return response.toString();
	}
}