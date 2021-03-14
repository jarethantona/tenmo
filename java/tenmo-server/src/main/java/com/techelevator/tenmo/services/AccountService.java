package com.techelevator.tenmo.services;

import org.springframework.stereotype.Service;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.model.User;

@Service
public class AccountService {
	
	private final AccountDAO accountDao;
	
	public AccountService(AccountDAO accountDao) {
		this.accountDao = accountDao;
	}
	
	public String getUsername(Long id) {
		String username = accountDao.getUsername(id);
		return username;
	}
	
	public Double getAccountBalance(Long id) {
		Double acctBalance = accountDao.getAccountBalance(id);
		return acctBalance;
	}
	
	public User[] getAllUsers(Long id) {
		
		// The DAO will return an array containing *all* users
		User[] allUsers = accountDao.getAllUsers();
		
		// Given the array just created, create an array one element smaller
		// and copy over every user *except* the current user
		Long idToSkip = id;
		
		int newArraySize = allUsers.length - 1;
		User[] allUsersExceptForCurrentUser = new User[newArraySize];
		
		for (int i = 0, j = 0; i < newArraySize + 1; i++, j++) {
			Long currentId = allUsers[i].getId();
			// For every user that is not the current user, copy it to the new array
			if ( ! (currentId.equals(idToSkip)) ) {
				allUsersExceptForCurrentUser[j] = allUsers[i];
			} else {
				j--; // If the user's ID is encountered, it is ignored
					 // and the pointer of the new array is moved back one space
			}
		}
				
		return allUsersExceptForCurrentUser;
	}

}
