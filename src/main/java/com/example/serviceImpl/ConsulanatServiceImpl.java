package com.example.serviceImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.DTO.ConsultantRequest;
import com.example.DTO.SearchRequest;
import com.example.entity.Consultant;
import com.example.entity.Vendor;
import com.example.repository.ConsultantRepository;
import com.example.repository.VendorRepository;
import com.example.service.ConsulanatService;

@Service
public class ConsulanatServiceImpl implements ConsulanatService {

	@Autowired
	private ConsultantRepository consultantRepository;

	@Autowired
	private VendorRepository vendorRepository;

	
//	private String storeFile(MultipartFile file) {
//
//	    try {
//
//	        Path uploadDir = Paths.get("uploads/consultants");
//
//	        if (!Files.exists(uploadDir)) {
//	            Files.createDirectories(uploadDir);
//	        }
//
//	        String originalFileName = file.getOriginalFilename();
//
//	        String fileName = UUID.randomUUID() + "_" + originalFileName;
//
//	        Path filePath = uploadDir.resolve(fileName);
//
//	        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//
//	        return filePath.toString();
//
//	    } catch (IOException e) {
//
//	        throw new RuntimeException("File upload failed: " + e.getMessage());
//	    }
//	}


	@Override
	public Consultant getById(Long cid) {
		return consultantRepository.findById(cid).orElseThrow(() -> new RuntimeException("Consultant not found"));
	}
//working
//	@Override
//	public Page<Consultant> getAllOrSearch(SearchRequest request) {
//
//		int pageNo = request.getPageNo() != null ? request.getPageNo() : 0;
//
//		Integer pageSize = request.getPageSize(); // 👈 NOT fixed
//
//		String sortField = request.getSortField() != null ? request.getSortField() : "id";
//		
//
//		String sortBy = request.getSortBy() != null ? request.getSortBy() : "asc";
//
//		Sort sort = sortBy.equalsIgnoreCase("desc") ? Sort.by(sortField).descending() : Sort.by(sortField).ascending();
//
//		Pageable pageable;
//
//		// 👉 If pageSize is NOT provided → unpaged
//		if (pageSize == null) {
//			pageable = Pageable.unpaged();
//		} else {
//			pageable = PageRequest.of(pageNo, pageSize, sort);
//		}
//
//		if (request.getKeyword() == null || request.getKeyword().isBlank()) {
//			return consultantRepository.findAll(pageable);
//		}
//
//		return consultantRepository.searchConsultants(request.getKeyword(), pageable);
//	}

	// Bhargav 21-02-26
	@Override
	public Page<Consultant> getConsultants(String keyword, Long adminId, PageRequest pageable) {

		// If both keyword and adminId present
		if (keyword != null && !keyword.isBlank() && adminId != null) {
			return consultantRepository.findByAdminIdAndKeyword(adminId, keyword, pageable);
		}

		// If only adminId present
		if (adminId != null) {
			return consultantRepository.findByAdminId(adminId, pageable);
		}

		// If only keyword present
		if (keyword != null && !keyword.isBlank()) {
			return consultantRepository.searchByKeyword(keyword, pageable);
		}

		// If nothing present
		return consultantRepository.findAll(pageable);
	}

//vasim
	@Override
	public Consultant update(Long id, Consultant req, MultipartFile file) {

		// ================= Fetch existing =================
		Consultant existing = consultantRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Consultant not found with id: " + id));

		// ================= Email uniqueness =================
		if (req.getEmail() != null && !req.getEmail().equalsIgnoreCase(existing.getEmail())
				&& consultantRepository.existsByEmailIgnoreCase(req.getEmail())) {

			throw new IllegalArgumentException("Consultant already exists with this email");
		}

		// ================= Basic Fields =================
		if (req.getFirstName() != null)
			existing.setFirstName(req.getFirstName());

		if (req.getLastName() != null)
			existing.setLastName(req.getLastName());

		if (req.getEmail() != null)
			existing.setEmail(req.getEmail());

		if (req.getMobileNumber() != null)
			existing.setMobileNumber(req.getMobileNumber());

		if (req.getBillRate() != null)
			existing.setBillRate(req.getBillRate());

		if (req.getStatus() != null)
			existing.setStatus(req.getStatus());

		if (req.getNetTerm() != null)
			existing.setNetTerm(req.getNetTerm());

		if (req.getClient() != null)
			existing.setClient(req.getClient());

		if (req.getInvoiceMail() != null)
			existing.setInvoiceMail(req.getInvoiceMail());

		// ================= Address =================
		if (req.getAddress() != null)
			existing.setAddress(req.getAddress());

		if (req.getSuite() != null)
			existing.setSuite(req.getSuite());

		if (req.getCity() != null)
			existing.setCity(req.getCity());

		if (req.getCountry() != null)
			existing.setCountry(req.getCountry());

		if (req.getPincode() != null)
			existing.setPincode(req.getPincode());

		// ================= Vendor =================
		if (req.getVendor() != null && req.getVendor().getVendorId() != null) {

			Long vendorId = req.getVendor().getVendorId();

			Vendor vendor = vendorRepository.findById(vendorId)
					.orElseThrow(() -> new IllegalArgumentException("Vendor not found with id: " + vendorId));

			existing.setVendor(vendor);
		}

		// ================= File Upload =================
		if (file != null && !file.isEmpty()) {
			existing.setDocumentPath(storeFile(file));
		}

		// ================= Audit =================
		existing.setUpdatedBy(getLoggedInUserId());

		// updatedAt handled automatically by @PreUpdate
		return consultantRepository.save(existing);
	}

	// vasim
	private String storeFile(MultipartFile file) {

		try {

			Path uploadDir = Paths.get("uploads/consultants");
			Files.createDirectories(uploadDir);

			String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
			Path filePath = uploadDir.resolve(fileName);

			Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

			return filePath.toString();

		} catch (IOException e) {
			throw new RuntimeException("File upload failed", e);
		}
	}

	private String storeFile1(MultipartFile file) {
		try {
			Path uploadDir = Paths.get("uploads/consultants");
			Files.createDirectories(uploadDir);

			String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
			Path filePath = uploadDir.resolve(fileName);

			Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

			return filePath.toString();
		} catch (IOException e) {
			throw new RuntimeException("File upload failed", e);
		}
	}

	@Override
	public void deactivate(Long id) {

		Consultant consultant = consultantRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Consultant not found with id: " + id));

		consultant.setStatus("INACTIVE");
		consultant.setUpdatedBy(getLoggedInUserId());

		consultantRepository.save(consultant);
	}

	private Long getLoggedInUserId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Consultant> getConsultantsByVendorId(Long vendorId) {

		if (!vendorRepository.existsById(vendorId)) {
			throw new RuntimeException("Vendor not found with id: " + vendorId);
		}

		return consultantRepository.findByVendor_VendorId(vendorId);
	}

	@Override
	public Optional<Consultant> deleteById(Long id) {

		// 1️⃣ Find consultant
		Optional<Consultant> consultantOpt = consultantRepository.findById(id);

		if (consultantOpt.isEmpty()) {
			throw new RuntimeException("Consultant not found with id: " + id);
		}

		Consultant consultant = consultantOpt.get();

		// 2️⃣ Delete document file (very important — you store documentPath)
		if (consultant.getDocumentPath() != null) {
			try {
				java.nio.file.Path path = java.nio.file.Paths.get(consultant.getDocumentPath());
				java.nio.file.Files.deleteIfExists(path);
			} catch (Exception e) {
				System.out.println("File delete failed: " + e.getMessage());
			}
		}

		// 3️⃣ Delete from DB
		consultantRepository.delete(consultant);

		// 4️⃣ return deleted consultant
		return Optional.of(consultant);
	}

	@Override
	public Consultant save(Consultant req, MultipartFile file) {

		if (consultantRepository.existsByEmailIgnoreCase(req.getEmail())) {
			throw new RuntimeException("Consultant already exists with this email");
		}


	    if (req.getVendor() == null || req.getVendor().getVendorId() == null) {
	        throw new RuntimeException("Vendor ID is required");
	    }

	    Long vendorId = req.getVendor().getVendorId();

	    Vendor vendor = vendorRepository.findById(vendorId)
	            .orElseThrow(() -> new RuntimeException("Vendor not found with id: " + vendorId));


		// File Upload
		if (file != null && !file.isEmpty()) {

			// 50MB validation
			long maxSize = 50 * 1024 * 1024;

			if (file.getSize() > maxSize) {
				throw new RuntimeException("File size should not exceed 50MB");
			}

			req.setDocumentPath(storeFile(file));
		}
		return req;

		
	}

}
