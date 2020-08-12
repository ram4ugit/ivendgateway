package com.limitlessmobility.iVendGateway.model.amazonpay;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Player {
	
	@JsonProperty("Type")
	private String Type;
	
	@JsonProperty("MessageId")
	private String MessageId;
	
	@JsonProperty("TopicArn")
	private String TopicArn;
	
	@JsonProperty("Message")
	private String Message;
	
	@JsonProperty("Timestamp")
	private String Timestamp;
	
	@JsonProperty("SignatureVersion")
	private String SignatureVersion;
	
	@JsonProperty("Signature")
	private String Signature;
	
	@JsonProperty("SigningCertURL")
	private String SigningCertURL;
	
	@JsonProperty("UnsubscribeURL")
	private String UnsubscribeURL;

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public String getMessageId() {
		return MessageId;
	}

	public void setMessageId(String messageId) {
		MessageId = messageId;
	}

	public String getTopicArn() {
		return TopicArn;
	}

	public void setTopicArn(String topicArn) {
		TopicArn = topicArn;
	}

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}

	public String getTimestamp() {
		return Timestamp;
	}

	public void setTimestamp(String timestamp) {
		Timestamp = timestamp;
	}

	public String getSignatureVersion() {
		return SignatureVersion;
	}

	public void setSignatureVersion(String signatureVersion) {
		SignatureVersion = signatureVersion;
	}

	public String getSignature() {
		return Signature;
	}

	public void setSignature(String signature) {
		Signature = signature;
	}

	public String getSigningCertURL() {
		return SigningCertURL;
	}

	public void setSigningCertURL(String signingCertURL) {
		SigningCertURL = signingCertURL;
	}

	public String getUnsubscribeURL() {
		return UnsubscribeURL;
	}

	public void setUnsubscribeURL(String unsubscribeURL) {
		UnsubscribeURL = unsubscribeURL;
	}
	
	

}
