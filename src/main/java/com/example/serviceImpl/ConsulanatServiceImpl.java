package com.example.serviceImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.DTO.ConsultantRequest;
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

	    req.setVendor(vendor);

	    if (file != null && !file.isEmpty()) {
	        req.setDocumentPath(storeFile(file));
	    }

	    req.setCreatedBy(getLoggedInUserId());

	    return consultantRepository.save(req);
	}

	// ✅ File storage
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

	@Override
	public Consultant getById(Long cid) {
		return consultantRepository.findById(cid).orElseThrow(() -> new RuntimeException("Consultant not found"));
	}

	@Override
	public List<Consultant> getAll() {
		return consultantRepository.findAll();
	}

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

		// ================= Update fields =================
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

		// ================= Vendor =================
		if (req.getVendor() != null && req.getVendor().getVendorId() != null) {

			Long vendorId = req.getVendor().getVendorId();

			Vendor vendor = vendorRepository.findById(vendorId)
					.orElseThrow(() -> new IllegalArgumentException("Vendor not found with id: " + vendorId));

			existing.setVendor(vendor);
		}

		// ================= File =================
		if (file != null && !file.isEmpty()) {
			existing.setDocumentPath(storeFile1(file));
		}

		// ================= Audit =================
		existing.setUpdatedBy(getLoggedInUserId());
		// updatedAt handled by @PreUpdate

		return consultantRepository.save(existing);
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

}
