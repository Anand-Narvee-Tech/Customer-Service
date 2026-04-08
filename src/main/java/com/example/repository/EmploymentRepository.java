package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Employments;

@Repository
public interface EmploymentRepository extends JpaRepository<Employments, Long> {

    boolean existsByConsultant_IdAndVendor_VendorId(Long consultantId, Long vendorId);

	
}
