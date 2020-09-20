package com.limitlessmobility.iVendGateway.services.amazonpay;

import java.io.IOException;
import java.io.StringReader;

import javax.ws.rs.Consumes;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.limitlessmobility.iVendGateway.dao.EventLogDao;
import com.limitlessmobility.iVendGateway.dao.TransactionDao;
import com.limitlessmobility.iVendGateway.dao.TransactionDaoImpl;
import com.limitlessmobility.iVendGateway.dao.eventLogDaoImpl;
import com.limitlessmobility.iVendGateway.dao.amazonpay.AmazonDaoImpl;
import com.limitlessmobility.iVendGateway.dao.amazonpay.AmazonPayDao;
import com.limitlessmobility.iVendGateway.model.amazonpay.AmazonPaymentModel;
import com.limitlessmobility.iVendGateway.paytm.model.EventLogs;
import com.limitlessmobility.iVendGateway.paytm.model.PaymentTransaction;

@Controller
@RequestMapping("/payment/v1/transactions/amazonpay")
public class AmazonPayCallbackService{

	//private static final Logger logger = Logger.getLogger(AmazonPayCallbackServiceR.class);

	/*
	 * THis API is used to store Amazonpay Callback. Called by Amazonpay directly.
	 */
	@RequestMapping(value = "/initiate", method = RequestMethod.POST)
	@ResponseBody
	@Consumes("text/plain")
	public String chargeNotification(@RequestBody String amazonPayCallbackModel)
			throws JSONException, SAXException, IOException, ParserConfigurationException {

		System.out.println("i am in AmazonPayCallbackService");
		
		System.out.println("incoming Data "+amazonPayCallbackModel);
		
		boolean isexist = true;
		
		EventLogs eventLogs=new EventLogs();
		AmazonPayDao amazonPayDao = new AmazonDaoImpl();
		
		eventLogs.setEventDetails(amazonPayCallbackModel);
		
		
		JSONObject jsonObject = new JSONObject(amazonPayCallbackModel);
		System.out.println("Incoming Data in Json " + jsonObject.toString());

		String jstr = jsonObject.getString("Message");

		System.out.println("Message " + jstr);

        AmazonPaymentModel  amazonPaymentModel=new AmazonPaymentModel(); 
		JSONObject jsonObject2 = new JSONObject(jstr);

		System.out.println("ReleaseEnvironment " + jsonObject2.getString("ReleaseEnvironment"));

		System.out.println("MarketplaceID " + jsonObject2.getString("MarketplaceID"));

		System.out.println("Version " + jsonObject2.getString("Version"));

		System.out.println("NotificationType " + jsonObject2.getString("NotificationType"));
		System.out.println("SellerId " + jsonObject2.getString("SellerId"));
		//amazonPaymentModel.setSellerId(jsonObject2.getString("SellerId"));

		System.out.println("Notification ReferenceId " + jsonObject2.getString("NotificationReferenceId"));

		System.out.println("Timestamp " + jsonObject2.getString("Timestamp"));

		System.out.println("NotificationData = " + jsonObject2.getString("NotificationData"));

		String xml = jsonObject2.getString("NotificationData");

		PaymentTransaction paymentTransaction = new PaymentTransaction();

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new InputSource(new StringReader(xml)));

			// optional, but recommended
			// read this -
			// http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();

			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

			NodeList nList = doc.getElementsByTagName("ChargeTransactionNotification");

			System.out.println("----------------------------");

			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);

				System.out.println("\nCurrent Element :" + nNode.getNodeName());

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;

					System.out
							.println("ChargeTransactionDetails : " + eElement.getAttribute("ChargeTransactionDetails"));
					System.out
							.println(" OrderID : " + eElement.getElementsByTagName("OrderID").item(0).getTextContent());
					
					
					amazonPaymentModel.setOrderId(eElement.getElementsByTagName("OrderID").item(0).getTextContent());
					//paymentTransaction.setPspTransactionId(eElement.getElementsByTagName("OrderID").item(0).getTextContent());
					System.out.println("MerChantOrderID "+amazonPaymentModel.getOrderId());
					
					System.out.println(" SellerReferenceId : "+ eElement.getElementsByTagName("SellerReferenceId").item(0).getTextContent());
					
					paymentTransaction.setOrderId(eElement.getElementsByTagName("SellerReferenceId").item(0).getTextContent());
					paymentTransaction.setPspTransactionId(eElement.getElementsByTagName("SellerReferenceId").item(0).getTextContent());
					
					System.out.println(" Amount : " + eElement.getElementsByTagName("Amount").item(1).getTextContent());
					
					amazonPaymentModel.setAmount(eElement.getElementsByTagName("Amount").item(1).getTextContent());
					
					System.out.println(" CurrencyCode : "
							+ eElement.getElementsByTagName("CurrencyCode").item(0).getTextContent());
					
					System.out
							.println(" TotalFee : " + eElement.getElementsByTagName("Amount").item(0).getTextContent());
					
					System.out.println(" CurrencyCode : "
							+ eElement.getElementsByTagName("CurrencyCode").item(0).getTextContent());
					
					System.out.println("CreationTimestamp : "
							+ eElement.getElementsByTagName("CreationTimestamp").item(0).getTextContent());
					
					amazonPaymentModel.setStatus(eElement.getElementsByTagName("State").item(0).getTextContent());
					//The status in IPN will be Completed  for successful transaction and Declined for unsuccessful transaction.
					System.out.println("State : " +amazonPaymentModel.getStatus());
					
					System.out.println("LastUpdateTimestamp : "
							+ eElement.getElementsByTagName("LastUpdateTimestamp").item(0).getTextContent());
					/*System.out.println(
							"ReasonCode : " + eElement.getElementsByTagName("ReasonCode").item(0).getTextContent());
					amazonPaymentModel.setStatusCode(eElement.getElementsByTagName("ReasonCode").item(0).getTextContent());
					System.out.println("ReasonDescription : "
							+ eElement.getElementsByTagName("ReasonDescription").item(0).getTextContent());
					amazonPaymentModel.setStatusMsg(eElement.getElementsByTagName("ReasonDescription").item(0).getTextContent());*/
				}
			}

		} catch (Exception de) {
			de.printStackTrace();
		}

		try {
			System.out.println("i am going with request");

			//jsonReturn = amazonPayCallbackController.saveTransaction(amazonPayCallbackModel);
			System.out.println("i am returning with response");

		} catch (Exception ace) {
			ace.printStackTrace();
		}

		/* Save to Transaction table............ */
		try {
			isexist = amazonPayDao.checkTxnId(paymentTransaction.getOrderId());
			if (isexist) {
				System.out.println(paymentTransaction.getOrderId()+" Already Exist Previously " );
			}
			else {
			 paymentTransaction.setMerchantId("vendiman");
			 System.out.println("MerchantId "+paymentTransaction.getMerchantId());
			 paymentTransaction.setPspMerchantId(jsonObject2.getString("SellerId"));
			 System.out.println("PspMerchantId "+jsonObject2.getString("SellerId"));
			/* paymentTransaction.setOrderId(amazonPaymentModel.getOrderId());*/
			 paymentTransaction.setMerchantOrderId(amazonPaymentModel.getOrderId());
			 System.out.println("MerchantOrderId "+paymentTransaction.getMerchantOrderId());
			 paymentTransaction.setAuthAmount(Double.valueOf(amazonPaymentModel.getAmount()));
			 System.out.println("Amount "+paymentTransaction.getAuthAmount());
			 paymentTransaction.setTransactionType("0");
			 paymentTransaction.setStatus(amazonPaymentModel.getStatus());
			 paymentTransaction.setServiceType("CHECK_TXN_STATUS");
			 System.out.println("ServiceType "+paymentTransaction.getServiceType());
			 paymentTransaction.setPspId("amazonpay");
			 System.out.println("PspId "+paymentTransaction.getPspId());
			// paymentTransaction.setTransactionType(amazonPayCallbackModel.getType());
			 paymentTransaction.setDeviceId("Amazon-D");
			 System.out.println("DeviceId "+paymentTransaction.getDeviceId());
			 paymentTransaction.setTerminalId("Amazon-Terminal");
			 System.out.println("TerminalId "+paymentTransaction.getTerminalId());
			 paymentTransaction.setMerchantId("zoneone");
			 paymentTransaction.setAuthDate(jsonObject2.getString("Timestamp"));
			 System.out.println("AuthDate "+paymentTransaction.getAuthDate());
			// paymentTransaction.setPspTransactionId(paymentTransaction.getOrderId());
			 System.out.println("PspTransactionId "+paymentTransaction.getPspTransactionId());
			 
			 
			 TransactionDao transactionDao = new TransactionDaoImpl();
			 transactionDao.saveTransaction(paymentTransaction);
			 
		    } 
		}catch (Exception pte) {
			pte.printStackTrace();
		}
		 try {
			EventLogDao dao=new eventLogDaoImpl();
			 dao.saveEventLog(eventLogs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		 
		    JSONObject finalresponse = new JSONObject();
		    finalresponse.put("message", "null");
		    finalresponse.put("status", "SUCCESS");
		 
			JSONObject responseObj = new JSONObject();
			responseObj.put("txn_amount",  amazonPaymentModel.getAmount());
			responseObj.put("statusMessage", "SUCCESS");
			responseObj.put("status", "SUCCESS");
			responseObj.put("statusCode", "SS_200");

			//jsonObject.put("responseObj", responseObj).toString();
			finalresponse.put("responseObj", responseObj);

			System.out.println("RES   " + finalresponse.toString());

			return finalresponse.toString();
	}

}