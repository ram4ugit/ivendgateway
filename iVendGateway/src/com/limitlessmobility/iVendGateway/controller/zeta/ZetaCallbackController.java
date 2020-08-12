package com.limitlessmobility.iVendGateway.controller.zeta;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.limitlessmobility.iVendGateway.dao.zeta.ZetaCallbackDao;
import com.limitlessmobility.iVendGateway.dao.zeta.ZetaCallbackDaoImpl;
import com.limitlessmobility.iVendGateway.dao.zeta.ZetaCallbackInitiationDao;
import com.limitlessmobility.iVendGateway.dao.zeta.ZetaCallbackInitiationDaoImpl;
import com.limitlessmobility.iVendGateway.model.zeta.ZetaCallbackTransactions;
import com.limitlessmobility.iVendGateway.paytm.model.PaymentTransaction;
//import com.limitlessmobility.iVendGateway.paytm.model.callbackTransactions;

public class ZetaCallbackController {
	private static final Logger logger = Logger.getLogger(ZetaCallbackController.class);

	/**
	 * Method to call ZetaCallback Controller for TRANSACTION -
	 * WAITING_FOR_CAPTURE Payload.
	 * 
	 * @throws JSONException
	 */

	public String saveTransaction(ZetaCallbackTransactions zetaCallbackTransactions) throws JSONException {

		System.out.println("Controller CallBack Method is calling!!");

		try {
			ZetaCallbackDao zetaCallbackDao = new ZetaCallbackDaoImpl();

			zetaCallbackDao.eventLogSave(zetaCallbackTransactions);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}

		PaymentTransaction callbackTransactions = new PaymentTransaction();

		// check psp id is null or not here...

		if (zetaCallbackTransactions.getMetaInfo().getIfi() == "null"
				|| zetaCallbackTransactions.getMetaInfo().getIfi() == null
				|| zetaCallbackTransactions.getMetaInfo().getIfi() == ""
				|| zetaCallbackTransactions.getMetaInfo().getIfi().equals("Sodexo")) {
			callbackTransactions.setPspId(zetaCallbackTransactions.getMetaInfo().getIfi().trim());
			System.out.println("psp id not null: " + callbackTransactions.getPspId());

		} else {
			callbackTransactions.setPspId("zeta");
			System.out.println("psp id null: " + callbackTransactions.getPspId());

		}

		// check psp transaction id is null or not here...

		// if (zetaCallbackTransactions.getMetaInfo().getPaymentReceiptId() !=
		// null
		// && zetaCallbackTransactions.getMetaInfo().getPaymentReceiptId() !=
		// "") {
		// callbackTransactions
		// .setPspTransactionId(zetaCallbackTransactions.getMetaInfo().getPaymentReceiptId().trim());
		// System.out.println("psp transaction id is not null: " +
		// callbackTransactions.getPspTransactionId());
		//
		// } else {
		callbackTransactions
				.setPspTransactionId(zetaCallbackTransactions.getPayload().getPurchaseTransactionId().trim());
		System.out.println("psp transaction id is null: " + callbackTransactions.getPspTransactionId());

		// }

		callbackTransactions.setDeviceId("zd1");
		callbackTransactions.setTerminalId(zetaCallbackTransactions.getPayload().getMerchantInfo().getTid());
		callbackTransactions.setMerchantId("zoneone");

		callbackTransactions.setPspMerchantId(zetaCallbackTransactions.getPayload().getMerchantInfo().getMid());

		callbackTransactions.setProductName("productname");

		callbackTransactions.setProductPrice(zetaCallbackTransactions.getPayload().getAmount().getValue());

		double authAmount = Double.parseDouble(zetaCallbackTransactions.getPayload().getAmount().getValue());

		callbackTransactions.setAuthAmount(authAmount);
		callbackTransactions.setAuthDate(zetaCallbackTransactions.getPayload().getPurchaseRequestTime());
		callbackTransactions.setAuthTime(zetaCallbackTransactions.getPayload().getPurchaseRequestTime());
		callbackTransactions.setStatus(zetaCallbackTransactions.getStatus());
		callbackTransactions.setStatusCode(zetaCallbackTransactions.getStatusCode());
		callbackTransactions.setStatusMsg(zetaCallbackTransactions.getPayload().getTransactionState());
		callbackTransactions.setOrderId(zetaCallbackTransactions.getPayload().getPurchaseTransactionId());
		callbackTransactions.setMerchantOrderId(zetaCallbackTransactions.getPayload().getPurchaseTransactionId());
		callbackTransactions.setServiceType("CHECK_TXN_STATUS");

		try {
			// TransactionDao transactionDao = new TransactionDaoImpl();
			// transactionDao.saveTransaction(callbackTransactions);

			ZetaCallbackInitiationDao zetaCallbackInitiationDao = new ZetaCallbackInitiationDaoImpl();

			zetaCallbackInitiationDao.saveTransaction(callbackTransactions, zetaCallbackTransactions);

		} catch (Exception e) {
			e.printStackTrace();

		}

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("message", "null");
		jsonObject.put("status", "SUCCESS");

		JSONObject responseObj = new JSONObject();
		responseObj.put("txn_amount", zetaCallbackTransactions.getPayload().getAmount().getValue());
		responseObj.put("statusMessage", "SUCCESS");
		responseObj.put("status", "SUCCESS");
		responseObj.put("statusCode", "SS_200");

		jsonObject.put("responseObj", responseObj).toString();

		System.out.println("RES   " + jsonObject.toString());

		return jsonObject.toString();

	}

}
