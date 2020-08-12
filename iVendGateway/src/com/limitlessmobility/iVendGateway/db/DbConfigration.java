package com.limitlessmobility.iVendGateway.db;

import java.sql.Connection;

public abstract interface DbConfigration {
		
	public abstract boolean isConnected(Connection connection);
	
	public abstract void closeConnection(Connection connection);
	
	public abstract Connection getCon();
}
