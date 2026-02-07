package com.example.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import com.example.DTO.InvoiceStatus;
import com.example.entity.Accounts;
import com.example.repository.AccountsRepo;
import com.example.service.AccountService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

	private final AccountsRepo repo;

	@Override
	public Accounts createInvoice(Accounts invoice) {
		invoice.setStatus(InvoiceStatus.DRAFT);
		invoice.setOutstandingAmount(invoice.getTotalAmount());
		return repo.save(invoice);
	}

	@Override
	public Accounts submitInvoice(Long invoiceId) {

		Accounts invoice = repo.findById(invoiceId).orElseThrow(() -> new RuntimeException("Invoice not found"));

		if (invoice.getStatus() != InvoiceStatus.DRAFT) {
			throw new RuntimeException("Only DRAFT invoices can be submitted");
		}

		invoice.setStatus(InvoiceStatus.SUBMITTED);
		invoice.setSubmittedAt(LocalDateTime.now());

		return repo.save(invoice);
	}

	@Override
	public Accounts sendInvoice(Long invoiceId, String sentBy) {

		Accounts invoice = repo.findById(invoiceId).orElseThrow(() -> new RuntimeException("Invoice not found"));

		if (invoice.getStatus() != InvoiceStatus.SUBMITTED) {
			throw new RuntimeException("Only SUBMITTED invoices can be sent");
		}

		invoice.setStatus(InvoiceStatus.SENT);
		invoice.setSentAt(LocalDateTime.now());
		invoice.setSentBy(sentBy);

		return repo.save(invoice);
	}

	@Override
	public List<Accounts> getAllInvoices() {
		return repo.findAll();
	}
}
