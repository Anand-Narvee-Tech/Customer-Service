package com.example.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.DTO.ConsultantRequestDTO;
import com.example.DTO.NetTerm;
import com.example.DTO.RestAPIResponse;
import com.example.DTO.SearchRequest;
import com.example.client.InvoiceFeignClient;
import com.example.entity.Consultant;
import com.example.repository.ConsultantRepository;
import com.example.service.ConsulanatService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/con")
public class ConsuantCon {

	@Autowired
	private ConsulanatService consultantServ;

	@Autowired
	private ConsultantRepository consultantRepository;

	@Autowired
	private InvoiceFeignClient invoiceFeignClient;
	
    private final ObjectMapper objectMapper; // Spring-managed ObjectMapper

    public ConsuantCon(ConsulanatService consultantServ, ObjectMapper objectMapper) {
        this.consultantServ = consultantServ;
        this.objectMapper = objectMapper;
    }

	// ================= CREATE =================
//	@PostMapping(value = "/saveConsultant", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//	public ResponseEntity<RestAPIResponse> createConsultant(@RequestPart("data") String dataJson, // ← String instead of
//																									// Consultant
//			@RequestPart(value = "file", required = false) MultipartFile file) {
//
//		try {
//			ObjectMapper objectMapper = new ObjectMapper();
//			Consultant data = objectMapper.readValue(dataJson, Consultant.class);
//
//			Consultant savedConsultant = consultantServ.save(data, file);
//
//			return ResponseEntity.status(HttpStatus.SC_OK)
//					.body(new RestAPIResponse("success", "Consultant created successfully", savedConsultant));
//
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			return ResponseEntity.status(HttpStatus.SC_OK)
//					.body(new RestAPIResponse("fail", "Error: " + ex.getMessage(), null));
//		}
//	}

//	working fine
	// ================= CREATE =================
//	@PostMapping(value = "/saveConsultant", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//	public ResponseEntity<RestAPIResponse> createConsultant(
//	        @RequestPart("data") String dataJson,
//	        @RequestPart(value = "file", required = false) MultipartFile file) {
//
//	    try {
//	        ObjectMapper objectMapper = new ObjectMapper();
//	        Consultant data = objectMapper.readValue(dataJson, Consultant.class);
//
//	        Consultant savedConsultant = consultantServ.save(data, file);
//
//	        return ResponseEntity.ok(
//	                new RestAPIResponse("success", "Consultant created successfully", savedConsultant));
//
//	    } catch (DataIntegrityViolationException ex) {
//	        // Handle DB constraint violations (like NetTerm)
//	        String message = ex.getMostSpecificCause().getMessage();
//
//	        if (message != null && message.contains("consultant_net_term_check")) {
//	            message = "Invalid Net Term. Allowed values: NET_7, NET_14, NET_30, NET_45, NET_60, NET_75, NET_120";
//	        } else if (message != null && message.toLowerCase().contains("email")) {
//	            message = "This email already exists. Please use a different email.";
//	        } else {
//	            message = "Database constraint violation: " + message;
//	        }
//
//	        return ResponseEntity.badRequest()
//	                .body(new RestAPIResponse("fail", message, null));
//
//	    } catch (IllegalArgumentException ex) {
//	        // Handle validation or not-found errors
//	        return ResponseEntity.badRequest()
//	                .body(new RestAPIResponse("fail", ex.getMessage(), null));
//
//	    } catch (RuntimeException ex) {
//	        // All other runtime errors
//	        String message = ex.getMessage() != null ? ex.getMessage() : "An unexpected error occurred";
//	        return ResponseEntity.badRequest()
//	                .body(new RestAPIResponse("fail", message, null));
//
//	    } catch (Exception ex) {
//	        // Fallback for any unhandled exceptions
//	        return ResponseEntity.internalServerError()
//	                .body(new RestAPIResponse("fail", "Unable to create consultant. Please try again.", null));
//	    }
//	}
	
	@PostMapping(value = "/saveConsultant", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<RestAPIResponse> createConsultant(
	        @RequestPart("data") String dataJson,
	        @RequestPart(value = "file", required = false) MultipartFile file) {

	    try {
	        ObjectMapper objectMapper = new ObjectMapper();
	        objectMapper.findAndRegisterModules(); // ✅ Fix for LocalDate

	        Consultant data = objectMapper.readValue(dataJson, Consultant.class);

	        Consultant savedConsultant = consultantServ.save(data, file);

	        return ResponseEntity.ok(
	                new RestAPIResponse("success", "Consultant created successfully", savedConsultant));

	    } catch (DataIntegrityViolationException ex) {

	        String message = ex.getMostSpecificCause() != null
	                ? ex.getMostSpecificCause().getMessage()
	                : ex.getMessage();

	        if (message != null && message.contains("consultant_net_term_check")) {
	            message = "Invalid Net Term. Allowed values: NET_7, NET_14, NET_30, NET_45, NET_60, NET_75, NET_120";
	        } else if (message != null && message.toLowerCase().contains("email")) {
	            message = "This email already exists. Please use a different email.";
	        } else if (message != null && message.toLowerCase().contains("not-null")) {
	            message = "Required field is missing. Please check your input.";
	        } else if (message != null && message.toLowerCase().contains("foreign key")) {
	            message = "Invalid reference data (Vendor or related entity not found).";
	        } else {
	            message = "Database error: " + message;
	        }

	        return ResponseEntity.badRequest()
	                .body(new RestAPIResponse("fail", message, null));

	    } catch (IllegalArgumentException ex) {

	        return ResponseEntity.badRequest()
	                .body(new RestAPIResponse("fail", ex.getMessage(), null));

	    } catch (RuntimeException ex) {

	        return ResponseEntity.badRequest()
	                .body(new RestAPIResponse("fail", ex.getMessage(), null));

	    } catch (Exception ex) {

	        ex.printStackTrace(); // ✅ for debugging

	        String message = ex.getMessage();

	        if (message == null || message.trim().isEmpty()) {
	            message = "Unexpected error occurred while creating consultant";
	        }

	        return ResponseEntity.internalServerError()
	                .body(new RestAPIResponse("fail", message, null));
	    }
	}
	
	
	
	
	// ================= UPDATE =================
	  @PutMapping("/{id}")
	    public ResponseEntity<RestAPIResponse> updateConsultant(
	            @PathVariable("id") Long id,
	            @RequestPart("data") String dataJson,
	            @RequestPart(value = "file", required = false) MultipartFile file) {

	        try {
	            // Use Spring's ObjectMapper with JavaTimeModule
	            Consultant request = objectMapper.readValue(dataJson, Consultant.class);

	            Consultant updatedConsultant = consultantServ.update(id, request, file);

	            return ResponseEntity.ok(
	                    new RestAPIResponse("success", "Consultant updated successfully", updatedConsultant)
	            );

	        } catch (IllegalArgumentException ex) {
	            ex.printStackTrace();
	            return ResponseEntity.badRequest().body(new RestAPIResponse("fail", ex.getMessage(), null));

	        } catch (Exception ex) {
	            ex.printStackTrace();
	            return ResponseEntity.internalServerError()
	                    .body(new RestAPIResponse("fail", "Error: " + ex.getMessage(), null));
	        }
	    }
	// ================= GET BY ID =================
	@GetMapping("/getByID/{id}")
	public ResponseEntity<RestAPIResponse> getConsultantById(@PathVariable("id") Long id) {

		Consultant consultant = consultantServ.getById(id);

		return ResponseEntity.ok(new RestAPIResponse("success", "Fetched successfully", consultant));
	}

	// ================= GET ALL =================

//Bhargav 21-02-26
//	@PostMapping("/searchAndSort")
//	public ResponseEntity<Page<Consultant>> searchAndSortConsultants(
//	        @RequestParam(defaultValue = "0") int page,
//	        @RequestParam(defaultValue = "10") int size,
//	        @RequestParam(defaultValue = "id") String sortField,
//	        @RequestParam(defaultValue = "asc") String sortDir,
//	        @RequestParam(required = false) String keyword,
//	        @RequestParam(required = false) Long adminId) {
//
//	    Sort sort = sortDir.equalsIgnoreCase("desc") ?
//	            Sort.by(sortField).descending() :
//	            Sort.by(sortField).ascending();
//
//	    PageRequest pageable = PageRequest.of(page, size, sort);
//
//	    Page<Consultant> result =
//	    		consultantServ.getConsultants(keyword, adminId, pageable);
//
//	    return ResponseEntity.ok(result);
//	}

	@PostMapping("/searchAndSort")
	public ResponseEntity<Page<Consultant>> searchAndSortConsultants(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size,
			@RequestParam(name = "sortField", defaultValue = "id") String sortField,
			@RequestParam(name = "sortDir", defaultValue = "asc") String sortDir,
			@RequestParam(name = "keyword", required = false) String keyword,
			@RequestParam(name = "adminId", required = false) Long adminId) {

		// sanitize keyword
		if (keyword == null) {
			keyword = "";
		}

		// validate sort direction
		Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

		Sort sort = Sort.by(direction, sortField);

		PageRequest pageable = PageRequest.of(page, size, sort);

		Page<Consultant> result = consultantServ.getConsultants(keyword, adminId, pageable);

		return ResponseEntity.ok(result);
	}

//Bhargav 21-02-26

	// ================= DEACTIVATE =================
	@DeleteMapping("/{id}")
	public ResponseEntity<RestAPIResponse> deactivateConsultant(@PathVariable("id") Long id) {

		consultantServ.deactivate(id);

		return ResponseEntity.ok(new RestAPIResponse("success", "Deactivated successfully", null));
	}

	@GetMapping("/vendors/{vendorId}/consultants")
	public ResponseEntity<RestAPIResponse> getConsultantsByVendor(@PathVariable("vendorId") Long vendorId) {
		List<Consultant> consultants = consultantServ.getConsultantsByVendorId(vendorId);

		return ResponseEntity.ok(new RestAPIResponse("success", "Consultants fetched successfully", consultants));
	}

	@GetMapping("/uploads/vendor-msa/{fileName:.+}")
	public ResponseEntity<Resource> getVendorMsaFile(@PathVariable("fileName") String fileName) throws IOException {

		String BASE_PATH = "uploads/vendor-msa";

		Path basePath = Paths.get(BASE_PATH).toAbsolutePath().normalize();
		Path filePath = basePath.resolve(fileName).normalize();

		// 🔐 Prevent path traversal (../)
		if (!filePath.startsWith(basePath)) {
			return ResponseEntity.badRequest().build();
		}

		Resource resource = new UrlResource(filePath.toUri());

		if (!resource.exists() || !resource.isReadable()) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM)
				.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}

	@GetMapping("/uploads/vendor-additional-docs/{fileName:.+}")
	public ResponseEntity<Resource> getVendorAdditionalDoc(@PathVariable("fileName") String fileName)
			throws IOException {

		String BASE_PATH = "uploads/vendor-additional-docs";

		Path basePath = Paths.get(BASE_PATH).toAbsolutePath().normalize();
		Path filePath = basePath.resolve(fileName).normalize();

		// 🔐 Security check
		if (!filePath.startsWith(basePath)) {
			return ResponseEntity.badRequest().build();
		}

		Resource resource = new UrlResource(filePath.toUri());

		if (!resource.exists() || !resource.isReadable()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM)
				.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}

	@GetMapping("/net-terms")
	public ResponseEntity<List<Map<String, Object>>> getNetTerms() {

		List<Map<String, Object>> response = Arrays.stream(NetTerm.values()).map(t -> {
			Map<String, Object> map = new HashMap<>();
			map.put("code", t.name());
			map.put("label", t.getLabel());
			map.put("days", t.getDays());
			return map;
		}).collect(Collectors.toList());

		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<RestAPIResponse> deleteConsultant(@PathVariable("id") Long id) {

		try {

			// Check invoices
			boolean hasInvoices = invoiceFeignClient.hasInvoices(id);

			if (hasInvoices) {
				return ResponseEntity.ok(new RestAPIResponse("fail",
						"Consultant cannot be deleted because invoices are associated with this consultant.", null));
			}

			Optional<Consultant> deletedConsultant = consultantServ.deleteById(id);

			if (deletedConsultant.isEmpty()) {
				return ResponseEntity.ok(new RestAPIResponse("fail", "Consultant not found with id: " + id, null));
			}

			return ResponseEntity
					.ok(new RestAPIResponse("success", "Consultant deleted successfully", deletedConsultant.get()));

		} catch (Exception e) {

			return ResponseEntity.ok(new RestAPIResponse("error",
					"Consultant cannot be deleted because invoices are associated with this consultant.", null));
		}
	}

	@GetMapping("/{id}")
	public Consultant getConsultant(@PathVariable("id") Long id) {
		return consultantRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Consultant not found with id: " + id));
	}

	

	
	
}