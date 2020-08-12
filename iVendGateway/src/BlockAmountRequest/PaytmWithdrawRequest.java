package BlockAmountRequest;

import com.google.gson.Gson;

public class PaytmWithdrawRequest {
	private String mid;
	private String merchantKey;
	private String preAuthId;
	private String reqType;
	private String txnAmount;
	private String appIP;
	private String orderId;
	private String currency;
	private String deviceId;//This needs to be the mobile number of the customer
	private String ssoToken;
	private String paymentMode;
	private String custId;
	private String industryType;
	private String channel;
	private String authMode;
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getMerchantKey() {
		return merchantKey;
	}
	public void setMerchantKey(String merchantKey) {
		this.merchantKey = merchantKey;
	}
	public String getPreAuthId() {
		return preAuthId;
	}
	public void setPreAuthId(String preAuthId) {
		this.preAuthId = preAuthId;
	}
	public String getReqType() {
		return reqType;
	}
	public void setReqType(String reqType) {
		this.reqType = reqType;
	}
	public String getTxnAmount() {
		return txnAmount;
	}
	public void setTxnAmount(String txnAmount) {
		this.txnAmount = txnAmount;
	}
	public String getAppIP() {
		return appIP;
	}
	public void setAppIP(String appIP) {
		this.appIP = appIP;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getSsoToken() {
		return ssoToken;
	}
	public void setSsoToken(String ssoToken) {
		this.ssoToken = ssoToken;
	}
	public String getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public String getIndustryType() {
		return industryType;
	}
	public void setIndustryType(String industryType) {
		this.industryType = industryType;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getAuthMode() {
		return authMode;
	}
	public void setAuthMode(String authMode) {
		this.authMode = authMode;
	}
	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}
