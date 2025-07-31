package com.turno.services;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.turno.dtos.AgentDecisionRequest;
import com.turno.dtos.LoanRequest;
import com.turno.dtos.LoanResponse;
import com.turno.dtos.LoansPage;
import com.turno.dtos.TopCustomerApproved;

public interface LoanService {

	public LoanResponse submitLoan(LoanRequest request);

	public Boolean reviewLoan(Integer loanId, Integer agentId, AgentDecisionRequest request) throws Exception;

	public Map<String, Integer> getLoanCountByStatuses();

	public List<TopCustomerApproved> getTopCustomers();

	Page<LoansPage> getLoansByStatus(String status, Integer page, Integer size);
}
