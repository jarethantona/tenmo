package com.techelevator.tenmo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.services.AccountService;

@RestController
@RequestMapping(path = "/accounts")
@PreAuthorize("isAuthenticated()")
public class AccountController {
	
	private final AccountService accountService;
	
	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}
	
	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public String retrieveUsername(@PathVariable Long id) {
		String username = accountService.getUsername(id);
		return username;
	}
	
	@RequestMapping(path = "", method = RequestMethod.GET)
	public Double getAccountBalance(@RequestParam Long userId) {
		Double acctBalance = accountService.getAccountBalance(userId);
		return acctBalance;
	}
	
	@RequestMapping(path = "/users", method = RequestMethod.GET)
	public User[] getAllUsers(@RequestParam Long userId) {
		User[] users = accountService.getAllUsers(userId);
		return users;
	}

}
