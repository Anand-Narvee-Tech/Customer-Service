package com.example.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.entity.Consultant;

@Repository
public interface ConsultantRepository extends JpaRepository<Consultant, Long> {

	Optional<Consultant> findByCid(String cid);

	boolean existsByEmailIgnoreCase(String email);

	List<Consultant> findByStatus(String status);

	boolean existsByCid(String cid);

	List<Consultant> findByVendor_VendorId(Long vendorId);

}
