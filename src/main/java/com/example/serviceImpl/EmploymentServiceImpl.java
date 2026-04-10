package com.example.serviceImpl;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.example.DTO.ConsultantDTO;
import com.example.DTO.EmploymentDTO;
import com.example.DTO.EmploymentSortingRequestDTO;
import com.example.DTO.VendorDTO;
import com.example.entity.Consultant;
import com.example.entity.Employments;
import com.example.entity.Vendor;
import com.example.repository.ConsultantRepository;
import com.example.repository.EmploymentRepository;
import com.example.repository.VendorRepository;
import com.example.service.EmploymentService;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;


@Service
public class EmploymentServiceImpl implements EmploymentService {

    @Autowired
    private EmploymentRepository repo;
    
    @Autowired
    private ConsultantRepository consultantRepository;
    
    @Autowired
    private VendorRepository vendorRepository;
    
    @Value("${file.upload.po.path}")
    private String basePath; // ✅ injected dynamically

    private static final String UPLOAD_DIR = "uploads/";
//    @Override
//    public Employments saveEmployment(Employments emp, MultipartFile poFile) {
//
//        try {
//
//            // ================= CONSULTANT VALIDATION =================
//            if (emp.getConsultant() == null || emp.getConsultant().getId() == null) {
//                throw new RuntimeException("Consultant ID is required");
//            }
//
//            Consultant consultant = consultantRepository.findById(emp.getConsultant().getId())
//                    .orElseThrow(() -> new RuntimeException(
//                            "Consultant not found with id: " + emp.getConsultant().getId()));
//
//            emp.setConsultant(consultant); // ✅ FIXED
//
//            // ================= ADMIN VALIDATION =================
//            if (emp.getAdminId() == null) {
//                throw new RuntimeException("Admin ID is required");
//            }
//
//            // ================= FILE UPLOAD =================
//            if (poFile != null && !poFile.isEmpty()) {
//
//                long maxSize = 50 * 1024 * 1024;
//
//                if (poFile.getSize() > maxSize) {
//                    throw new RuntimeException("PO file size should not exceed 50MB");
//                }
//
//                String fileName = System.currentTimeMillis() + "_" + poFile.getOriginalFilename();
//
//                Path path = Paths.get(UPLOAD_DIR, fileName);
//                Files.createDirectories(path.getParent());
//                Files.write(path, poFile.getBytes());
//
//                emp.setPoUpload(fileName);
//            }
//
//            return repo.save(emp);
//
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to save employment: " + e.getMessage());
//        }
//    }
    @Override
    public Employments saveEmployment(Employments emp, MultipartFile poFile) {

        try {

            // ================= CONSULTANT =================
            if (emp.getConsultant() == null || emp.getConsultant().getId() == null) {
                throw new RuntimeException("Consultant ID is required");
            }

            Consultant consultant = consultantRepository.findById(emp.getConsultant().getId())
                    .orElseThrow(() -> new RuntimeException(
                            "Consultant not found with id: " + emp.getConsultant().getId()));

            emp.setConsultant(consultant);

         // ================= VENDOR =================
            if (emp.getVendor() != null && emp.getVendor().getVendorId() != null) {

                Long consultantId = consultant.getId();
                Long vendorId = emp.getVendor().getVendorId();

                // 🔥 DUPLICATE CHECK
                boolean exists = repo.existsByConsultant_IdAndVendor_VendorId(consultantId, vendorId);

                if (exists) {
                    throw new RuntimeException("This consultant already has this vendor assigned");
                }

                // ✅ FETCH VENDOR FROM DB
                Vendor vendor = vendorRepository.findById(vendorId)
                        .orElseThrow(() -> new RuntimeException("Vendor not found with id: " + vendorId));

                emp.setVendor(vendor);
            }

            // ================= ADMIN =================
            if (emp.getAdminId() == null) {
                throw new RuntimeException("Admin ID is required");
            }

            // ================= FILE =================
            if (poFile != null && !poFile.isEmpty()) {

                long maxSize = 50 * 1024 * 1024;

                if (poFile.getSize() > maxSize) {
                    throw new RuntimeException("PO file size should not exceed 50MB");
                }

                String fileName = System.currentTimeMillis() + "_" + poFile.getOriginalFilename();

                Path path = Paths.get(UPLOAD_DIR, fileName);
                Files.createDirectories(path.getParent());
                Files.write(path, poFile.getBytes());

                emp.setPoUpload(fileName);
            }

            return repo.save(emp);

        } catch (Exception e) {
            throw new RuntimeException("Failed to save employment: " + e.getMessage());
        }
    }
  
    @Override
    public Employments updateEmployment(Long id, Employments emp, MultipartFile poFile) {

        Employments existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Employment not found with ID: " + id));

        try {

            // ================= CONSULTANT =================
            if (emp.getConsultant() == null || emp.getConsultant().getId() == null) {
                throw new RuntimeException("Consultant ID is required");
            }

            Consultant consultant = consultantRepository.findById(emp.getConsultant().getId())
                    .orElseThrow(() -> new RuntimeException(
                            "Consultant not found with id: " + emp.getConsultant().getId()));

            existing.setConsultant(consultant);

            // ================= VENDOR =================
            if (emp.getVendor() != null && emp.getVendor().getVendorId() != null) {

                Long consultantId = consultant.getId();
                Long vendorId = emp.getVendor().getVendorId();

                // ✅ FIXED (empId used)
                boolean exists = repo.existsByConsultant_IdAndVendor_VendorIdAndEmpIdNot(
                        consultantId, vendorId, id);

                if (exists) {
                    throw new RuntimeException("This consultant already has this vendor assigned");
                }

                Vendor vendor = vendorRepository.findById(vendorId)
                        .orElseThrow(() -> new RuntimeException("Vendor not found with id: " + vendorId));

                existing.setVendor(vendor);

            } else {
                existing.setVendor(null); // optional
            }

            // ================= ADMIN =================
            if (emp.getAdminId() == null) {
                throw new RuntimeException("Admin ID is required");
            }
            existing.setAdminId(emp.getAdminId());

            // ================= FILE =================
            if (poFile != null && !poFile.isEmpty()) {

                long maxSize = 50 * 1024 * 1024;

                if (poFile.getSize() > maxSize) {
                    throw new RuntimeException("PO file size should not exceed 50MB");
                }

                String fileName = System.currentTimeMillis() + "_" + poFile.getOriginalFilename();

                Path path = Paths.get(UPLOAD_DIR, fileName);
                Files.createDirectories(path.getParent());
                Files.write(path, poFile.getBytes());

                existing.setPoUpload(fileName);
            }

            // ================= UPDATE FIELDS =================
            existing.setWorkLocation(emp.getWorkLocation());
            existing.setClient(emp.getClient());
            existing.setBillRate(emp.getBillRate());
            existing.setPaymentFrequency(emp.getPaymentFrequency());
            existing.setNetTerm(emp.getNetTerm());
            existing.setProjectStartDate(emp.getProjectStartDate());
            existing.setProjectEndDate(emp.getProjectEndDate());
            existing.setClientHireDate(emp.getClientHireDate());
            existing.setInvoiceMail(emp.getInvoiceMail());

            return repo.save(existing);

        } catch (Exception e) {
            throw new RuntimeException("Failed to update employment: " + e.getMessage());
        }
    }
    
    
    // ✅ GET ALL
    @Override
    public ResponseEntity<List<Employments>> getAll() {
        List<Employments> list = repo.findAll();

        if (list.isEmpty()) {
            throw new RuntimeException("No employment records found");
        }

        return ResponseEntity.ok(list);
    }

    // ✅ GET BY ID
    @Override
    public ResponseEntity<Employments> getById(Long id) {
        Employments emp = repo.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Employment not found with ID: " + id));

        return ResponseEntity.ok(emp);
    }

    // ✅ DELETE
    @Override
    public ResponseEntity<String> deleteEmployment(Long id) {

        if (!repo.existsById(id)) {
            throw new RuntimeException("Employment not found with ID: " + id);
        }

        repo.deleteById(id);

        return ResponseEntity.ok("Employment deleted successfully");
    }
    
    @Override
    public Resource getPoFile(Long id) {

        Employments emp = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Employment not found"));

        Path filePath = Paths.get(UPLOAD_DIR, emp.getPoUpload()).normalize();

        try {
            UrlResource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                throw new RuntimeException("File not found");
            }

            return resource;

        } catch (Exception e) {
            throw new RuntimeException("Error fetching file: " + e.getMessage());
        }
    }
    
   
    @Override
    public Resource getPoFile(String fileName) {

        try {
            fileName = fileName.trim();

            Path baseDir = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();

            Path filePath = baseDir.resolve(fileName).normalize();

            System.out.println("👉 Final Path: " + filePath);

            if (!filePath.startsWith(baseDir)) {
                throw new RuntimeException("Invalid file path");
            }

            if (!Files.exists(filePath)) {
                throw new RuntimeException("File NOT FOUND. Check DB or upload path.");
            }

            if (!Files.isReadable(filePath)) {
                throw new RuntimeException("File NOT readable.");
            }

            return new UrlResource(filePath.toUri());

        } catch (Exception e) {
            throw new RuntimeException("Error fetching file: " + e.getMessage());
        }
    }
    
	@Override
	public EmploymentDTO mapToDTO(Employments emp) {

	    EmploymentDTO dto = new EmploymentDTO();

	    dto.setEmpId(emp.getEmpId());
	    dto.setWorkLocation(emp.getWorkLocation());
	    dto.setClientHireDate(emp.getClientHireDate());
	    dto.setProjectStartDate(emp.getProjectStartDate());
	    dto.setProjectEndDate(emp.getProjectEndDate());
	    dto.setClient(emp.getClient());
	    dto.setInvoiceMail(emp.getInvoiceMail());
	    dto.setNetTerm(emp.getNetTerm() != null ? emp.getNetTerm().name() : null);
	    dto.setPaymentFrequency(emp.getPaymentFrequency());
	    dto.setBillRate(emp.getBillRate());
	    dto.setPoUpload(emp.getPoUpload());

	    // ✅ Consultant full mapping
	    if (emp.getConsultant() != null) {
	        Consultant c = emp.getConsultant();

	        ConsultantDTO cdto = new ConsultantDTO();
	        cdto.setId(c.getId());
	        cdto.setFirstName(c.getFirstName());
	        cdto.setLastName(c.getLastName());
	        cdto.setEmail(c.getEmail());
	        cdto.setMobileNumber(c.getMobileNumber());

	        dto.setConsultant(cdto);
	        dto.setConsultantId(c.getId());
	        dto.setConsultantName(c.getFirstName() + " " + c.getLastName());
	    }
	    
	    if (emp.getVendor() != null) {
	        Vendor v = emp.getVendor();

	        VendorDTO vdto = new VendorDTO();
	        vdto.setVendorId(v.getVendorId());
	        vdto.setVendorName(v.getVendorName());
	        vdto.setEmail(v.getEmail());
	        vdto.setVendorType(v.getVendorType());

	        dto.setVendor(vdto);
	    }

	    return dto;
	}
    
	
	@Override
	public Page<Employments> getEmploymentsByAdmin(EmploymentSortingRequestDTO requestDTO) {

	    String search = requestDTO.getSearch();
	    String sortBy = requestDTO.getSortField();
	    String sortDir = requestDTO.getSortOrder();
	    Integer pageNo = requestDTO.getPageNumber();
	    Integer pageSize = requestDTO.getPageSize();
	    Long adminId = requestDTO.getAdminId();

	    // ✅ Default handling
	    if (pageNo == null || pageNo < 0) pageNo = 0;
	    int zeroBasedPageNo = (pageNo > 0) ? pageNo - 1 : pageNo;

	    if (pageSize == null || pageSize <= 0) pageSize = 10;

	    if (sortBy == null || sortBy.trim().isEmpty()) sortBy = "empId";
	    if (sortDir == null || sortDir.trim().isEmpty()) sortDir = "desc";

	    // ✅ Mapping (like invoice)
	    switch (sortBy.toLowerCase()) {
	        case "client": sortBy = "client"; break;
	        case "worklocation": sortBy = "workLocation"; break;
	        case "paymentfrequency": sortBy = "paymentFrequency"; break;
	        case "billrate": sortBy = "billRate"; break;
	        case "clienthiredate": sortBy = "clientHireDate"; break;
	        case "projectstartdate": sortBy = "projectStartDate"; break;
	        case "projectenddate": sortBy = "projectEndDate"; break;
	        default: sortBy = "empId";
	    }

	    Sort.Direction direction = sortDir.equalsIgnoreCase("desc")
	            ? Sort.Direction.DESC
	            : Sort.Direction.ASC;

	    Pageable pageable = PageRequest.of(zeroBasedPageNo, pageSize, Sort.by(direction, sortBy));

	    boolean hasSearch = search != null && !search.trim().isEmpty();

	    if (hasSearch) {
	        return repo.searchEmploymentsByAdmin(
	                adminId,
	                search.toLowerCase().trim(),
	                pageable
	        );
	    }

	    return repo.findByAdminId(adminId, pageable);
	}

	@Override
	public List<Employments> getEmploymentsByVendorIdAndAdminId(Long vendorId, Long adminId) {

	    if (!vendorRepository.existsById(vendorId)) {
	        throw new RuntimeException("Vendor not found with id: " + vendorId);
	    }

	    return repo.findEmploymentsByVendorAndAdminNative(vendorId, adminId);
	}
    
}