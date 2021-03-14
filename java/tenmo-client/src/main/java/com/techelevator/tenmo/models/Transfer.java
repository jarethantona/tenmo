package com.techelevator.tenmo.models;

public class Transfer {
	
	private Integer transferId;
	private Integer transferType;
	private Integer transferStatus;
	private Account accountFrom;
	private Account accountTo;
	private Double amount;
	
	public Transfer() {
		
	}
	
	public Transfer (Integer transferId, Integer transferType, Integer transferStatus, 
			Account accountFrom, Account accountTo, Double amount) {
		this.setTransferId(transferId);
		this.setTransferType(transferType);
		this.setTransferStatus(transferStatus);
		this.setAccountFrom(accountFrom);
		this.setAccountTo(accountTo);
		this.setAmount(amount);
	}
	
	public Integer getTransferId() {
		return transferId;
	}
	public void setTransferId(Integer transferId) {
		this.transferId = transferId;
	}
	public Integer getTransferType() {
		return transferType;
	}
	public void setTransferType(Integer transferType) {
		this.transferType = transferType;
	}
	public Integer getTransferStatus() {
		return transferStatus;
	}
	public void setTransferStatus(Integer transferStatus) {
		this.transferStatus = transferStatus;
	}
	public Account getAccountFrom() {
		return accountFrom;
	}
	public void setAccountFrom(Account accountFrom) {
		this.accountFrom = accountFrom;
	}
	public Account getAccountTo() {
		return accountTo;
	}
	public void setAccountTo(Account accountTo) {
		this.accountTo = accountTo;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}

}
