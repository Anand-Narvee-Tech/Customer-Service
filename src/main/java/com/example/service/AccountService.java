package com.example.service;

import java.util.List;

import com.example.entity.Accounts;

public interface AccountService {

	Accounts createInvoice(Accounts invoice);

	Accounts submitInvoice(Long invoiceId);

	Accounts sendInvoice(Long invoiceId, String sentBy);

	List<Accounts> getAllInvoices();

}
