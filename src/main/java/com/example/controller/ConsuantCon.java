package com.example.controller;

import java.util.List;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.DTO.RestAPIResponse;
import com.example.entity.Consultant;
import com.example.service.ConsulanatService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/con")
public class ConsuantCon {

	@Autowired
	private ConsulanatService consultantServ;

	// ================= CREATE =================
//	@PostMapping(value = "/saveConsultant", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//	public ResponseEntity<RestAPIResponse> createConsultant(
//	        @RequestParam("data") String dataJson,
//	        @RequestParam(value = "file", required = false) MultipartFile file) {
//
//	    try {
//	        ObjectMapper mapper = new ObjectMapper();
//	        Consultant data = mapper.readValue(dataJson, Consultant.class);
//	        
//	        Consultant savedConsultant = consultantServ.save(data, file);
//
//	        return ResponseEntity.status(HttpStatus.SC_OK)
//	                .body(new RestAPIResponse("success", "Consultant created successfully", savedConsultant));
//
//	    } catch (Exception ex) {
//	        ex.printStackTrace();
//	        return ResponseEntity.status(HttpStatus.SC_OK)
//	                .body(new RestAPIResponse("fail", "Error: " + ex.getMessage(), null));
//	    }
//	}
	@PostMapping(value = "/saveConsultant", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<RestAPIResponse> createConsultant(
	        @RequestPart("data") String dataJson,  // ← String instead of Consultant
	        @RequestPart(value = "file", required = false) MultipartFile file) {

	    try {
	        ObjectMapper objectMapper = new ObjectMapper();
	        Consultant data = objectMapper.readValue(dataJson, Consultant.class);
	        
	        Consultant savedConsultant = consultantServ.save(data, file);

	        return ResponseEntity.status(HttpStatus.SC_OK)
	                .body(new RestAPIResponse("success", "Consultant created successfully", savedConsultant));

	    } catch (Exception ex) {
	        ex.printStackTrace();
	        return ResponseEntity.status(HttpStatus.SC_OK)
	                .body(new RestAPIResponse("fail", "Error: " + ex.getMessage(), null));
	    }
	}
	// ================= UPDATE =================
	@PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<RestAPIResponse> updateConsultant(
	        @PathVariable("id") Long id,
	        @RequestPart("data") String dataJson,  // ← Changed to String
	        @RequestPart(value = "file", required = false) MultipartFile file) {

	    try {
	        // Parse JSON string to Consultant object
	        ObjectMapper objectMapper = new ObjectMapper();
	        Consultant request = objectMapper.readValue(dataJson, Consultant.class);
	        
	        Consultant updatedConsultant = consultantServ.update(id, request, file);

	        return ResponseEntity.ok(
	            new RestAPIResponse("success", "Consultant updated successfully", updatedConsultant));

	    } catch (IllegalArgumentException ex) {
	        ex.printStackTrace();
	        return ResponseEntity.badRequest()
	            .body(new RestAPIResponse("fail", ex.getMessage(), null));

	    } catch (Exception ex) {
	        ex.printStackTrace();  // ← Important: see the actual error
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
	@GetMapping("/getAll")
	public ResponseEntity<RestAPIResponse> getAllConsultants() {

		List<Consultant> consultants = consultantServ.getAll();

		return ResponseEntity.ok(new RestAPIResponse("success", "Fetched successfully", consultants));
	}

	// ================= DEACTIVATE =================
	@DeleteMapping("/{id}")
	public ResponseEntity<RestAPIResponse> deactivateConsultant(@PathVariable("id") Long id) {

		consultantServ.deactivate(id);

		return ResponseEntity.ok(new RestAPIResponse("success", "Deactivated successfully", null));
	}
}
