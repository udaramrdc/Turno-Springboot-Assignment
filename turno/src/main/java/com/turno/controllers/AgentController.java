package com.turno.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turno.dtos.AgentDecisionRequest;
import com.turno.services.LoanService;

@RestController
@RequestMapping("api/v1/agents")
public class AgentController {

	@Autowired
	private LoanService loanService;
	
	@PutMapping("{agent_id}/loans/{loan_id}/decision")
	public Boolean reviewLoan(@PathVariable("agent_id") Integer agentId, @PathVariable("loan_id") Integer loanId,
			@RequestBody AgentDecisionRequest request) {
		try {
			return loanService.reviewLoan(loanId, agentId, request);
		} catch (Exception e) {
			return false;
		}
	}

}
