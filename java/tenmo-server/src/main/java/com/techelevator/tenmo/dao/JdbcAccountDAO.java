package com.techelevator.tenmo.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;

@Component
public class JdbcAccountDAO implements AccountDAO {
	
	private JdbcTemplate jdbcTemplate;
	
	public JdbcAccountDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Double getAccountBalance(Long id) {
		Double acctBalance = null;
		
		String sql = "SELECT balance FROM accounts WHERE user_id = ?";
		SqlRowSet result = jdbcTemplate.queryForRowSet(sql, id);
		
		if (result.next()) {
			acctBalance = result.getDouble("balance");
		}
		
		return acctBalance;
	}

	@Override
	public User[] getAllUsers() {
		
		// Create a list of users from the database
		List<User> tempList = new ArrayList<>();
		
		String sql = "SELECT user_id, username FROM users";
		SqlRowSet result = jdbcTemplate.queryForRowSet(sql);
		
		while (result.next()) {
			Long id = result.getLong("user_id");
			String username = result.getString("username");
			
			User user = new User();
			user.setId(id);
			user.setUsername(username);
			
			tempList.add(user);
		}
		
		// Copy the list over to an array and return the array
		int arraySize = tempList.size();
		User[] allUsers = new User[arraySize];
		
		for (int i = 0; i < arraySize; i++) {
			allUsers[i] = tempList.get(i);
		}
		
		return allUsers;
	}

	@Override
	public String getUsername(Long id) {
		String sql = "SELECT username FROM users WHERE user_id = ?";
		String username = jdbcTemplate.queryForObject(sql, String.class, id);
		return username;
	}

}
