package com.example.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import com.example.entity.Employments;

@Repository
public interface EmploymentRepository extends JpaRepository<Employments, Long> {

    boolean existsByConsultant_IdAndVendor_VendorId(Long consultantId, Long vendorId);

    // ✅ Without search
    Page<Employments> findByAdminId(Long adminId, Pageable pageable);

    // ✅ With search
    @Query("""
    	    SELECT e FROM Employments e
    	    WHERE e.adminId = :adminId
    	    AND (
    	        LOWER(e.client) LIKE %:search% OR
    	        LOWER(e.workLocation) LIKE %:search% OR
    	        LOWER(e.paymentFrequency) LIKE %:search% OR
    	        LOWER(e.poUpload) LIKE %:search% OR

    	        LOWER(CAST(e.billRate AS string)) LIKE %:search% OR
    	        LOWER(CAST(e.clientHireDate AS string)) LIKE %:search% OR
    	        LOWER(CAST(e.projectStartDate AS string)) LIKE %:search% OR
    	        LOWER(CAST(e.projectEndDate AS string)) LIKE %:search% OR
    	        LOWER(CAST(e.netTerm AS string)) LIKE %:search% OR
    	        LOWER(CAST(e.adminId AS string)) LIKE %:search%
    	    )
    	""")
    	Page<Employments> searchEmploymentsByAdmin(
    	        @Param("adminId") Long adminId,
    	        @Param("search") String search,
    	        Pageable pageable
    	);

 
	
}
