package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.common.RestAPIResponse;
import com.example.entity.ConsultantBankAccount;
import com.example.service.ConsultantBankAccountService;

@RestController
@RequestMapping("/bank-accounts")
public class ConsultantBankAccountController {

    @Autowired
    private ConsultantBankAccountService service;

    // ✅ CREATE
    @PostMapping("/saveaccounts")
    public ResponseEntity<RestAPIResponse> create(@RequestBody ConsultantBankAccount bank) {

        ConsultantBankAccount saved = service.create(bank);

        return ResponseEntity.ok(
                new RestAPIResponse("success", "Bank account created successfully", saved)
        );
    }

    // ✅ GET ALL BY CONSULTANT
    @GetMapping("/getAll")
    public ResponseEntity<RestAPIResponse> getByConsultant() {

        List<ConsultantBankAccount> list = service.getByAllConsultantBankAccounts();

        return ResponseEntity.ok(
                new RestAPIResponse("success", "Fetched successfully", list)
        );
    }

    // ✅ UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<RestAPIResponse> update(@PathVariable Long id,
                                                 @RequestBody ConsultantBankAccount bank) {

        ConsultantBankAccount updated = service.update(id, bank);

        return ResponseEntity.ok(
                new RestAPIResponse("success", "Updated successfully", updated)
        );
    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<RestAPIResponse> delete(@PathVariable Long id) {

        service.delete(id);

        return ResponseEntity.ok(
                new RestAPIResponse("success", "Bank account deleted successfully")
        );
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<RestAPIResponse> getByConsultantBankAccount(@PathVariable Long id) {

        service.getByConsultantBankAccount(id);

        return ResponseEntity.ok(
                new RestAPIResponse("success", "Bank account Fetched successfully")
        );
    }
}