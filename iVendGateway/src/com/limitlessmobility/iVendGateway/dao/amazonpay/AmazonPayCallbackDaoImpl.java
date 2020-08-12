package com.limitlessmobility.iVendGateway.dao.amazonpay;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.limitlessmobility.iVendGateway.db.DbConfigration;
import com.limitlessmobility.iVendGateway.db.DbConfigrationImpl;
import com.limitlessmobility.iVendGateway.model.amazonpay.AmazonPayCallbackModel;

/**
 * Implementation Class for Zeta Callback Service.
 **/

public class AmazonPayCallbackDaoImpl implements AmazonPayCallbackDao {

	static final Logger logger = Logger.getLogger(AmazonPayCallbackDaoImpl.class);

	DbConfigration dbConfig = new DbConfigrationImpl();

	@Override
	public boolean eventLogSave(AmazonPayCallbackModel amazonPayCallbackModel) {

		System.out.println("i am in AmazonPayCallbackDaoImpl");

		boolean isConnected = true;

		Connection con = dbConfig.getCon();
		if (con == null) {
			isConnected = false;
		}

		try {

			if (isConnected) {
				logger.info("Connection Object Created...");

				JSONObject jsonObject = new JSONObject(amazonPayCallbackModel);
				String str = jsonObject.toString();
				logger.info(str);

				Date date = new Date();
				java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String currentTime = sdf.format(date);

				String query = "INSERT INTO event_logs(event_details,event_date) VALUES(?,?)";

				PreparedStatement preparedStatement = con.prepareStatement(query);
				preparedStatement.setString(1, str);
				preparedStatement.setString(2, currentTime);

				int isInsert = preparedStatement.executeUpdate();

				System.out.println("Record is inserted into event_logs table!");
				if (isInsert > 0) {
					return true;
				}

				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);

		} finally {
			dbConfig.closeConnection(con);
			logger.info("db connection closed");
		}
		return false;
	}
}
