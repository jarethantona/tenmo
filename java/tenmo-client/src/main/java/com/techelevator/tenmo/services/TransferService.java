package com.techelevator.tenmo.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;

public class TransferService {
	
    private String baseUrl;
    private RestTemplate restTemplate = new RestTemplate();

    public TransferService(String baseUrl) {
    	this.baseUrl = baseUrl;
    }
    
    public void transferTEBucks(AuthenticatedUser currentUser, Long recipientId, Double amount) {
    	HttpEntity entity = createRequestEntity(currentUser);
    	Long userId = currentUser.getUser().getId();
    	createTransfer(entity, userId, recipientId, amount);
    }
    
    public Transfer[] getAllTransfers(AuthenticatedUser currentUser) {
    	HttpEntity entity = createRequestEntity(currentUser);
    	Long userId = currentUser.getUser().getId();
    	Transfer[] transfers = requestAllTransfers(entity, userId);
    	return transfers;
    }
    
    public String getTransferTypeName(AuthenticatedUser currentUser, Integer transferType) {
    	HttpEntity entity = createRequestEntity(currentUser);
    	String typeName = requestTypeName(entity, transferType);
    	return typeName;
    }
    
    public String getTransferStatusName(AuthenticatedUser currentUser, Integer transferStatus) {
    	HttpEntity entity = createRequestEntity(currentUser);
    	String statusName = requestStatusName(entity, transferStatus);
    	return statusName;
    }
    
    /*
     *  Private Methods
     */
    
	private HttpEntity createRequestEntity(AuthenticatedUser user) {
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_JSON);
    	headers.setBearerAuth(user.getToken());
    	HttpEntity entity = new HttpEntity<>(headers);
    	return entity;
    }
	
	private void createTransfer(HttpEntity entity, Long userId, Long recipientId, Double amount) {
		try {
			restTemplate.exchange(baseUrl + "transfers?userId=" + userId 
					+ "&recipientId=" + recipientId + "&amount=" + amount, 
					HttpMethod.POST, entity, Transfer.class);
		} catch (RestClientResponseException ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	private Transfer[] requestAllTransfers(HttpEntity entity, Long userId) {
		Transfer[] transfers = null;
		try {
			ResponseEntity<Transfer[]> response = restTemplate.exchange
					(baseUrl + "transfers?userId=" + userId, 
					HttpMethod.GET, entity, Transfer[].class);
			transfers = response.getBody();
		} catch (RestClientResponseException ex) {
			System.out.println(ex.getMessage());
		}
		return transfers;
	}
	
	private String requestTypeName(HttpEntity entity, Integer transferType) {
		String typeName = null;
		try {
			ResponseEntity<String> response = restTemplate.exchange
					(baseUrl + "transfers/typetable?type=" + transferType, 
					HttpMethod.GET, entity, String.class);
			typeName = response.getBody();
		} catch (RestClientResponseException ex) {
			System.out.println(ex.getMessage());
		}
		return typeName;
	}
	
	private String requestStatusName(HttpEntity entity, Integer transferStatus) {
		String statusName = null;
		try {
			ResponseEntity<String> response = restTemplate.exchange
					(baseUrl + "transfers/statustable?status=" + transferStatus, 
					HttpMethod.GET, entity, String.class);
			statusName = response.getBody();
		} catch (RestClientResponseException ex) {
			System.out.println(ex.getMessage());
		}
		return statusName;
	}


}
