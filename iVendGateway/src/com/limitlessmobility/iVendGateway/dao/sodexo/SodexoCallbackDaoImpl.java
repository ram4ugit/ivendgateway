package com.limitlessmobility.iVendGateway.dao.sodexo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.limitlessmobility.iVendGateway.db.DbConfigration;
import com.limitlessmobility.iVendGateway.db.DbConfigrationImpl;
import com.limitlessmobility.iVendGateway.model.sodexo.SodexoCallbackTransactions;

/**
 * Implementation Class for Sodexo Callback Service.
 **/

public class SodexoCallbackDaoImpl implements SodexoCallbackDao {

	static final Logger logger = Logger.getLogger(SodexoCallbackDaoImpl.class);

	DbConfigration dbConfig = new DbConfigrationImpl();

	@Override
	public boolean eventLogSave(SodexoCallbackTransactions sodexoCallbackTransactions) {

		System.out.println("DaoImpl CallBack Method is calling!!");

		boolean isConnected = true;

		Connection con = dbConfig.getCon();
		if (con == null) {
			isConnected = false;
		}

		try {

			if (isConnected) {
				logger.info("Connection Object Created...");

				JSONObject jsonObject = new JSONObject(sodexoCallbackTransactions);
				String str = jsonObject.toString();
				logger.info(str);

				Date date = new Date();
				java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String currentTime = sdf.format(date);

				String query = "INSERT INTO event_logs(event_details,event_type,terminal_id,date,event_date,timestamp,version,orderid) VALUES(?,?,?,?,?,?,?,?)";

				System.out.println("3");

				PreparedStatement preparedStatement = con.prepareStatement(query);
				preparedStatement.setString(1, str);
				preparedStatement.setString(2, "Sodexo Callback API Response");
				preparedStatement.setString(3, sodexoCallbackTransactions.getPayload().getMerchantInfo().getTid());
				preparedStatement.setString(4, currentTime);
				preparedStatement.setString(5, currentTime);
				preparedStatement.setString(6, null);
				preparedStatement.setString(7, sodexoCallbackTransactions.VERSION);
				preparedStatement.setString(8, sodexoCallbackTransactions.getPayload().getPurchaseTransactionId());

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
