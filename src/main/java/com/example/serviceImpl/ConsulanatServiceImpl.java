package com.example.serviceImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.DTO.ConsultantBankAccountDTO;
import com.example.DTO.ConsultantDTO;
import com.example.DTO.ConsultantRequestDTO;
import com.example.DTO.ContributionDTO;
import com.example.DTO.EmploymentDTO;
import com.example.DTO.NetTerm;
import com.example.DTO.VendorDTO;
import com.example.entity.Consultant;
import com.example.entity.Vendor;
import com.example.repository.ConsultantRepository;
import com.example.repository.VendorRepository;
import com.example.service.ConsulanatService;

import jakarta.ws.rs.core.HttpHeaders;


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
	public Consultant save(Consultant req, MultipartFile w4Form, MultipartFile voidCheque) {

	    // ✅ Email validation
	    if (consultantRepository.existsByEmailIgnoreCase(req.getEmail())) {
	        throw new RuntimeException("Consultant already exists with this email");
	    }

//	    // ✅ Vendor validation
//	    if (req.getVendors() == null || req.getVendors().isEmpty()) {
//	        throw new RuntimeException("At least one vendor is required");
//	    }

	    // ================= MULTIPLE VENDORS =================
	    List<Vendor> vendorList = new ArrayList<>();

	    for (Vendor v : req.getVendors()) {

	        if (v.getVendorId() == null) {
	            throw new RuntimeException("Vendor ID is required");
	        }

	        Vendor vendorObj = vendorRepository.findById(v.getVendorId())
	                .orElseThrow(() -> new RuntimeException("Vendor not found with id: " + v.getVendorId()));

	        vendorList.add(vendorObj);
	    }

	    req.setVendors(vendorList);

	    // ================= FILE UPLOAD =================

	    long maxSize = 50 * 1024 * 1024;

	    // ✅ W4 Form (FIXED - only once)
	    if (w4Form != null && !w4Form.isEmpty()) {

	        if (w4Form.getSize() > maxSize) {
	            throw new RuntimeException("W4 file size should not exceed 50MB");
	        }

	        req.setW4Form(storeFile(w4Form));
	    }

	    // ✅ Void Cheque (ADDED VALIDATION)
	    if (voidCheque != null && !voidCheque.isEmpty()) {

	        if (voidCheque.getSize() > maxSize) {
	            throw new RuntimeException("Void Cheque file size should not exceed 50MB");
	        }

	        req.setVoidCheque(storeFile(voidCheque));
	    }

	    // ================= BANK ACCOUNTS =================
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
	public Consultant update(Long id, Consultant req,MultipartFile w4Form, MultipartFile voidCheque) {

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

		if (req.getStatus() != null)
			existing.setStatus(req.getStatus());

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
		
		// ================= Vendor (UPDATED FIX) =================
		if (req.getVendors() != null && !req.getVendors().isEmpty()) {

		    List<Vendor> vendorList = new ArrayList<>();

		    for (Vendor v : req.getVendors()) {

		        if (v.getVendorId() == null) {
		            throw new IllegalArgumentException("Vendor ID is required");
		        }

		        Vendor vendor = vendorRepository.findById(v.getVendorId())
		                .orElseThrow(() -> new IllegalArgumentException("Vendor not found with id: " + v.getVendorId()));

		        vendorList.add(vendor);
		    }

		    existing.setVendors(vendorList);
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
	    
	    // ================= EMPLOYMENTS  =================
	    if (req.getEmployments() != null) {
	        // Clear old contributions and add updated ones
	        existing.getEmployments().clear();
	        req.getEmployments().forEach(employment -> {
	        	employment.setConsultant(existing); // set back-reference if bi-directional
	            existing.getEmployments().add(employment);
	        });
	    }
	
		//Bhargav-31-03-26
	
	 // ================= File Upload =================
	  

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

		return consultantRepository.findByVendors_VendorId(vendorId);
	}

	@Override
	public Optional<Consultant> deleteById(Long id) {

		Optional<Consultant> consultantOpt = consultantRepository.findById(id);

		if (consultantOpt.isEmpty()) {
			return Optional.empty();
		}

		Consultant consultant = consultantOpt.get();

		// Delete file if exists
		if (consultant.getW4Form() != null) {
			try {
				java.nio.file.Path path = java.nio.file.Paths.get(consultant.getW4Form());
				java.nio.file.Files.deleteIfExists(path);
			} catch (Exception e) {
				System.out.println("File delete failed: " + e.getMessage());
			}
		}
		
		// Delete file if exists
				if (consultant.getVoidCheque() != null) {
					try {
						java.nio.file.Path path = java.nio.file.Paths.get(consultant.getVoidCheque());
						java.nio.file.Files.deleteIfExists(path);
					} catch (Exception e) {
						System.out.println("File delete failed: " + e.getMessage());
					}
				}

		consultantRepository.delete(consultant);

		return Optional.of(consultant);
	}


	@Override
	public ResponseEntity<Resource> previewFile(Long adminId, Long consultantId,
	        String type) {

	    try {
	        // Fetch consultant
	        Consultant consultant = consultantRepository.findById(consultantId)
	                .orElseThrow(() -> new RuntimeException("Consultant not found"));

	        // Admin validation
	        if (!consultant.getAdminId().equals(adminId)) {
	            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
	        }

	        // Map file type to corresponding consultant field
	        String filePath = switch (type.toLowerCase()) {
	            case "w4" -> consultant.getW4Form();
	            case "void" -> consultant.getVoidCheque();
	            default -> null;
	        };

	        // Check if file exists
	        if (filePath == null || filePath.trim().isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                    .body(null);
	        }

	        // Resolve paths
	        Path basePath = Paths.get("uploads").toAbsolutePath().normalize();
	        Path path = Paths.get(filePath).toAbsolutePath().normalize();

	        // Security check to prevent path traversal
	        if (!path.startsWith(basePath)) {
	            return ResponseEntity.badRequest().build();
	        }

	        Resource resource = new UrlResource(path.toUri());

	        if (!resource.exists() || !resource.isReadable()) {
	            return ResponseEntity.notFound().build();
	        }

	        // Detect content type dynamically
	        String contentType = Files.probeContentType(path);
	        if (contentType == null) {
	            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
	        }

	        return ResponseEntity.ok()
	                .contentType(MediaType.parseMediaType(contentType))
	                .header(HttpHeaders.CONTENT_DISPOSITION,
	                        "inline; filename=\"" + resource.getFilename() + "\"")
	                .body(resource);

	    } catch (Exception e) {
	        throw new RuntimeException("Error while previewing file: " + e.getMessage());
	    }
	}
	
	@Override
	public ConsultantRequestDTO mapToDTO(Consultant consultant) {

	    ConsultantRequestDTO dto = new ConsultantRequestDTO();

	    // ================= ✅ CONSULTANT =================
	    ConsultantDTO basic = new ConsultantDTO();

	    basic.setId(consultant.getId());
	    basic.setCid(consultant.getCid());
	    basic.setFirstName(consultant.getFirstName());
	    basic.setLastName(consultant.getLastName());
	    basic.setDateOfBirth(consultant.getDateOfBirth() != null ? consultant.getDateOfBirth().toString() : null);
	    basic.setGender(consultant.getGender());
	    basic.setMaritalStatus(consultant.getMaritalStatus());
	    basic.setNumberOfChildren(consultant.getNumberOfChildren());
	    basic.setSecurityNumber(consultant.getSecurityNumber());
	    basic.setPersonalEmail(consultant.getPersonalEmail());
	    basic.setEmail(consultant.getEmail());
	    basic.setMobileNumber(consultant.getMobileNumber());
	    basic.setAlternateNumber(consultant.getAlternateNumber());
	    basic.setAddress(consultant.getAddress());
	    basic.setW4Form(consultant.getW4Form());
	    basic.setVoidCheque(consultant.getVoidCheque());
	    basic.setSuite(consultant.getSuite());
	    basic.setCity(consultant.getCity());
	    basic.setState(consultant.getState());
	    basic.setCountry(consultant.getCountry());
	    basic.setPincode(consultant.getPincode());
	    basic.setHireDate(consultant.getHireDate() != null ? consultant.getHireDate().toString() : null);
	    basic.setVisaType(consultant.getVisaType());
	    basic.setVisaStartDate(consultant.getVisaStartDate() != null ? consultant.getVisaStartDate().toString() : null);
	    basic.setVisaEndDate(consultant.getVisaEndDate() != null ? consultant.getVisaEndDate().toString() : null);

	    dto.setConsultant(basic);

	    // ================= ✅ BANK =================
	    if (consultant.getBankAccounts() != null) {
	        List<ConsultantBankAccountDTO> bankList = consultant.getBankAccounts()
	                .stream()
	                .map(b -> {
	                    ConsultantBankAccountDTO bdto = new ConsultantBankAccountDTO();
	                    bdto.setBankName(b.getBankName());
	                    bdto.setAccountHolderName(b.getAccountHolderName());
	                    bdto.setRoutingNumber(b.getRoutingNumber());
	                    bdto.setAccountType(b.getAccountType());
	                    bdto.setId(b.getId());
	                    bdto.setAccountNumber(b.getAccountNumber());  // ✅ ADD THIS
	                    bdto.setIfscCode(b.getIfscCode());            // ✅ ADD THIS
	                    return bdto;
	                })
	                .toList();

	        dto.setBankAccounts(bankList);
	    }

	    // ================= ✅ CONTRIBUTIONS =================
	    if (consultant.getContributions() != null) {
	        List<ContributionDTO> contList = consultant.getContributions()
	                .stream()
	                .map(c -> {
	                    ContributionDTO cdto = new ContributionDTO();
	                    cdto.setContributionId(c.getContributionId());
	                    cdto.setOtherContribution(c.getOtherContribution());
	                    cdto.setContributionType(c.getContributionType());
	                    cdto.setAmount(c.getAmount());
	                    cdto.setAmountType(c.getAmountType());
	                    return cdto;
	                })
	                .toList();

	        dto.setContributions(contList);
	    }

	    // ================= ✅ EMPLOYMENTS =================
	    if (consultant.getEmployments() != null) {
	        List<EmploymentDTO> empList = consultant.getEmployments()
	                .stream()
	                .map(e -> {
	                    EmploymentDTO edto = new EmploymentDTO();

	                    edto.setWorkLocation(e.getWorkLocation());
	                    edto.setClientHireDate(e.getClientHireDate());
	                    edto.setProjectStartDate(e.getProjectStartDate());
	                    edto.setProjectEndDate(e.getProjectEndDate());
	                    edto.setClient(e.getClient());
	                    edto.setInvoiceMail(e.getInvoiceMail());
	                    edto.setNetTerm(e.getNetTerm() != null ? e.getNetTerm().name() : null);
	                    edto.setPaymentFrequency(e.getPaymentFrequency());
	                    edto.setBillRate(e.getBillRate());
	                    edto.setPoUpload(e.getPoUpload());

	                    // ================= ✅ FIXED VENDOR MAPPING =================
	                    if (e.getVendor() != null) {

	                        Vendor fullVendor = vendorRepository
	                                .findById(e.getVendor().getVendorId())
	                                .orElse(null);

	                        if (fullVendor != null) {
	                            VendorDTO vdto = new VendorDTO();

	                            vdto.setVendorId(fullVendor.getVendorId());
	                            vdto.setVendorName(fullVendor.getVendorName());
	                            vdto.setEmail(fullVendor.getEmail());
	                            vdto.setPhoneNumber(fullVendor.getPhoneNumber());
	                            vdto.setEinNumber(fullVendor.getEinNumber());
	                            vdto.setCreatedAt(fullVendor.getCreatedAt());
	                            vdto.setGstin(fullVendor.getGstin());
	                            vdto.setMsaAgreement(fullVendor.getMsaAgreement());
	                            vdto.setAddress(fullVendor.getAddress());
	                            vdto.setWebsite(fullVendor.getWebsite());
	                            vdto.setAdminId(fullVendor.getAdminId());
	                            vdto.setAttentionTo(fullVendor.getAttentionTo());
	                            vdto.setAdditionDoc(fullVendor.getAdditionDoc());
	                            vdto.setDiscount(fullVendor.getDiscount());
	                            vdto.setVendorType(fullVendor.getVendorType());

	                            edto.setVendor(vdto);
	                        }
	                    }

	                    return edto;
	                })
	                .toList();

	        dto.setEmployments(empList);
	    }

	    return dto;
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
