package com.limitlessmobility.iVendGateway.services.phonepe;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.limitlessmobility.iVendGateway.dao.phonepe.PhonePeDao;
import com.limitlessmobility.iVendGateway.dao.phonepe.PhonePeDaoImpl;
import com.limitlessmobility.iVendGateway.db.DbConfigration;
import com.limitlessmobility.iVendGateway.db.DbConfigrationImpl;
import com.limitlessmobility.iVendGateway.model.phonepe.PhonePeCheckStatusModel;

@Controller
@RequestMapping("/v1/phonepe")
public class PhonePeDynamicCheckStatus {

	private static final Logger logger = Logger.getLogger(PhonePeDynamicCheckStatus.class);
	public static final String VERSION = "v1";
	public static final String PHONEPEKEY = "8289e078-be0b-484d-ae60-052f117f8deb";
	public static final String MERCHANTID = "UATMERCHANT9";
	public static final String QRSTR = "/v3/transaction";

	DbConfigration dbConfig = new DbConfigrationImpl();
	PhonePeDao pdao = new PhonePeDaoImpl();

	@RequestMapping(value = "/paymentCheckStatus", method = RequestMethod.POST)
	@ResponseBody
	private String getPhonepeCheckStatus(@RequestBody PhonePeCheckStatusModel checkStatusModel) {

		logger.info("posId.." + checkStatusModel.getPosId() + "...txnId..." + checkStatusModel.getTxnId()
				+ "...appId..." + checkStatusModel.getAppId() + "...pspId..." + checkStatusModel.getPspId()
				+ "...amount..." + checkStatusModel.getAmount());

		System.out.println("posId.." + checkStatusModel.getPosId() + "...txnId..." + checkStatusModel.getTxnId()
				+ "...appId..." + checkStatusModel.getAppId() + "...pspId..." + checkStatusModel.getPspId()
				+ "...amount..." + checkStatusModel.getAmount());

		String baseurl = "";

		String transactionId = checkStatusModel.getTxnId();

		JSONObject jsonObject = new JSONObject(checkStatusModel);

		System.out.println("A Request Data" + jsonObject.toString());

		System.out.println("txnId " + transactionId);
		String appId = checkStatusModel.getAppId();
		System.out.println("AppId " + appId);
		String terminalId = checkStatusModel.getPosId();
		System.out.println("terminalId " + terminalId);
		String merchantId = MERCHANTID;
		System.out.println("merchantId " + merchantId);

		String xveri = QRSTR.concat("/").concat(merchantId).concat("/").concat(transactionId).concat("/")
				.concat("status").concat(PHONEPEKEY).trim();

		System.out.println("xveri " + xveri);

		String xverify = (org.apache.commons.codec.digest.DigestUtils.sha256Hex(xveri).toUpperCase()).concat("###1")
				.trim();

		System.out.println("xverify " + xverify);

		/* ................ */

		String line = "";
		StringBuilder response = new StringBuilder();
		try {
			
			
			baseurl = ("https://mercury.phonepe.com/v3/transaction".concat("/").concat(merchantId).concat("/").concat(transactionId).concat("/")
					.concat("status")).trim();
			System.out.println("baseurl "+baseurl);

			/*String resturl = baseurl.concat("/").concat(merchantId).concat("/").concat(transactionId).concat("/")
					.concat("status").trim();
			System.out.println("resturl " + resturl);*/

			URL url = new URL(baseurl);

			HttpURLConnection connection = null;
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("x-verify", xverify);
			//connection.setRequestProperty("Content-Length", Integer.toString(resturl.getBytes().length));
			connection.setUseCaches(false);
			connection.setDoOutput(true);

			try (DataOutputStream wr = new DataOutputStream(
             
					connection.getOutputStream())) {
				System.out.println("coming here......");
				wr.writeBytes(baseurl);
			}

			int responseCode = connection.getResponseCode();
			System.out.println("responseCode is "+responseCode);

			InputStream is;

			if (responseCode == HttpURLConnection.HTTP_OK) {

				System.out.println("1");
				System.out.println("res code: " + responseCode);

				is = connection.getInputStream();

			} else {
				System.out.println("comming");
				is = connection.getErrorStream();

			}

			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			line = "";
			while ((line = rd.readLine()) != null) {
				response.append(line);

				System.out.println(response.length());

				System.out.append("\r");
			}

			System.out.println("PHONEPE CHECKSTATUS RESPONSE..." + response.toString());

			rd.close();
		} catch (IOException e) {
			System.out.println("Exception in Checkstatus Service class.." + e);
			e.printStackTrace();
		}

		/* ................. */

		return response.toString();

	}
}
