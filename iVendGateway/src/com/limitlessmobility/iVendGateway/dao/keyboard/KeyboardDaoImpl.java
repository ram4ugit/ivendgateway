package com.limitlessmobility.iVendGateway.dao.keyboard;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.limitlessmobility.iVendGateway.db.DbConfigration;
import com.limitlessmobility.iVendGateway.db.DbConfigrationImpl;

public class KeyboardDaoImpl implements KeyboardDao{

	@Override
	public String getKeyboardType(String terminalId) {
		DbConfigration dbConfig = new DbConfigrationImpl();
		boolean isConnected = true;
		String keyboardType = null;

		Connection con = dbConfig.getCon();
		System.out.println("con..."+con);
		if (con == null) {
			isConnected = false;
		}

		try {

			if (isConnected) {
				
				Statement stmt = con.createStatement();

				String query = "SELECT keyboard_type from keyboard where terminal_id='"+terminalId+"'";
				System.out.println("query.." + query);

				//Statement stmt = con.createStatement();

				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {

					keyboardType = rs.getString(1);

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}

		return keyboardType;
	}

}
