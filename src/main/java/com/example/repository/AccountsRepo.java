package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Accounts;

@Repository
public interface AccountsRepo extends JpaRepository<Accounts, Long> {

	List<Accounts> findByStatus(String status);

	boolean existsByInvoiceNumber(String invoiceNumber);

	List<Accounts> findByVendorVendorId(Long vendorId);
}
