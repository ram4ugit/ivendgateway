package com.limitlessmobility.iVendGateway.services.zeta;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.limitlessmobility.iVendGateway.controller.zeta.ZetaCallbackControllerV2;
import com.limitlessmobility.iVendGateway.model.zeta.ZetaCallbackTransactions;

@Controller
@RequestMapping("/payment/v2/transactions/zeta")
public class ZetaCallbackServiceV2 {

	private static final Logger logger = Logger.getLogger(ZetaCallbackService.class);

	String result = "";

	/**
	 * Method to call ZetaCallback Service for TRANSACTION - WAITING_FOR_CAPTURE
	 * Payload.
	 */

	@RequestMapping(value = "/initiate", method = RequestMethod.POST)
	@ResponseBody
	public String getZetaCallback(@RequestBody ZetaCallbackTransactions zetaCallbackTransactions) {
		logger.info("Service CallBack Method is calling!!");

		System.out.println("Service CallBack Method is calling!!");

		System.out.println("entity: " + zetaCallbackTransactions.getEntity());
		System.out.println("event: " + zetaCallbackTransactions.getEvent());
		System.out.println("purchase request id: " + zetaCallbackTransactions.getPayload().getPurchaseRequestId());
		System.out.println("purchase request time: " + zetaCallbackTransactions.getPayload().getPurchaseRequestTime());
		System.out
				.println("purchase transaction id:" + zetaCallbackTransactions.getPayload().getPurchaseTransactionId());
		System.out.println("transaction state:" + zetaCallbackTransactions.getPayload().getTransactionState());
		System.out.println("currency: " + zetaCallbackTransactions.getPayload().getAmount().getCurrency());
		System.out.println("amount value: " + zetaCallbackTransactions.getPayload().getAmount().getValue());
		System.out.println("capture time out: "
				+ zetaCallbackTransactions.getPayload().getCaptureInfo().getCaptureTimeoutInMillis());
		System.out.println("should auto capture: "
				+ zetaCallbackTransactions.getPayload().getCaptureInfo().getShouldAutoCapture());
		System.out.println("aid: " + zetaCallbackTransactions.getPayload().getMerchantInfo().getAid());
		System.out.println("mid: " + zetaCallbackTransactions.getPayload().getMerchantInfo().getMid());
		System.out.println("tid: " + zetaCallbackTransactions.getPayload().getMerchantInfo().getTid());
		System.out.println("payment reciept id: " + zetaCallbackTransactions.getMetaInfo().getPaymentReceiptId());
		System.out.println("psp id: " + zetaCallbackTransactions.getMetaInfo().getIfi());

		try {
			JSONObject jsonObject = new JSONObject(zetaCallbackTransactions);
			String str = jsonObject.toString();
			System.out.println("response in service: " + str);
			logger.info("response in service: " + str);
		} catch (Exception e) {

			e.printStackTrace();
		}

		try {
			ZetaCallbackControllerV2 zetaCallbackController = new ZetaCallbackControllerV2();

			result = zetaCallbackController.saveTransaction(zetaCallbackTransactions);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}

		/*
		 * JSONObject jsonObject = new JSONObject(); String result =
		 * jsonObject.put("msg", "success").toString();
		 */

		return result;

	}
}
