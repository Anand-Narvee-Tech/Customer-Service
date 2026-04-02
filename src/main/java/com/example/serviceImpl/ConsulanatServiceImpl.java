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
import com.example.DTO.NetTerm;
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

//	@Override
//	public Consultant save(Consultant req, MultipartFile file) {
//
//	    if (consultantRepository.existsByEmailIgnoreCase(req.getEmail())) {
//	        throw new RuntimeException("Consultant already exists with this email");
//	    }
//
//	    if (req.getVendor() == null || req.getVendor().getVendorId() == null) {
//	        throw new RuntimeException("Vendor ID is required");
//	    }
//
//	    // ================= NetTerm Validation =================
//	    if (req.getNetTerm() == null) {
//	        throw new RuntimeException(
//	            "Net Term is required. Allowed values: NET_7, NET_14, NET_30, NET_45, NET_60, NET_75, NET_120"
//	        );
//	    }
//
//	    // Optional: validate against Enum just to be safe
//	    boolean valid = false;
//	    for (NetTerm n : NetTerm.values()) {
//	        if (n == req.getNetTerm()) {
//	            valid = true;
//	            break;
//	        }
//	    }
//	    if (!valid) {
//	        throw new RuntimeException(
//	            "Invalid Net Term. Allowed values: NET_7, NET_14, NET_30, NET_45, NET_60, NET_75, NET_120"
//	        );
//	    }
//
//	    Long vendorId = req.getVendor().getVendorId();
//
//	    Vendor vendor = vendorRepository.findById(vendorId)
//	            .orElseThrow(() -> new RuntimeException("Vendor not found with id: " + vendorId));
//
//	    req.setVendor(vendor);
//
//	    // File Upload
//	    if (file != null && !file.isEmpty()) {
//	        long maxSize = 50 * 1024 * 1024;
//
//	        if (file.getSize() > maxSize) {
//	            throw new RuntimeException("File size should not exceed 50MB");
//	        }
//
//	        req.setDocumentPath(storeFile(file));
//	    }
//	    
//
//	    // ================= NEW LOGIC (BANK ACCOUNTS) =================
//	    if (req.getBankAccounts() != null && !req.getBankAccounts().isEmpty()) {
//	        req.getBankAccounts().forEach(bank -> bank.setConsultant(req));
//	    }
//
//	    // old logic unchanged
//	    return consultantRepository.save(req);
//	}
	
	//today
//	@Override
//	public Consultant save(Consultant req, MultipartFile file) {
//
//	    // ✅ Email validation
//	    if (consultantRepository.existsByEmailIgnoreCase(req.getEmail())) {
//	        throw new RuntimeException("Consultant already exists with this email");
//	    }
//
//	    // ✅ Vendor validation
//	    if (req.getVendor() == null || req.getVendor().getVendorId() == null) {
//	        throw new RuntimeException("Vendor ID is required");
//	    }
//
//	    // ✅ NetTerm validation
//	    if (req.getNetTerm() == null) {
//	        throw new RuntimeException(
//	                "Net Term is required. Allowed values: NET_7, NET_14, NET_30, NET_45, NET_60, NET_75, NET_120"
//	        );
//	    }
//
//	    boolean valid = false;
//	    for (NetTerm n : NetTerm.values()) {
//	        if (n == req.getNetTerm()) {
//	            valid = true;
//	            break;
//	        }
//	    }
//
//	    if (!valid) {
//	        throw new RuntimeException(
//	                "Invalid Net Term. Allowed values: NET_7, NET_14, NET_30, NET_45, NET_60, NET_75, NET_120"
//	        );
//	    }
//
//	    // ✅ Fetch Vendor
//	    Long vendorId = req.getVendor().getVendorId();
//
//	    Vendor vendor = vendorRepository.findById(vendorId)
//	            .orElseThrow(() -> new RuntimeException("Vendor not found with id: " + vendorId));
//
//	    req.setVendor(vendor);
//
//	    // ✅ File Upload
//	    if (file != null && !file.isEmpty()) {
//
//	        long maxSize = 50 * 1024 * 1024;
//
//	        if (file.getSize() > maxSize) {
//	            throw new RuntimeException("File size should not exceed 50MB");
//	        }
//
//	        req.setDocumentPath(storeFile(file));
//	    }
//
//	    // ✅ IMPORTANT FIX (Bank Accounts mapping)
//	    if (req.getBankAccounts() != null && !req.getBankAccounts().isEmpty()) {
//
//	        req.getBankAccounts().forEach(bank -> {
//
//	            // 🔥 VERY IMPORTANT
//	            bank.setConsultant(req);
//
//	            // ✅ Optional safety (avoid null DB errors)
//	            if (bank.getAccountNumber() == null || bank.getAccountNumber().isEmpty()) {
//	                throw new RuntimeException("Bank account number is required");
//	            }
//
//	            if (bank.getBankName() == null || bank.getBankName().isEmpty()) {
//	                throw new RuntimeException("Bank name is required");
//	            }
//	        });
//	    }
//
//	    return consultantRepository.save(req);
//	}
//	
	@Override
	public Consultant save(Consultant req, MultipartFile file, MultipartFile w4Form, MultipartFile voidCheque) {

	    // ✅ Email validation
	    if (consultantRepository.existsByEmailIgnoreCase(req.getEmail())) {
	        throw new RuntimeException("Consultant already exists with this email");
	    }

	    // ✅ Vendor validation
	    if (req.getVendor() == null || req.getVendor().getVendorId() == null) {
	        throw new RuntimeException("Vendor ID is required");
	    }

	    // ✅ NetTerm validation
	    if (req.getNetTerm() == null) {
	        throw new RuntimeException(
	                "Net Term is required. Allowed values: NET_7, NET_14, NET_30, NET_45, NET_60, NET_75, NET_120"
	        );
	    }

	    boolean valid = false;
	    for (NetTerm n : NetTerm.values()) {
	        if (n == req.getNetTerm()) {
	            valid = true;
	            break;
	        }
	    }

	    if (!valid) {
	        throw new RuntimeException(
	                "Invalid Net Term. Allowed values: NET_7, NET_14, NET_30, NET_45, NET_60, NET_75, NET_120"
	        );
	    }

	    // ✅ Fetch Vendor
	    Long vendorId = req.getVendor().getVendorId();

	    Vendor vendor = vendorRepository.findById(vendorId)
	            .orElseThrow(() -> new RuntimeException("Vendor not found with id: " + vendorId));

	    req.setVendor(vendor);

	    // ✅ File Upload (main file)
	    if (file != null && !file.isEmpty()) {

	        long maxSize = 50 * 1024 * 1024;

	        if (file.getSize() > maxSize) {
	            throw new RuntimeException("File size should not exceed 50MB");
	        }

	        req.setDocumentPath(storeFile(file));
	    }

	    // ✅ W4 Form
	    if (w4Form != null && !w4Form.isEmpty()) {
	        req.setW4Form(storeFile(w4Form));
	    }

	    // ✅ Void Cheque
	    if (voidCheque != null && !voidCheque.isEmpty()) {
	        req.setVoidCheque(storeFile(voidCheque));
	    }

	    // ✅ Bank Accounts mapping
	    if (req.getBankAccounts() != null && !req.getBankAccounts().isEmpty()) {

	        req.getBankAccounts().forEach(bank -> {

	            bank.setConsultant(req);

	            if (bank.getAccountNumber() == null || bank.getAccountNumber().isEmpty()) {
	                throw new RuntimeException("Bank account number is required");
	            }

	            if (bank.getBankName() == null || bank.getBankName().isEmpty()) {
	                throw new RuntimeException("Bank name is required");
	            }
	        });
	    }

	    return consultantRepository.save(req);
	}
	
	
	
	
	
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
	public Consultant update(Long id, Consultant req, MultipartFile file,MultipartFile w4Form, MultipartFile voidCheque) {

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

		if (req.getState() != null)
			existing.setState(req.getState());

		if (req.getCountry() != null)
			existing.setCountry(req.getCountry());

		if (req.getPincode() != null)
			existing.setPincode(req.getPincode());
		// ================= Personal Details =================
		if (req.getDateOfBirth() != null)
		    existing.setDateOfBirth(req.getDateOfBirth());

		if (req.getGender() != null)
		    existing.setGender(req.getGender());

		if (req.getMaritalStatus() != null)
		    existing.setMaritalStatus(req.getMaritalStatus());

		if (req.getNumberOfChildren() != null)
		    existing.setNumberOfChildren(req.getNumberOfChildren());

		if (req.getSecurityNumber() != null)
		    existing.setSecurityNumber(req.getSecurityNumber());

		if (req.getPersonalEmail() != null)
		    existing.setPersonalEmail(req.getPersonalEmail());

		// ================= Work Details =================
		if (req.getHireDate() != null)
		    existing.setHireDate(req.getHireDate());

		if (req.getClienthireDate() != null)
		    existing.setClienthireDate(req.getClienthireDate());

		if (req.getWorkLocation() != null)
		    existing.setWorkLocation(req.getWorkLocation());

		if (req.getAlternateNumber() != null)
		    existing.setAlternateNumber(req.getAlternateNumber());

		// ================= Visa Details =================
		if (req.getVisaType() != null)
		    existing.setVisaType(req.getVisaType());

		if (req.getVisaStartDate() != null)
		    existing.setVisaStartDate(req.getVisaStartDate());

		if (req.getVisaEndDate() != null)
		    existing.setVisaEndDate(req.getVisaEndDate());

		if (req.getW4Form() != null)
		    existing.setW4Form(req.getW4Form());

		if (req.getVoidCheque() != null)
		    existing.setVoidCheque(req.getVoidCheque());

		// ================= Project / Payment =================
		if (req.getPaymentFrequency() != null)
		    existing.setPaymentFrequency(req.getPaymentFrequency());

		if (req.getProjectStartDate() != null)
		    existing.setProjectStartDate(req.getProjectStartDate());

		if (req.getProjectEndDate() != null)
		    existing.setProjectEndDate(req.getProjectEndDate());
		// ================= Vendor =================
		if (req.getVendor() != null && req.getVendor().getVendorId() != null) {

			Long vendorId = req.getVendor().getVendorId();

			Vendor vendor = vendorRepository.findById(vendorId)
					.orElseThrow(() -> new IllegalArgumentException("Vendor not found with id: " + vendorId));

			existing.setVendor(vendor);
		}

		//Bhargav-31-03-26
		  // ================= Bank Accounts =================
	    if (req.getBankAccounts() != null) {
	        // Clear old accounts and add updated ones
	        existing.getBankAccounts().clear();
	        req.getBankAccounts().forEach(account -> {
	            account.setConsultant(existing); // set back-reference if bi-directional
	            existing.getBankAccounts().add(account);
	        });
	    }

	    // ================= Contributions =================
	    if (req.getContributions() != null) {
	        // Clear old contributions and add updated ones
	        existing.getContributions().clear();
	        req.getContributions().forEach(contribution -> {
	            contribution.setConsultant(existing); // set back-reference if bi-directional
	            existing.getContributions().add(contribution);
	        });
	    }
	
		//Bhargav-31-03-26
	
	 // ================= File Upload =================
	    if (file != null && !file.isEmpty()) {
	        existing.setDocumentPath(storeFile(file));
	    }

	    // ✅ ADD THIS (w4Form)
	    if (w4Form != null && !w4Form.isEmpty()) {
	        existing.setW4Form(storeFile(w4Form));
	    }

	    // ✅ ADD THIS (voidCheque)
	    if (voidCheque != null && !voidCheque.isEmpty()) {
	        existing.setVoidCheque(storeFile(voidCheque));
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

		Optional<Consultant> consultantOpt = consultantRepository.findById(id);

		if (consultantOpt.isEmpty()) {
			return Optional.empty();
		}

		Consultant consultant = consultantOpt.get();

		// Delete file if exists
		if (consultant.getDocumentPath() != null) {
			try {
				java.nio.file.Path path = java.nio.file.Paths.get(consultant.getDocumentPath());
				java.nio.file.Files.deleteIfExists(path);
			} catch (Exception e) {
				System.out.println("File delete failed: " + e.getMessage());
			}
		}

		consultantRepository.delete(consultant);

		return Optional.of(consultant);
	}

	

//	@Override
//	public Consultant save(Consultant req, MultipartFile file) {
//
//		if (consultantRepository.existsByEmailIgnoreCase(req.getEmail())) {
//			throw new RuntimeException("Consultant already exists with this email");
//		}
//
//
//	    if (req.getVendor() == null || req.getVendor().getVendorId() == null) {
//	        throw new RuntimeException("Vendor ID is required");
//	    }
//
//	    Long vendorId = req.getVendor().getVendorId();
//
//	    Vendor vendor = vendorRepository.findById(vendorId)
//	            .orElseThrow(() -> new RuntimeException("Vendor not found with id: " + vendorId));
//
//
//		// File Upload
//		if (file != null && !file.isEmpty()) {
//
//			// 50MB validation
//			long maxSize = 50 * 1024 * 1024;
//
//			if (file.getSize() > maxSize) {
//				throw new RuntimeException("File size should not exceed 50MB");
//			}
//
//			req.setDocumentPath(storeFile(file));
//		}
//		return req;
//
//		
//	}

}
