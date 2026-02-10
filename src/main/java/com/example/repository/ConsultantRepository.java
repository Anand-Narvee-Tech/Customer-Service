package com.example.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.entity.Consultant;

@Repository
public interface ConsultantRepository extends JpaRepository<Consultant, Long> {

	Optional<Consultant> findByCid(String cid);

	boolean existsByEmailIgnoreCase(String email);

	List<Consultant> findByStatus(String status);

	boolean existsByCid(String cid);

	List<Consultant> findByVendor_VendorId(Long vendorId);

	Page<Consultant> findAll(Pageable pageable);

	@Query("""
			    SELECT c FROM Consultant c
			    WHERE
			        LOWER(c.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
			     OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))
			     OR LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
			     OR LOWER(c.mobileNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))
			     OR LOWER(c.cid) LIKE LOWER(CONCAT('%', :keyword, '%'))
			     OR LOWER(c.status) LIKE LOWER(CONCAT('%', :keyword, '%'))
			""")
	Page<Consultant> searchConsultants(@Param("keyword") String keyword, Pageable pageable);

}
