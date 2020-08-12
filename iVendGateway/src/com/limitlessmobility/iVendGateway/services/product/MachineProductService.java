package com.limitlessmobility.iVendGateway.services.product;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.limitlessmobility.iVendGateway.db.DbConfigration;
import com.limitlessmobility.iVendGateway.db.DbConfigrationImpl;
import com.limitlessmobility.iVendGateway.model.product.MachineProductModel;

@Controller
@RequestMapping("/v1")
public class MachineProductService {

	private static final Logger logger = Logger.getLogger(MachineProductService.class);

	String result = "";
	String tid = "";
	String deviceId = "";
	String machineId = "";
	String response = "";
	String jsonRes = "";
	String paCode = "";
	double machinePrice;

	JSONArray jsonArray = null;

	DbConfigration dbConfig = new DbConfigrationImpl();

	@RequestMapping(value = "/machineproduct", method = RequestMethod.POST)
	@ResponseBody
	public String getMachineProduct(@RequestBody MachineProductModel machineProductModel) throws JSONException {
		logger.info("Service getMachineProduct Method is calling!!");
		System.out.println("Service getMachineProduct Method is calling!!");

		tid = machineProductModel.getTerminalId().trim();
		System.out.println("terminalId: " + tid);

		paCode = machineProductModel.getPaCode();
		System.out.println("PACode: " + paCode);

		boolean isConnected = true;

		Connection con = dbConfig.getCon();
		if (con == null) {
			isConnected = false;
		}

		try {

			if (isConnected) {
				System.out.println("Connection Object Created...");

				logger.info("Connection Object Created...");

				String query1 = "SELECT device_id FROM terminal WHERE terminal_id='" + tid + "'";

				System.out.println(query1);
				logger.info(query1);

				PreparedStatement preparedStatement1 = con.prepareStatement(query1);

				ResultSet rs1 = preparedStatement1.executeQuery();

				try {
					if (rs1.next()) {

						deviceId = rs1.getString("device_id").trim();

						System.out.println("device_id:  " + deviceId);

					}

				} catch (Exception e) {

					System.out.println(e);
				}

				String query12 = "SELECT machine_id FROM devices WHERE device_id='" + deviceId + "'";

				System.out.println(query12);
				logger.info(query12);

				PreparedStatement preparedStatement12 = con.prepareStatement(query12);

				ResultSet rs12 = preparedStatement12.executeQuery();

				try {
					if (rs12.next()) {

						machineId = rs12.getString("machine_id").trim();

						System.out.println("machine_id:  " + machineId);

					}

				} catch (Exception e) {

					System.out.println(e);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);

		}

		try {

			String url = "https://lynx.nayax.com/Operational/v1/machines/" + machineId + "/machineProducts";
			// String url =
			// "https://lynx.nayax.com/Operational/v1/machines/424429130/machineProducts";
			System.out.println("url: " + url);

			URL obj = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/json");

			TokenGenerate tokenGenerate = new TokenGenerate();
			String apiKey = tokenGenerate.generateToken().trim();

			System.out.println("apiKey: " + apiKey);

			conn.setRequestProperty("Authorization", "Bearer " + apiKey);

			conn.setRequestProperty("charset", "utf-8");
			conn.setUseCaches(false);

			int responseCode = conn.getResponseCode();
			String responseBody = conn.getResponseMessage();

			System.out.println("Response Code : " + responseCode);
			System.out.println("Response Body : " + responseBody);

			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			while ((response = rd.readLine()) != null) {

//				System.out.println("RESPONSE..." + response.toString());

				jsonRes = response.toString();

				JSONArray values = new JSONArray(jsonRes);

				for (int i = 0; i < values.length(); i++) {

					// System.out.println("i: " + i);

					JSONObject data = values.getJSONObject(i);

					// System.out.println("data: " + data);

					if (!data.isNull("PACode")) {

						// System.out.println("here pa code in not null...");

						String paCodeData = data.getString("PACode").trim();
						// System.out.println("here pa code is : " + paCode);

						if (paCode.equals(paCodeData)) {

							// System.out.println("now pa code is equal with: " + paCode + ":" +
							// paCodeData);

							// System.out.println("1: " + paCode);
							// System.out.println("2: " + paCodeData);

							// if (!data.isNull("MachinePrice")) {

							if (!data.isNull("DexPrice")) {

								// System.out.println("here machine price is not null...");

								// machinePrice = data.getDouble("MachinePrice");

								machinePrice = data.getDouble("DexPrice");

								// System.out.println("machinePrice: " + machinePrice);

								JSONObject jsonObject = new JSONObject();

								JSONObject jsonObject2 = new JSONObject();

								jsonObject2.put("machinePrice", machinePrice);

								jsonObject.put("status", "success");
								jsonObject.put("message", "");
								jsonObject.put("data", jsonObject2);

								machinePrice = 0;
								return jsonObject.toString();

							} else {

								// System.out.println("machine price is null here...");
								machinePrice = 0;
								// System.out.println("machinePrice: " + machinePrice);

								JSONObject jsonObject = new JSONObject();

								JSONObject jsonObject2 = new JSONObject();

								jsonObject2.put("machinePrice", machinePrice);

								jsonObject.put("status", "failure");
								jsonObject.put("message", "Please select alternate product.");
								jsonObject.put("data", jsonObject2);

								return jsonObject.toString();

							}
						}

					} else {

						// System.out.println("pa code is null here...");

						// System.out.println(paCode);

						JSONObject jsonObject = new JSONObject();
						JSONObject jsonObject2 = new JSONObject();
						jsonObject2.put("machinePrice", 0);
						jsonObject.put("status", "failure");
						jsonObject.put("message", "Product code invalid.");
						jsonObject.put("data", jsonObject2);
						return jsonObject.toString();

					}

				}

			}

			rd.close();

		} catch (Exception e) {
			e.printStackTrace();

		}

		JSONObject jsonObject = new JSONObject();
		JSONObject jsonObject2 = new JSONObject();
		jsonObject2.put("machinePrice", machinePrice);
		jsonObject.put("status", "failure");
		jsonObject.put("message", "Product code invalid.");
		jsonObject.put("data", jsonObject2);
		return jsonObject.toString();

	}

}
