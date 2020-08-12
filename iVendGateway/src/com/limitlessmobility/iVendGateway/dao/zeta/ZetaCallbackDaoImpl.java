package com.limitlessmobility.iVendGateway.dao.zeta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.limitlessmobility.iVendGateway.db.DbConfigration;
import com.limitlessmobility.iVendGateway.db.DbConfigrationImpl;
import com.limitlessmobility.iVendGateway.model.zeta.ZetaCallbackTransactions;

/**
 * Implementation Class for Zeta Callback Service.
 **/

public class ZetaCallbackDaoImpl implements ZetaCallbackDao {

	static final Logger logger = Logger.getLogger(ZetaCallbackDaoImpl.class);

	DbConfigration dbConfig = new DbConfigrationImpl();

	@Override
	public boolean eventLogSave(ZetaCallbackTransactions zetaCallbackTransactions) {

		System.out.println("DaoImpl CallBack Method is calling!!");

		boolean isConnected = true;

		Connection con = dbConfig.getCon();
		if (con == null) {
			isConnected = false;
		}

		try {

			if (isConnected) {
				logger.info("Connection Object Created...");

				JSONObject jsonObject = new JSONObject(zetaCallbackTransactions);
				String str = jsonObject.toString();
				logger.info(str);

				Date date = new Date();
				java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String currentTime = sdf.format(date);

				String query = "INSERT INTO event_logs(event_details,event_type,terminal_id,date,event_date,timestamp,version,orderid) VALUES(?,?,?,?,?,?,?,?)";

				System.out.println("3");

				PreparedStatement preparedStatement = con.prepareStatement(query);
				preparedStatement.setString(1, str);
				preparedStatement.setString(2, "Zeta Callback API Response");
				preparedStatement.setString(3, zetaCallbackTransactions.getPayload().getMerchantInfo().getTid());
				preparedStatement.setString(4, currentTime);
				preparedStatement.setString(5, currentTime);
				preparedStatement.setString(6, null);
				preparedStatement.setString(7, ZetaCallbackTransactions.VERSION);
				preparedStatement.setString(8, zetaCallbackTransactions.getPayload().getPurchaseTransactionId());

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
