package com.turno.services;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.turno.constants.LoanStatus;
import com.turno.constants.LoanType;
import com.turno.dtos.AgentDecisionRequest;
import com.turno.dtos.LoanRequest;
import com.turno.dtos.LoanResponse;
import com.turno.dtos.LoansPage;
import com.turno.dtos.TopCustomerApproved;
import com.turno.entities.CustomerEntity;
import com.turno.entities.LoanEntity;
import com.turno.repositories.CustomerRepository;
import com.turno.repositories.LoanRepository;

//@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class LoanServiceImpTest {

	@InjectMocks
	private LoanServiceImp loanService;

	@Mock
	private CustomerRepository customerRepository;
	@Mock
	private LoanRepository loanRepository;

	@Mock
	private NotificationService notificationService;

	@Test
	void testSubmitLoan_NewCustomer() {
		LoanRequest request = new LoanRequest();
		request.setCustomerName("Ravi");
		request.setCustomerPhone("9876543210");
		request.setLoanAmount(10000.0);
		request.setLoanType(LoanType.PERSONAL);

		when(customerRepository.findByPhone(request.getCustomerPhone())).thenReturn(Optional.empty());

		CustomerEntity savedCustomer = new CustomerEntity(1, "Ravi", "9876543210");
		when(customerRepository.save(any())).thenReturn(savedCustomer);

		LoanEntity loanEntity = new LoanEntity();
		loanEntity.setId(101);
		loanEntity.setLoanAmount(10000.0);
		loanEntity.setLoanType("PERSONAL");
		loanEntity.setApplicationStatus("APPLIED");
		loanEntity.setCreatedAt(LocalDateTime.now());
		loanEntity.setCustomerId(savedCustomer.getId());

		when(loanRepository.save(any())).thenReturn(loanEntity);

		LoanResponse response = loanService.submitLoan(request);

		assertEquals(101, response.getLoanId());
		assertEquals("Ravi", response.getCustomerName());
		assertEquals("9876543210", response.getCustomerPhone());
		assertEquals("PERSONAL", response.getLoanType());
		assertEquals("APPLIED", response.getApplicationStatus());
	}
	
	@Test
	void testSubmitLoan_ExistingCustomer() {
		LoanRequest request = new LoanRequest();
		request.setCustomerName("Ravi");
		request.setCustomerPhone("9876543210");
		request.setLoanAmount(10000.0);
		request.setLoanType(LoanType.PERSONAL);
		CustomerEntity savedCustomer = new CustomerEntity(1, "Ravi", "9876543210");
		when(customerRepository.findByPhone(request.getCustomerPhone())).thenReturn(Optional.of(savedCustomer));

		LoanEntity loanEntity = new LoanEntity();
		loanEntity.setId(1);
		loanEntity.setLoanAmount(10000.0);
		loanEntity.setLoanType("PERSONAL");
		loanEntity.setApplicationStatus("APPLIED");
		loanEntity.setCreatedAt(LocalDateTime.now());
		loanEntity.setCustomerId(savedCustomer.getId());

		when(loanRepository.save(any())).thenReturn(loanEntity);

		LoanResponse response = loanService.submitLoan(request);

		assertEquals(1, response.getLoanId());
		assertEquals("Ravi", response.getCustomerName());
		assertEquals("9876543210", response.getCustomerPhone());
		assertEquals("PERSONAL", response.getLoanType());
		assertEquals("APPLIED", response.getApplicationStatus());
	}

	@Test
	void testReviewLoan_Approve() throws Exception {
		LoanEntity loan = new LoanEntity();
		loan.setId(101);
		loan.setAssignedAgentId(2);
		loan.setApplicationStatus("UNDER_REVIEW");
		loan.setCustomerId(5);

		AgentDecisionRequest decisionRequest = new AgentDecisionRequest();
		decisionRequest.setDecision("APPROVE");

		when(loanRepository.findByIdAndAssignedAgentId(101, 2)).thenReturn(Optional.of(loan));

		CustomerEntity customer = new CustomerEntity(5, "Ravi", "9876543210");
		when(customerRepository.findById(5)).thenReturn(Optional.of(customer));

		boolean result = loanService.reviewLoan(101, 2, decisionRequest);

		assertTrue(result);
		verify(notificationService).notifyCustomer(customer, loan);
		verify(loanRepository).save(loan);
		assertEquals(LoanStatus.APPROVED_BY_AGENT.name(), loan.getApplicationStatus()); 
	}
	
	void testReviewLoan_Reject() throws Exception {
		LoanEntity loan = new LoanEntity();
		loan.setId(101);
		loan.setAssignedAgentId(2);
		loan.setApplicationStatus("UNDER_REVIEW");
		loan.setCustomerId(5);

		AgentDecisionRequest decisionRequest = new AgentDecisionRequest();
		decisionRequest.setDecision("REJECT");

		when(loanRepository.findByIdAndAssignedAgentId(101, 2)).thenReturn(Optional.of(loan));

		CustomerEntity customer = new CustomerEntity(5, "Ravi", "9876543210");
		when(customerRepository.findById(5)).thenReturn(Optional.of(customer));

		boolean result = loanService.reviewLoan(101, 2, decisionRequest);

		assertTrue(result);
		verify(notificationService).notifyCustomer(customer, loan);
		verify(loanRepository).save(loan);
		assertEquals(LoanStatus.REJECTED_BY_AGENT.name(), loan.getApplicationStatus()); 
	}

	@Test
	void testGetLoanCountByStatuses() {
		when(loanRepository.countLoansGroupedByStatus())
				.thenReturn(List.of(new Object[] { "APPLIED", 2L }, new Object[] { "APPROVED_BY_AGENT", 1L }));

		Map<String, Integer> result = loanService.getLoanCountByStatuses();

		assertEquals(1, result.get("APPROVED_BY_AGENT"));
		assertEquals(2, result.get("APPLIED"));
		for (LoanStatus status : LoanStatus.values()) {
			assertTrue(result.containsKey(status.name()));
		}
	}

	@Test
	void testGetTopCustomers() {
		TopCustomerApproved t1 = new TopCustomerApproved(1, "Ravi", "9876543210",3L);
		TopCustomerApproved t2 = new TopCustomerApproved(2, "Sneha", "9999990001", 2L);

		when(loanRepository.findTopApprovedCustomers()).thenReturn(List.of(t1, t2));

		List<TopCustomerApproved> result = loanService.getTopCustomers();

		assertEquals(2, result.size());
		assertEquals("Ravi", result.get(0).getCustomerName());
	}

	@Test
	void testGetLoansByStatus() {
		Page<LoansPage> page = new PageImpl<>(List.of(
				new LoansPage(101, "Ravi", "9876543210", 10000.0, "PERSONAL", "APPLIED", null, LocalDateTime.now())));

		when(loanRepository.findByApplicationStatus(eq("APPLIED"), any(Pageable.class))).thenReturn(page);

		Page<LoansPage> result = loanService.getLoansByStatus("APPLIED", 0, 10);

		assertEquals(1, result.getContent().size());
		assertEquals("Ravi", result.getContent().get(0).getCustomerName());
	}
}
