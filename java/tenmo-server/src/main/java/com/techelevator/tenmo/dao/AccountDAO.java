package com.techelevator.tenmo.dao;

import java.security.Principal;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;

public interface AccountDAO {
	
	String getUsername(Long id);
	
	Double getAccountBalance(Long id);
	
	User[] getAllUsers();

}
