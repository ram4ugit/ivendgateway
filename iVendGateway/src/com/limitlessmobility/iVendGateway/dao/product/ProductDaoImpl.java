package com.limitlessmobility.iVendGateway.dao.product;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.limitlessmobility.iVendGateway.db.DbConfigration;
import com.limitlessmobility.iVendGateway.db.DbConfigrationImpl;
import com.limitlessmobility.iVendGateway.model.product.ProductPriceDetail;

public class ProductDaoImpl implements ProductDao{

	@Override
	public String getProductPriceByCard(String cardNo, String productId, String amount) {

		DbConfigration dbConfig = new DbConfigrationImpl();
		boolean isConnected = true;
		Connection con = dbConfig.getCon();

		String returnStatus="False";

		if (con == null) {
			isConnected = false;
		}

//		ProductPriceDetail productPriceDetail = new ProductPriceDetail();
		try {

			if (isConnected) {

				String query = "SELECT * FROM prepaid_card_payment where card_no='"+cardNo+"' and amount >'"+amount+"' and status='1'";

				Statement stmt = con.createStatement();

				ResultSet rs = stmt.executeQuery(query);

				if (rs.next()) {

					
					String status = rs.getString("status");
					double dbAmount = rs.getDouble("amount");
					double l = Double.valueOf(amount); 
					System.out.println("dbAmount--"+dbAmount+" ..l.."+l);
					double updatedAmount = dbAmount-l;
					
//					productPriceDetail.setAmount(String.valueOf(updatedAmount));
					String cardNumber = rs.getString("card_no");
					
					if(status.equalsIgnoreCase("1")){
//						productPriceDetail.setStatus("active");
						returnStatus="true";
						stmt =  con.createStatement();
					      String sql = "UPDATE prepaid_card_payment SET amount = '"+updatedAmount+"' WHERE card_no='"+cardNumber+"'";
					      stmt.executeUpdate(sql);
					} else {
//						productPriceDetail.setStatus("inactive");
						returnStatus="flase";
					}
					
				} else {
					/*productPriceDetail.setAmount("0");
					productPriceDetail.setStatus("inactive");*/
					returnStatus="false";
				}
			}

		} catch (Exception e) {
//			e.printStackTrace();

		} finally {
			dbConfig.closeConnection(con);
		}
		return returnStatus;

	}

	@Override
	public ProductPriceDetail getProductPriceByMachine(String machineId,
			String productId) {

		DbConfigration dbConfig = new DbConfigrationImpl();
		boolean isConnected = true;
		Connection con = dbConfig.getCon();

		if (con == null) {
			isConnected = false;
		}

		ProductPriceDetail productPriceDetail = new ProductPriceDetail();
		try {

			if (isConnected) {

				String query = "SELECT * FROM prepaid_card_payment where machine_id='"+machineId+"' and product_id='"+productId+"' and status='1'";

				Statement stmt = con.createStatement();

				ResultSet rs = stmt.executeQuery(query);

				if (rs.next()) {

					productPriceDetail.setAmount(rs.getString("amount"));
					String status = rs.getString("status");
					if(status.equalsIgnoreCase("1")){
						productPriceDetail.setStatus("active");
					} else {
						productPriceDetail.setStatus("inactive");
					}
					
				} else {
					productPriceDetail.setAmount("0");
					productPriceDetail.setStatus("inactive");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			dbConfig.closeConnection(con);
		}
		return productPriceDetail;

	}

}
