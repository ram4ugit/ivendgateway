package com.limitlessmobility.iVendGateway.services.psp;

import java.util.List;

import com.google.gson.Gson;

public class CommonPspModel {

	private String label;
	private String parentId;
	private String entityId;
	
	private List<CommonPspModelChild> data;
	
	public String getLabel() {
		return label;
	}



	public void setLabel(String label) {
		this.label = label;
	}



	public String getParentId() {
		return parentId;
	}



	public void setParentId(String parentId) {
		this.parentId = parentId;
	}



	public String getEntityId() {
		return entityId;
	}



	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}



	public List<CommonPspModelChild> getData() {
		return data;
	}



	public void setData(List<CommonPspModelChild> data) {
		this.data = data;
	}



	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}
