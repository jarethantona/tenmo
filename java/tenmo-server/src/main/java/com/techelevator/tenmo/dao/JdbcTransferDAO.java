package com.techelevator.tenmo.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

@Component
public class JdbcTransferDAO implements TransferDAO {
	
	private JdbcTemplate jdbcTemplate;
	
	public JdbcTransferDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void createTransferRecord(Integer senderAcctNum, Integer recipientAcctNum, 
			Double transferAmount, Integer transferType, Integer transferStatus) {
		
		String sql = "INSERT INTO transfers (transfer_type_id, transfer_status_id, "
		+ "account_from, account_to, amount) VALUES (?, ?, ?, ?, ?)";
		
		jdbcTemplate.update(sql, transferType, transferStatus, senderAcctNum, recipientAcctNum, transferAmount);

	}

	@Override
	public Transfer[] viewAllTransfers(Long userId) {
		
		// Create list of all transfers to which the user is a party
		List<Transfer> list = new ArrayList<>();
		
		String sql = "SELECT account_id FROM accounts WHERE user_id = ?";
		Integer userAcctNum = jdbcTemplate.queryForObject(sql, Integer.class, userId);
		
		// SQL query checks both 'account_from' and 'account_to' in transfer table
		String sqlGetUsersTransfers = "SELECT * FROM transfers WHERE account_from = ? OR account_to = ?";
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlGetUsersTransfers, userAcctNum, userAcctNum);
		
		while (rowSet.next()) { // Add each transfer to the list
			
			Transfer transfer = new Transfer();
			
			// Create readable variables
			Integer transferId = rowSet.getInt("transfer_id");
			Integer transferTypeId = rowSet.getInt("transfer_type_id");
			Integer transferStatusId = rowSet.getInt("transfer_status_id");
			Double amount = rowSet.getDouble("amount");
			
			transfer.setTransferId(transferId);
			transfer.setTransferType(transferTypeId);
			transfer.setTransferStatus(transferStatusId);
			transfer.setAmount(amount);
			
			Integer acctFromNum = rowSet.getInt("account_from");
			Integer acctToNum = rowSet.getInt("account_to");
			
			// Create Account objects to add to the transfer
			// (Transfer class's instance variables include an Account object,
			// not just the account number)
			
			String sqlGetAcctFrom = "SELECT * FROM accounts WHERE account_id = ?";
			SqlRowSet rowSetFrom = jdbcTemplate.queryForRowSet(sqlGetAcctFrom, acctFromNum);
			if (rowSetFrom.next()) {
				Integer accountId = rowSetFrom.getInt("account_id");
				Integer acctUserId = rowSetFrom.getInt("user_id");
				Double balance = rowSetFrom.getDouble("balance");
				Account accountFrom = new Account(accountId, acctUserId, balance);
				transfer.setAccountFrom(accountFrom);
			}
				
			String sqlGetAcctTo = "SELECT * FROM accounts WHERE account_id = ?";
			SqlRowSet rowSetTo = jdbcTemplate.queryForRowSet(sqlGetAcctTo, acctToNum);
			if (rowSetTo.next()) {
				Integer accountId = rowSetTo.getInt("account_id");
				Integer acctUserId = rowSetTo.getInt("user_id");
				Double balance = rowSetTo.getDouble("balance");
				Account accountTo = new Account(accountId, acctUserId, balance);
				transfer.setAccountTo(accountTo);
			}
			
			list.add(transfer);
		}
		
		// Create an array and copy over the list, then return the array
		int arraySize = list.size();
		Transfer[] allTransfers = new Transfer[arraySize];
		
		for (int i = 0; i < arraySize; i++) {
			allTransfers[i] = list.get(i);
		}
		
		return allTransfers;
	}
	
	@Override
	public Integer updateAcctBalanceReturnsAcctNum(Double newBalance, Long userId) {
		
		String sqlUpdateBalance = "UPDATE accounts SET balance = ? WHERE user_id = ?";
		jdbcTemplate.update(sqlUpdateBalance, newBalance, userId);
		
		String sqlGetAcctNum = "SELECT account_id FROM accounts WHERE user_id = ?";
		Integer acctNum = jdbcTemplate.queryForObject(sqlGetAcctNum, Integer.class, userId);
		
		return acctNum;
	}
	
	@Override
	public String getTypeName(Integer type) {
		String sql = "SELECT transfer_type_desc FROM transfer_types WHERE transfer_type_id = ?";
		String typeName = jdbcTemplate.queryForObject(sql, String.class, type);
		return typeName;
	}
	
	@Override
	public String getStatusName(Integer status) {
		String sql = "SELECT transfer_status_desc FROM transfer_statuses WHERE transfer_status_id = ?";
		String statusName = jdbcTemplate.queryForObject(sql, String.class, status);
		return statusName;
	}

}
