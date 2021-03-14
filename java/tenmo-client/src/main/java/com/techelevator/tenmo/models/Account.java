package com.techelevator.tenmo.models;

public class Account {
	
	private Integer accountId;
	private Integer userId;
	private Double balance;
	
	public Account() {
		
	}
	
	public Account (Integer accountId, Integer userId, Double balance) {
		this.setAccountId(accountId);
		this.setUserId(userId);
		this.setBalance(balance);
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer acctNumber) {
		this.accountId = acctNumber;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

}
