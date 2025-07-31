package com.turno.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.turno.constants.LoanType;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoanRequest {

	@NotBlank(message = "Customer name is required")
	@JsonProperty("customer_name")
	private String customerName;

	@NotBlank(message = "Customer phone is required")
	@JsonProperty("customer_phone")
	private String customerPhone;

	@NotNull(message = "Loan amount is required")
	@DecimalMin(value = "1000.0", message = "Loan amount must be at least 1000")
	@JsonProperty("loan_amount")
	private Double loanAmount;

	@NotNull(message = "Loan type is required")
	@JsonProperty("loan_type")
	private LoanType loanType;
}
