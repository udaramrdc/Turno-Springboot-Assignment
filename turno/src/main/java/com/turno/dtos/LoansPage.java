package com.turno.dtos;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoansPage {

	    @JsonProperty("loan_id")
	    private Integer loanId;

	    @JsonProperty("customer_name")
	    private String customerName;

	    @JsonProperty("customer_phone")
	    private String customerPhone;

	    @JsonProperty("loan_amount")
	    private Double loanAmount;

	    @JsonProperty("loan_type")
	    private String loanType;

	    @JsonProperty("application_status")
	    private String applicationStatus;

	    @JsonProperty("assigned_agent_id")
	    private Integer assignedAgentId;

	    @JsonProperty("created_at")
	    private LocalDateTime createdAt;
	}
