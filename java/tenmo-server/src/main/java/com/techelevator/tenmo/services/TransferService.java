package com.techelevator.tenmo.services;

import org.springframework.stereotype.Service;

import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

@Service
public class TransferService {
	
	public static final Integer TRANSFER_TYPE_REQUEST = 1;
	public static final Integer TRANSFER_TYPE_SEND = 2;
	public static final Integer TRANSFER_STATUS_PENDING = 1;
	public static final Integer TRANSFER_STATUS_APPROVED = 2;
	public static final Integer TRANSFER_STATUS_REJECTED = 3;
	
	private final TransferDAO transferDao;
	private final AccountService accountService;
	
	public TransferService(TransferDAO transferDao, AccountService accountService) {
		this.transferDao = transferDao;
		this.accountService = accountService;
	}
	
	public void createTransfer(Long userId, Long recipientId, Double transferAmount) {
		
		Double senderCurrentBalance = accountService.getAccountBalance(userId);
		Double senderBalanceAfterTransfer = (senderCurrentBalance - transferAmount);
		
		// Only execute the transfer if the sender has enough money
		if ( senderBalanceAfterTransfer >= 0.0 ) {
			
			// Deduct from sender's balance; get back sender's account number
			Integer senderAcctNum = transferDao.updateAcctBalanceReturnsAcctNum
												(senderBalanceAfterTransfer, userId);
			
			Double recipientCurrentBalance = accountService.getAccountBalance(recipientId);
			Double recipientBalanceAfterTransfer = (recipientCurrentBalance + transferAmount);
			
			// Add to recipient's balance; get back recipient's account number
			Integer recipientAcctNum = transferDao.updateAcctBalanceReturnsAcctNum
												   (recipientBalanceAfterTransfer, recipientId);
			
			// Insert record of transfer into database
			transferDao.createTransferRecord(senderAcctNum, recipientAcctNum, transferAmount, 
											 TRANSFER_TYPE_SEND, TRANSFER_STATUS_APPROVED);
		}
	}
	
	public Transfer[] getAllTransfers(Long userId) {
		Transfer[] transfers = transferDao.viewAllTransfers(userId);
		return transfers;
	}
	
	public String getTypeName(Integer type) {
		String typeName = null;
		if (type.equals(TRANSFER_TYPE_REQUEST)) {
			typeName = transferDao.getTypeName(TRANSFER_TYPE_REQUEST);
		} else if (type.equals(TRANSFER_TYPE_SEND)){
			typeName = transferDao.getTypeName(TRANSFER_TYPE_SEND);
		}
		return typeName;
	}
	
	public String getStatusName(Integer status) {
		String statusName = null;
		if (status.equals(TRANSFER_STATUS_PENDING)) {
			statusName = transferDao.getStatusName(TRANSFER_STATUS_PENDING);
		} else if (status.equals(TRANSFER_STATUS_APPROVED)) {
			statusName = transferDao.getStatusName(TRANSFER_STATUS_APPROVED);
		} else if (status.equals(TRANSFER_STATUS_REJECTED)) {
			statusName = transferDao.getStatusName(TRANSFER_STATUS_REJECTED);
		}
		return statusName;
	}
	
}
