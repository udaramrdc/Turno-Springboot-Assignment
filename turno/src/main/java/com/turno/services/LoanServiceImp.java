package com.turno.services;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.turno.constants.LoanStatus;
import com.turno.dtos.AgentDecisionRequest;
import com.turno.dtos.LoanRequest;
import com.turno.dtos.LoanResponse;
import com.turno.dtos.LoansPage;
import com.turno.dtos.TopCustomerApproved;
import com.turno.entities.CustomerEntity;
import com.turno.entities.LoanEntity;
import com.turno.exceptions.InvalidStatusException;
import com.turno.exceptions.LoanDoesNotExist;
import com.turno.repositories.CustomerRepository;
import com.turno.repositories.LoanRepository;

import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;

@Service
public class LoanServiceImp implements LoanService {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private LoanRepository loanRepository;
	
	@Autowired
	private NotificationService notificationService;

	@Override
	@Transactional
	public LoanResponse submitLoan(LoanRequest request) {
		// find or create customer entry
		Optional<CustomerEntity> customerOpt = customerRepository.findByPhone(request.getCustomerPhone());
		CustomerEntity customerEntity;
		if (customerOpt.isEmpty()) {
			customerEntity = new CustomerEntity();
			customerEntity.setName(request.getCustomerName());
			customerEntity.setPhone(request.getCustomerPhone());
			customerEntity = customerRepository.save(customerEntity);
		} else {
			customerEntity = customerOpt.get();
		}

		// Logic to save loan details
		LoanEntity loanEntity = new LoanEntity();
		loanEntity.setLoanAmount(request.getLoanAmount());
		loanEntity.setApplicationStatus(LoanStatus.APPLIED.name());
		loanEntity.setCreatedAt(LocalDateTime.now());
		loanEntity.setLoanType(request.getLoanType().name());
		loanEntity.setCustomerId(customerEntity.getId());
		loanEntity = loanRepository.save(loanEntity);

		return createLoanResponse(loanEntity, customerEntity);
	}

	// method to map loan details to loan response
	private LoanResponse createLoanResponse(LoanEntity loanEntity, CustomerEntity customerEntity) {
		LoanResponse response = new LoanResponse();
		response.setLoanId(loanEntity.getId().intValue());
		response.setCustomerName(customerEntity.getName());
		response.setCustomerPhone(customerEntity.getPhone());
		response.setLoanAmount(loanEntity.getLoanAmount());
		response.setLoanType(loanEntity.getLoanType());
		response.setApplicationStatus(loanEntity.getApplicationStatus());
		response.setAssignedAgentId(loanEntity.getAssignedAgentId()); // Nullable if not assigned
		response.setCreatedAt(loanEntity.getCreatedAt().toLocalDate());
		return response;
	}

	@Override
	public Boolean reviewLoan(Integer loanId, Integer agentId, AgentDecisionRequest request) throws Exception {
		Optional<LoanEntity> loanOpt = loanRepository.findByIdAndAssignedAgentId(loanId, agentId);
		if (loanOpt.isEmpty()) {
			throw new LoanDoesNotExist();
		}
		LoanEntity loanEntity = loanOpt.get();
		String status;
		switch (request.getDecision()) {
		case "APPROVE":
			status = LoanStatus.APPROVED_BY_AGENT.name();
			break;
		case "REJECT":
			status = LoanStatus.REJECTED_BY_AGENT.name();
			break;
		default:
			status = "";
		}
		if (StringUtils.isBlank(status)) {
			throw new InvalidStatusException();
		}
		loanEntity.setApplicationStatus(request.getDecision());
		loanRepository.save(loanEntity);
		CustomerEntity customer = customerRepository.findById(loanEntity.getCustomerId())
	            .orElseThrow(() -> new RuntimeException("Customer not found"));

	    notificationService.notifyCustomer(customer, loanEntity);
		return true;
	}

	@Override
	public Map<String, Integer> getLoanCountByStatuses() {
		List<Object[]> data = loanRepository.countLoansGroupedByStatus();

		Map<String, Integer> statusMap = new HashMap();
		for (Object[] row : data) {
			String status = row[0].toString();
			Integer count = ((Long) row[1]).intValue();
			statusMap.put(status, count);
		}

		// Add missing statuses with 0 count
		for (LoanStatus status : LoanStatus.values()) {
			statusMap.putIfAbsent(status.name(), 0);
		}

		return statusMap;
	}

	@Override
	public List<TopCustomerApproved> getTopCustomers() {
		return loanRepository.findTopApprovedCustomers();
	}

	@Override
	public Page<LoansPage> getLoansByStatus(String status, Integer page, Integer size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
		return loanRepository.findByApplicationStatus(status.toUpperCase(), pageable);
	}

}