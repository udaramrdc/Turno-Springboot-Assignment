package com.turno.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.turno.dtos.LoansPage;
import com.turno.dtos.TopCustomerApproved;
import com.turno.entities.LoanEntity;

import jakarta.persistence.LockModeType;

public interface LoanRepository extends JpaRepository<LoanEntity, Integer> {
	Optional<LoanEntity> findByCustomerId(Integer customerId);

	Optional<LoanEntity> findByIdAndAssignedAgentId(Integer loanId, Integer agentId);

	@Query("SELECT l.applicationStatus, COUNT(l) FROM LoanEntity l GROUP BY l.applicationStatus")
	List<Object[]> countLoansGroupedByStatus();

	@Query(value = """
			    SELECT
			        c.id AS customer_id,
			        c.name AS customer_name,
			        c.phone AS customer_phone,
			        COUNT(l.id) AS approved_loan_count
			    FROM customers c
			    JOIN loans l ON l.customer_id = c.id
			    WHERE l.application_status IN ('APPROVED_BY_SYSTEM', 'APPROVED_BY_AGENT')
			    GROUP BY c.id
			    ORDER BY approved_loan_count DESC
			    LIMIT 3
			""", nativeQuery = true)
	List<TopCustomerApproved> findTopApprovedCustomers();

	@Query("""
			    SELECT
			        l.id AS loanId,
			        c.name AS customerName,
			        c.phone AS customerPhone,
			        l.loanAmount AS loanAmount,
			        l.loanType AS loanType,
			        l.applicationStatus AS applicationStatus,
			        l.assignedAgentId AS assignedAgentId,
			        l.createdAt AS createdAt
			    FROM LoanEntity l
			    JOIN CustomerEntity c ON l.customerId = c.id
			    WHERE l.applicationStatus = :status
			""")
	Page<LoansPage> findByApplicationStatus(String status, Pageable pageable);
	
	List<LoanEntity> findTop5ByApplicationStatus(String status);
	
	List<LoanEntity> findByApplicationStatusAndAssignedAgentIdIsNull(String status);
}
