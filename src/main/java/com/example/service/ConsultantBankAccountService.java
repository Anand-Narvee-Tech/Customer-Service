package com.example.service;

import java.util.List;

import com.example.entity.ConsultantBankAccount;

public interface ConsultantBankAccountService {

    ConsultantBankAccount create(ConsultantBankAccount bank);

    List<ConsultantBankAccount> getByAllConsultantBankAccounts();
    
    public ConsultantBankAccount getByConsultantBankAccount(Long consultantId);

    ConsultantBankAccount update(Long id, ConsultantBankAccount bank);

    public void delete(Long id);
}