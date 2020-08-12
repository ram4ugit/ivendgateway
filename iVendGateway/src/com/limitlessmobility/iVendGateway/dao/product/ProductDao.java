package com.limitlessmobility.iVendGateway.dao.product;

import com.limitlessmobility.iVendGateway.model.product.ProductPriceDetail;

public interface ProductDao {

	public abstract String getProductPriceByCard(String cardNo, String productId, String amount);
	public abstract ProductPriceDetail getProductPriceByMachine(String machineId, String productId);
}
