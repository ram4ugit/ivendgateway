package com.limitlessmobility.iVendGateway.model.amazonpay;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Status {
	
	@JsonProperty("status")
	 private String status;
	
	@JsonProperty("description")
	 private String description;
	
	@JsonProperty("reasonCode")
	 private String reasonCode;


	 // Getter Methods 

	 public String getStatus() {
	  return status;
	 }

	 public String getDescription() {
	  return description;
	 }

	 public String getReasonCode() {
	  return reasonCode;
	 }

	 // Setter Methods 

	 public void setStatus(String status) {
	  this.status = status;
	 }

	 public void setDescription(String description) {
	  this.description = description;
	 }

	 public void setReasonCode(String reasonCode) {
	  this.reasonCode = reasonCode;
	 }
}
