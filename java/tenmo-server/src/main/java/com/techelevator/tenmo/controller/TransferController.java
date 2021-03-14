package com.techelevator.tenmo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.services.TransferService;

@RestController
@RequestMapping(path = "/transfers")
@PreAuthorize("isAuthenticated()")
public class TransferController {
	
	private final TransferService transferService;
	
	public TransferController(TransferService transferService)	 {
		this.transferService = transferService;
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(path = "", method = RequestMethod.POST)
	public void createTransfer(@RequestParam Long userId, 
			@RequestParam Long recipientId, @RequestParam Double amount) {
		transferService.createTransfer(userId, recipientId, amount);
	}
	
	@RequestMapping(path = "", method = RequestMethod.GET)
	public Transfer[] getAllTransfers(@RequestParam Long userId) {
		Transfer[] allTransfers = transferService.getAllTransfers(userId);
		return allTransfers;
	}
	
	@RequestMapping(path = "/typetable", method = RequestMethod.GET)
	public String getTransferTypeName(@RequestParam Integer type) {
		String typeName = transferService.getTypeName(type);
		return typeName;
	}
	
	@RequestMapping(path = "/statustable", method = RequestMethod.GET)
	public String getTransferStatusName(@RequestParam Integer status) {
		String statusName = transferService.getStatusName(status);
		return statusName;
	}

}
