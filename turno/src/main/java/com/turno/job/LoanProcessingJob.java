package com.turno.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.turno.processors.LoanProcessor;

@Component
public class LoanProcessingJob {
	
	@Autowired
	private LoanProcessor loanProcessor;

	@Scheduled(fixedDelay = 10000) // every 10 seconds
	public void processAppliedLoans() {
		loanProcessor.processAppliedLoans();
	}
	
	@Scheduled(fixedDelay = 10000) // every 10 seconds
	public void assignAgentsToLoan() {
		loanProcessor.assignAgentToLoans();
	}
}
