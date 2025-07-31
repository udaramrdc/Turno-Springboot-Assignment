package com.turno.exceptions;

public class LoanDoesNotExist extends Exception {
	public LoanDoesNotExist(String message) {
		super(message);
	}
	
	public LoanDoesNotExist() {
		super("Loan does not exist");
	}
	
}
