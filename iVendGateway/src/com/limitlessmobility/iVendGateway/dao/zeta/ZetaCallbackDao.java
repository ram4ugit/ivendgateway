package com.limitlessmobility.iVendGateway.dao.zeta;

import com.limitlessmobility.iVendGateway.model.zeta.ZetaCallbackTransactions;

/**
 * DAO Class
 **/

public interface ZetaCallbackDao {

	public abstract boolean eventLogSave(ZetaCallbackTransactions zetaCallbackTransactions);

}
