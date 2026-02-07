package com.example.DTO;

public enum InvoiceStatus {
	DRAFT, // Invoice created by Accounts
	SUBMITTED, // Submitted for processing
	SENT, // Sent to client via email
	PARTIAL, // Partially paid (Finance)
	PAID, // Fully paid (Finance)
	OVERDUE // Past due date
}