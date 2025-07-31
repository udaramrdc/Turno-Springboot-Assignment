package com.turno.processors;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.turno.constants.LoanStatus;
import com.turno.entities.AgentEntity;
import com.turno.entities.LoanEntity;
import com.turno.repositories.AgentRepository;
import com.turno.repositories.LoanRepository;
import com.turno.services.NotificationService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LoanProcessor {

	@Autowired
	private LoanRepository loanRepository;

	@Autowired
	private AgentRepository agentRepository;
	
	@Autowired
	private NotificationService notificationService;

	@Autowired
	private Executor taskExecutor;

	private final Random random = new Random();

	public void processAppliedLoans() {
		// fetch top 5 applications with status Applied
		// this was processing the same loan by multiple threads
		List<LoanEntity> appliedLoans = loanRepository.findTop5ByApplicationStatus(LoanStatus.APPLIED.name());

		for (LoanEntity loan : appliedLoans) {
			loan.setApplicationStatus(LoanStatus.PROCESSING.name());
			loanRepository.save(loan);
			taskExecutor.execute(() -> handleLoanProcessing(loan));
		}
	}

	private void handleLoanProcessing(LoanEntity loan) {
		try {
			log.info("Processing loan: {}", loan.getId());

			// Random delay to each thread
			Thread.sleep(5000 + random.nextInt(20000)); // 5 to 25 second

			// Random outcome
			int outcome = random.nextInt(3); // 0,1,2
			String newStatus;
			if (outcome == 0) {
				newStatus = "APPROVED_BY_SYSTEM";
			} else if (outcome == 1) {
				newStatus = "REJECTED_BY_SYSTEM";
			} else {
				newStatus = "UNDER_REVIEW";
			}

			loan.setApplicationStatus(newStatus);
			loanRepository.save(loan);

			log.info("Loan {} status updated to {}", loan.getId(), newStatus);

		} catch (InterruptedException e) {
			log.error("Loan processing interrupted for loan {}", loan.getId(), e);
			Thread.currentThread().interrupt();
		}
	}

	public void assignAgentToLoans() {
		// fetch unassigned loans with under review
		List<LoanEntity> loans = loanRepository
				.findByApplicationStatusAndAssignedAgentIdIsNull(LoanStatus.UNDER_REVIEW.name());
		List<AgentEntity> availableAgents = agentRepository.findAvailableAgents();
		if (availableAgents.size() == 0) {
			log.info("No agent is available for assignment");
			return;
		}
		if (loans.size() == 0) {
			log.info("No loan is available for assignment");
		}
		for (LoanEntity loan : loans) {
			if (availableAgents.size() == 0)
				return;
			loan.setAssignedAgentId(availableAgents.get(0).getId());
			notificationService.notifyAgentAndManager(availableAgents.get(0), loan);
		}
		loanRepository.saveAll(loans);
	}
}
