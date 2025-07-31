package com.turno.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "loans")
@AllArgsConstructor
@NoArgsConstructor
public class LoanEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
	@Column(name = "loan_amount")
    private Double loanAmount;

	@Column(name = "loan_type")
    private String loanType;

	@Column(name = "application_status")
    private String applicationStatus;

	@Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "assigned_agent_id")
    private Integer assignedAgentId;
}
