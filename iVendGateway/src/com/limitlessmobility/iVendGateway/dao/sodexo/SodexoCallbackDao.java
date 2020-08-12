package com.limitlessmobility.iVendGateway.dao.sodexo;

import com.limitlessmobility.iVendGateway.model.sodexo.SodexoCallbackTransactions;

/**
 * DAO Class
 **/

public interface SodexoCallbackDao {

	public abstract boolean eventLogSave(SodexoCallbackTransactions sodexoCallbackTransactions);

}
