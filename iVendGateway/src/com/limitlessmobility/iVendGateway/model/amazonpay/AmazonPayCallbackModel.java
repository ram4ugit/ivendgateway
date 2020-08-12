package com.limitlessmobility.iVendGateway.model.amazonpay;

//import com.fasterxml.jackson.annotation.JsonProperty;

public class AmazonPayCallbackModel {

	////@JsonProperty(value = "Type")
	private String type;

	//@JsonProperty(value = "MessageId")
	private String messageId;

	//@JsonProperty(value = "TopicArn")
	private String topicArn;

	//@JsonProperty(value = "Message")
	private String message;

	//@JsonProperty(value = "Timestamp")
	private String timestamp;

	//@JsonProperty(value = "SignatureVersion")
	private String signatureVersion;

	//@JsonProperty(value = "Signature")
	private String signature;

	//@JsonProperty(value = "SigningCertURL")
	private String signingCertURL;

	//@JsonProperty(value = "UnsubscribeURL")
	private String unsubscribeURL;

	private String pspId;
	private String deviceId;
	private String merchantId;
	private String productName;
	private String serviceType;

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getPspId() {
		return pspId;
	}

	public void setPspId(String pspId) {
		this.pspId = pspId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getTopicArn() {
		return topicArn;
	}

	public void setTopicArn(String topicArn) {
		this.topicArn = topicArn;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getSignatureVersion() {
		return signatureVersion;
	}

	public void setSignatureVersion(String signatureVersion) {
		this.signatureVersion = signatureVersion;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getSigningCertURL() {
		return signingCertURL;
	}

	public void setSigningCertURL(String signingCertURL) {
		this.signingCertURL = signingCertURL;
	}

	public String getUnsubscribeURL() {
		return unsubscribeURL;
	}

	public void setUnsubscribeURL(String unsubscribeURL) {
		this.unsubscribeURL = unsubscribeURL;
	}

}
