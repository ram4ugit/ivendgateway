package com.limitlessmobility.iVendGateway.controller.amazonpay;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.limitlessmobility.iVendGateway.dao.amazonpay.AmazonPayCallbackDao;
import com.limitlessmobility.iVendGateway.dao.amazonpay.AmazonPayCallbackDaoImpl;
import com.limitlessmobility.iVendGateway.dao.amazonpay.AmazonPayInitiationDao;
import com.limitlessmobility.iVendGateway.dao.amazonpay.AmazonPayInitiationDaoImpl;
import com.limitlessmobility.iVendGateway.model.amazonpay.AmazonPayCallbackModel;
import com.limitlessmobility.iVendGateway.paytm.model.PaymentTransaction;

public class AmazonPayCallbackController {
	private static final Logger logger = Logger.getLogger(AmazonPayCallbackController.class);

	/**
	 * Method to call Amazon Controller for TRANSACTION - WAITING_FOR_CAPTURE
	 * Payload.
	 * 
	 * @throws JSONException
	 */

	public String saveTransaction(AmazonPayCallbackModel amazonPayCallbackModel) throws JSONException {

		System.out.println("i am in AmazonPayCallbackController");

		try {
			AmazonPayCallbackDao amazonPayCallbackDao = new AmazonPayCallbackDaoImpl();
			amazonPayCallbackDao.eventLogSave(amazonPayCallbackModel);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
		

		PaymentTransaction callbackTransactions = new PaymentTransaction();

		amazonPayCallbackModel.setPspId("amazonpay");
		amazonPayCallbackModel.setDeviceId("ad1");
		amazonPayCallbackModel.setMerchantId("zoneone");
		amazonPayCallbackModel.setProductName("food");
		callbackTransactions.setTerminalId("A_P_T");
		/*callbackTransactions.setPspMerchantId());
		callbackTransactions.setProductPrice();
		callbackTransactions.setPspTransactionId()*/;

		// double authAmount =
		// Double.parseDouble(zetaCallbackTransactions.getPayload().getAmount().getValue());

		// callbackTransactions.setAuthAmount(authAmount);
		// callbackTransactions.setAuthDate();
		// callbackTransactions.setAuthTime();
		// callbackTransactions.setStatus());
		// callbackTransactions.setStatusCode();
		// callbackTransactions.setStatusMsg();
		// callbackTransactions.setOrderId();
		// callbackTransactions.setMerchantOrderId();
		amazonPayCallbackModel.setServiceType("CHECK_TXN_STATUS");

		try {

			AmazonPayInitiationDao amazonPayInitiationDao = new AmazonPayInitiationDaoImpl();
			amazonPayInitiationDao.saveTransaction(amazonPayCallbackModel);

		} catch (Exception e) {
			e.printStackTrace();

		}

		JSONObject responseObj = new JSONObject();

		responseObj.put("status", "SUCCESS");
		responseObj.put("message", "SUCCESS");

		return responseObj.toString();

	}

}
