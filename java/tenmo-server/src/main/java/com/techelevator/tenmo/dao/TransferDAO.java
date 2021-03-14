package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

public interface TransferDAO {
	
	void createTransferRecord(Integer senderAcctNum, Integer recipientAcctNum, 
			Double transferAmount, Integer transferType, Integer transferStatus);
	
	Integer updateAcctBalanceReturnsAcctNum(Double newBalance, Long userId);
	
	Transfer[] viewAllTransfers(Long userId);
	
	String getTypeName(Integer type);
	
	String getStatusName(Integer status);

}
