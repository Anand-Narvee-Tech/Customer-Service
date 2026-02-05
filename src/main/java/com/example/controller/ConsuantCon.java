package com.example.controller;

import java.util.List;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.DTO.ConsultantRequest;
import com.example.DTO.RestAPIResponse;
import com.example.entity.Consultant;
import com.example.service.ConsulanatService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.ws.rs.core.MediaType;

@RestController
@RequestMapping("/con")
public class ConsuantCon {

	@Autowired
	private ConsulanatService consultantServ;

	@PostMapping(value = "/saveConsultant", consumes = MediaType.MULTIPART_FORM_DATA)
	public ResponseEntity<RestAPIResponse> createConsultant(@RequestPart("data") String consultantJson,
			@RequestPart(value = "file", required = false) MultipartFile file) {

		try {
			ObjectMapper mapper = new ObjectMapper();
			Consultant request = mapper.readValue(consultantJson, Consultant.class);

			Consultant savedConsultant = consultantServ.save(request, file);

			return ResponseEntity.status(HttpStatus.SC_OK)
					.body(new RestAPIResponse("success", "Consultant created successfully", savedConsultant));

		} catch (IllegalArgumentException ex) {

			return ResponseEntity.status(HttpStatus.SC_OK).body(new RestAPIResponse("fail", ex.getMessage(), null));

		} catch (Exception ex) {

			return ResponseEntity.status(HttpStatus.SC_OK)
					.body(new RestAPIResponse("fail", "Something went wrong while creating consultant", null));
		}
	}

	@PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA)
	public ResponseEntity<RestAPIResponse> updateConsultant(@PathVariable("id") Long id,
			@RequestPart("data") String consultantJson,
			@RequestPart(value = "file", required = false) MultipartFile file) {

		try {
			ObjectMapper mapper = new ObjectMapper();
			Consultant request = mapper.readValue(consultantJson, Consultant.class);

			Consultant updatedConsultant = consultantServ.update(id, request, file);

			return ResponseEntity
					.ok(new RestAPIResponse("success", "Consultant updated successfully", updatedConsultant));

		} catch (IllegalArgumentException ex) {
			return ResponseEntity.badRequest().body(new RestAPIResponse("fail", ex.getMessage(), null));

		} catch (Exception ex) {
			return ResponseEntity.internalServerError()
					.body(new RestAPIResponse("fail", "Something went wrong while updating consultant", null));
		}
	}

	@GetMapping("/getByID/{id}")
	public ResponseEntity<RestAPIResponse> getConsultantById(@PathVariable("id") Long id) {

		Consultant consultant = consultantServ.getById(id);

		RestAPIResponse response = new RestAPIResponse("SUCCESS", "Fetched successfully", consultant);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/getAll")
	public ResponseEntity<RestAPIResponse> getAllConsultants() {

		List<Consultant> consultants = consultantServ.getAll();

		RestAPIResponse response = new RestAPIResponse("SUCCESS", "Fetched successfully", consultants);

		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<RestAPIResponse> deactivateConsultant(@PathVariable("id") Long id) {

		consultantServ.deactivate(id);

		RestAPIResponse response = new RestAPIResponse("SUCCESS", "Deactivated successfully");

		return ResponseEntity.ok(response);
	}

}
