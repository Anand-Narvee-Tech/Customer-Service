package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Accounts;
import com.example.service.AccountService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountsController {

	@Autowired
	private final AccountService service;

	@PostMapping("/create")
	public Accounts create(@RequestBody Accounts invoice) {
		return service.createInvoice(invoice);
	}

	@PutMapping("/{id}/submit")
	public Accounts submit(@PathVariable("id") Long id) {
		return service.submitInvoice(id);
	}

	@PutMapping("/{id}/send")
	public Accounts send(@PathVariable("id") Long id, @RequestParam String sentBy) {
		return service.sendInvoice(id, sentBy);
	}

	@GetMapping
	public List<Accounts> list() {
		return service.getAllInvoices();
	}
}
