package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.entity.ConsultantBankAccount;

public interface ConsultantBankAccountRepository extends JpaRepository<ConsultantBankAccount, Long> {

    List<ConsultantBankAccount> findByConsultantId(Long consultantId);
}