package com.turno.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.turno.dtos.LoanRequest;
import com.turno.dtos.LoanResponse;
import com.turno.dtos.LoansPage;
import com.turno.services.LoanService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/loans")
public class LoanController {
	
	@Autowired
	private LoanService loanService;

	@PostMapping
	public LoanResponse submitLoan(@Valid @RequestBody LoanRequest request) {
	    return loanService.submitLoan(request);
	}
	
	@GetMapping
	public Map<String, Object> getLoansByStatus(
	        @RequestParam String status,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size) {

		Page<LoansPage> loanPage = loanService.getLoansByStatus(status, page, size);

	    Map<String, Object> response = new HashMap<>();
	    response.put("loans", loanPage.getContent());
	    response.put("current_page", loanPage.getNumber());
	    response.put("total_items", loanPage.getTotalElements());
	    response.put("total_pages", loanPage.getTotalPages());
	    return response;
	}
	
	@GetMapping("/status-count")
    public Map<String, Integer> getLoanCountByStatuses() {
        Map<String, Integer> statusCounts = loanService.getLoanCountByStatuses();
        return statusCounts;
	}
}
