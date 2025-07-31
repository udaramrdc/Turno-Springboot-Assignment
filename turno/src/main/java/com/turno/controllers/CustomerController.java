package com.turno.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turno.dtos.TopCustomerApproved;
import com.turno.services.LoanService;

@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {
	
	@Autowired
	private LoanService loanService;
	
	@GetMapping("/top")
	public List<TopCustomerApproved> getTopCustomers() {
	    return loanService.getTopCustomers();
	}
}
