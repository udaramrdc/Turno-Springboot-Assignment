package com.turno.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TopCustomerApproved {

	@JsonProperty("customer_id")
	Integer customerId;

	@JsonProperty("customer_name")
	String customerName;

	@JsonProperty("customer_phone")
	String customerPhone;

	@JsonProperty("approved_loan_count")
	Long approvedLoanCount;
}
