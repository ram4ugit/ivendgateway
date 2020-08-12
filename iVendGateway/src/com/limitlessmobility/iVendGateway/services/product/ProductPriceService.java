package com.limitlessmobility.iVendGateway.services.product;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.limitlessmobil.ivendgateway.util.HttpStatusModal;
import com.limitlessmobility.iVendGateway.controller.product.ProductController;
import com.limitlessmobility.iVendGateway.model.product.ProductPriceDetailModel;
import com.limitlessmobility.iVendGateway.model.product.ProductPriceDetail;

@Controller
@RequestMapping("/v1/product")
public class ProductPriceService {

	@RequestMapping(value = "/getProductPrice", method = RequestMethod.POST)
	@ResponseBody
	public String getProductDetails(@RequestBody ProductPriceDetailModel productDetail) throws JSONException{
		
		ProductController productController = new ProductController();
		String productPriceDetail = productController.getProductPriceByCard(productDetail.getCardNo(), productDetail.getProductId(), productDetail.getAmount());
		JSONObject finalResponse = new JSONObject();
		JSONObject responseObjJson = new JSONObject();
		if(productPriceDetail.equalsIgnoreCase("true")){
			responseObjJson.put("status", HttpStatusModal.OK);
			finalResponse.put("responseObj", responseObjJson);
			finalResponse.put("message", "null");
			finalResponse.put("status", HttpStatusModal.OK);
		} else{
			responseObjJson.put("status", HttpStatusModal.ERROR);
			finalResponse.put("responseObj", responseObjJson);
			finalResponse.put("message", "null");
			finalResponse.put("status", "false");
		}
		
		
		return finalResponse.toString();
	}
	@RequestMapping(value = "/productPrice", method = RequestMethod.POST)
	@ResponseBody
	public String getProductDetailByMachine(@RequestBody ProductPriceDetailModel productDetail) throws JSONException{
		
		ProductController productController = new ProductController();
		ProductPriceDetail productPriceDetail = productController.getProductPriceByMachine(productDetail.getMachineId(), productDetail.getProductId());


		JSONObject finalResponse = new JSONObject();
		JSONObject responseObjJson = new JSONObject();
		responseObjJson.put("amount", productPriceDetail.getAmount());
		responseObjJson.put("status", productPriceDetail.getStatus());
		if(productPriceDetail.getStatus().equalsIgnoreCase("active")){
			finalResponse.put("responseObj", responseObjJson);
			finalResponse.put("message", "null");
			finalResponse.put("status", HttpStatusModal.OK);
		} else{
			finalResponse.put("responseObj", responseObjJson);
			finalResponse.put("message", "null");
			finalResponse.put("status", "false");
		}
		return finalResponse.toString();
	}
}
