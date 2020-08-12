package com.limitlessmobility.iVendGateway.services.product;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.limitlessmobility.iVendGateway.db.Util;
import com.limitlessmobility.iVendGateway.model.product.ProductPrice;

@Controller
@RequestMapping(value="/v1")
public class ProductService {

	@RequestMapping(value="/getProductPrice", method=RequestMethod.POST)
	@ResponseBody
	public List<ProductPrice> getProductPrice(@RequestBody ProductPrice productPriceModel) throws JSONException{
		List<ProductPrice> productPriceList = new ArrayList<ProductPrice>();
		try {
			String url = "https://lynx.nayax.com/Operational/v1/machines/698323128/machineProducts";
			
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// optional default is GET
			con.setRequestMethod("GET");

			//add request header
			con.setRequestProperty("Authorization", Util.authKeyReader());

			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'GET' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			//print result
			System.out.println(response.toString());
			
			
			// Load the data from json 
		     JSONArray jsonArray= new JSONArray(response.toString());
			
			boolean isEmpty = productPriceModel.getProductId() == null || productPriceModel.getProductId().trim().length() == 0;
			if (isEmpty) {
				for (int i = 0; i < jsonArray.length(); i++) {

			        JSONObject jsonObj = jsonArray.getJSONObject(i);

			        ProductPrice p = new ProductPrice();
			        p.setMachineId(jsonObj.getString("MachineID").toString());
			        p.setProductId(jsonObj.getString("PCCode"));
			        p.setPrice(jsonObj.getString("MachinePrice"));
			        /*String machineId = jsonObj.getString("MachineID");
			        String productId = jsonObj.getString("PCCode");
			        String price = jsonObj.getString("MachinePrice");
			        responseJsonMap.put(ean, nr);  // here you put ean as key and nr as value
	*/		        productPriceList.add(p);
			     }
			} else {
				for (int i = 0; i < jsonArray.length(); i++) {

			        JSONObject jsonObj = jsonArray.getJSONObject(i);

			        ProductPrice p = new ProductPrice();
			        p.setMachineId(jsonObj.getString("MachineID"));
			        p.setProductId(jsonObj.getString("PCCode"));
			        p.setPrice(jsonObj.getString("MachinePrice"));
			        /*String machineId = jsonObj.getString("MachineID");
			        String productId = jsonObj.getString("PCCode");
			        String price = jsonObj.getString("MachinePrice");
			        responseJsonMap.put(ean, nr);  // here you put ean as key and nr as value*/
			        
			        if(p.getProductId().equalsIgnoreCase(productPriceModel.getProductId())){
			        	productPriceList.add(p);
			        }
			        }
			}
//			HashMap<String, String> responseJsonMap = new HashMap<String, String>();
			
		     
		     
		} catch (IOException e) {
			System.out.println("Exception in DynamicQRService class.."+e);
			e.printStackTrace();
		}
		return productPriceList;
	}
	
}
