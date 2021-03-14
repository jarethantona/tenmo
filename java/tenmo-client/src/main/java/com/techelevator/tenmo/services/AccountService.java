package com.techelevator.tenmo.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.User;

public class AccountService {
	
    private String baseUrl;
    private RestTemplate restTemplate = new RestTemplate();
    
    public AccountService(String baseUrl) {
    	this.baseUrl = baseUrl;
    }
    
    public String getUsername(AuthenticatedUser user, Long userIdToSearchWith) {
    	HttpEntity entity = createRequestEntity(user);
    	return retrieveUsername(entity, userIdToSearchWith);
    }
    
    public Double getAccountBalance(AuthenticatedUser user) {
    	HttpEntity entity = createRequestEntity(user);
    	Long userId = user.getUser().getId();
    	return sendBalanceRequest(entity, userId);
    }
    
    public User[] getAllUsers(AuthenticatedUser user) {
    	HttpEntity entity = createRequestEntity(user);
    	Long userId = user.getUser().getId();
    	return sendRequestForAllUsers(entity, userId);
    }
    
    /*
     *  Private Methods
     */
    
    private String retrieveUsername(HttpEntity entity, Long userId) {
    	String result = null;
    	try {
    		ResponseEntity<String> response = restTemplate.exchange(baseUrl + "/accounts/" + userId, 
    				HttpMethod.GET, entity, String.class);
    		result = response.getBody();
		} catch (RestClientResponseException ex) {
			System.out.println(ex.getMessage());
		}
    	return result;
    }
	
	private Double sendBalanceRequest(HttpEntity entity, Long userId) {
		Double result = null;
		try {
			ResponseEntity<Double> response = restTemplate.exchange(baseUrl + "/accounts?userId=" + userId, 
					HttpMethod.GET, entity, Double.class);
			result = response.getBody();
		} catch (RestClientResponseException ex) {
			System.out.println(ex.getMessage());
		}
		return result;
	}
	
	private User[] sendRequestForAllUsers(HttpEntity entity, Long userId) {
		User[] result = null;
		try {
			ResponseEntity<User[]> response = restTemplate.exchange(baseUrl + "/accounts/users?userId=" + userId, 
					HttpMethod.GET, entity, User[].class);
			result = response.getBody();
		} catch (RestClientResponseException ex) {
			System.out.println(ex.getMessage());
		}
		return result;
	}
	
	private HttpEntity createRequestEntity(AuthenticatedUser user) {
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_JSON);
    	headers.setBearerAuth(user.getToken());
    	HttpEntity entity = new HttpEntity<>(headers);
    	return entity;
    }
	
}
