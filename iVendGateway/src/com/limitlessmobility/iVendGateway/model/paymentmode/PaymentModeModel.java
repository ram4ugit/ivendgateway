package com.limitlessmobility.iVendGateway.model.paymentmode;

public class PaymentModeModel {

	private String paymentModeId;
	private String paymentModelName;
	private String isChild;
	private int type;
	private int isStatic;
	private String qrCode;
	private String message;
	private String terminalId;
	private String refundMessage;
	private String keyboardType;
	private int productSelectionTime;
	private int paymentProcessTime;
	private int vendingTime;
	private int walletAlertTime;
	private int pspListRefreshTime;
	private int hurryUpTime;
	private int checkStatusTime;
	private String msg1;
	private String msg2;
	private String msg3;
	private int transactionPostTime;
	private boolean image;
	private String dateTime;
	
	public String getPaymentModeId() {
		return paymentModeId;
	}
	public void setPaymentModeId(String paymentModeId) {
		this.paymentModeId = paymentModeId;
	}
	public String getPaymentModelName() {
		return paymentModelName;
	}
	public void setPaymentModelName(String paymentModelName) {
		this.paymentModelName = paymentModelName;
	}
	public String getIsChild() {
		return isChild;
	}
	public void setIsChild(String isChild) {
		this.isChild = isChild;
	}
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getIsStatic() {
		return isStatic;
	}
	public void setIsStatic(int isStatic) {
		this.isStatic = isStatic;
	}
	public String getQrCode() {
		return qrCode;
	}
	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}
	public String getRefundMessage() {
		return refundMessage;
	}
	public void setRefundMessage(String refundMessage) {
		this.refundMessage = refundMessage;
	}
	public String getKeyboardType() {
		return keyboardType;
	}
	public void setKeyboardType(String keyboardType) {
		this.keyboardType = keyboardType;
	}
	public int getProductSelectionTime() {
		return productSelectionTime;
	}
	public void setProductSelectionTime(int productSelectionTime) {
		this.productSelectionTime = productSelectionTime;
	}
	public int getPaymentProcessTime() {
		return paymentProcessTime;
	}
	public void setPaymentProcessTime(int paymentProcessTime) {
		this.paymentProcessTime = paymentProcessTime;
	}
	public int getVendingTime() {
		return vendingTime;
	}
	public void setVendingTime(int vendingTime) {
		this.vendingTime = vendingTime;
	}
	public int getWalletAlertTime() {
		return walletAlertTime;
	}
	public void setWalletAlertTime(int walletAlertTime) {
		this.walletAlertTime = walletAlertTime;
	}
	public int getPspListRefreshTime() {
		return pspListRefreshTime;
	}
	public void setPspListRefreshTime(int pspListRefreshTime) {
		this.pspListRefreshTime = pspListRefreshTime;
	}
	public int getHurryUpTime() {
		return hurryUpTime;
	}
	public void setHurryUpTime(int hurryUpTime) {
		this.hurryUpTime = hurryUpTime;
	}
	public int getCheckStatusTime() {
		return checkStatusTime;
	}
	public void setCheckStatusTime(int checkStatusTime) {
		this.checkStatusTime = checkStatusTime;
	}
	public String getMsg1() {
		return msg1;
	}
	public void setMsg1(String msg1) {
		this.msg1 = msg1;
	}
	public String getMsg2() {
		return msg2;
	}
	public void setMsg2(String msg2) {
		this.msg2 = msg2;
	}
	public String getMsg3() {
		return msg3;
	}
	public void setMsg3(String msg3) {
		this.msg3 = msg3;
	}
	public int getTransactionPostTime() {
		return transactionPostTime;
	}
	public void setTransactionPostTime(int transactionPostTime) {
		this.transactionPostTime = transactionPostTime;
	}
	
	public boolean isImage() {
		return image;
	}
	public void setImage(boolean image) {
		this.image = image;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	
}
