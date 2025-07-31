package com.turno.exceptions;

public class InvalidStatusException extends Exception {
	public InvalidStatusException(String message) {
		super(message);
	}
	
	public InvalidStatusException() {
		super("Invalid status");
	}
}
