package com.example.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.entity.Consultant;
import com.example.entity.Vendor;

@Repository
public interface ConsultantRepository extends JpaRepository<Consultant, Long> {

	Optional<Consultant> findByCid(String cid);

	boolean existsByEmailIgnoreCase(String email);

	List<Consultant> findByStatus(String status);

	boolean existsByCid(String cid);

	List<Consultant> findByVendor_VendorId(Long vendorId);

	Page<Consultant> findAll(Pageable pageable);

//bhargav 21/02/26
	
	

    Page<Consultant> findByAdminId(Long adminId, Pageable pageable);

    // Search only
    @Query("""
        SELECT c FROM Consultant c
        WHERE LOWER(c.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(c.mobileNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(c.cid) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(c.status) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    Page<Consultant> searchByKeyword(@Param("keyword") String keyword,
                                     Pageable pageable);


    // Search + AdminId filter
    @Query("""
        SELECT c FROM Consultant c
        WHERE c.adminId = :adminId
        AND (
            LOWER(c.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
         OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))
         OR LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
         OR LOWER(c.mobileNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))
         OR LOWER(c.cid) LIKE LOWER(CONCAT('%', :keyword, '%'))
         OR LOWER(c.status) LIKE LOWER(CONCAT('%', :keyword, '%'))
        )
    """)
    Page<Consultant> findByAdminIdAndKeyword(@Param("adminId") Long adminId,
                                             @Param("keyword") String keyword,
                                             Pageable pageable);
	
//bhargav 21/02/26

}
