package com.limitlessmobility.iVendGateway.controller.product;

import com.limitlessmobility.iVendGateway.dao.product.ProductDao;
import com.limitlessmobility.iVendGateway.dao.product.ProductDaoImpl;
import com.limitlessmobility.iVendGateway.model.product.ProductPriceDetail;

public class ProductController {

	ProductDao productDao = new ProductDaoImpl();
	
	public String getProductPriceByCard(String cardNo, String productId, String amount){
		
		String p = productDao.getProductPriceByCard(cardNo, productId, amount);
		
		return p;
	}
	public ProductPriceDetail getProductPriceByMachine(String machineId, String productId){
		
		ProductPriceDetail p = productDao.getProductPriceByMachine(machineId, productId);
		
		return p;
	}
}
