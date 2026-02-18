package com.example.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import com.example.DTO.RestAPIResponse;
import com.example.DTO.VendorAddressDTO;
import com.example.DTO.VendorDTO;
import com.example.entity.Vendor;
import com.example.exception.DuplicateVendorException;
import com.example.repository.VendorRepository;
import com.example.serviceImpl.VendorServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/vendor")
public class VendorController {

	@Autowired
	private VendorServiceImpl vendorServiceImpl;

	@Autowired
	private VendorRepository vendorRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<RestAPIResponse> saveVendor(@RequestPart("data") String vendorJson,
			@RequestPart(value = "msaFile", required = false) MultipartFile msaFile) {

		try {
			// Convert JSON string → Vendor
			Vendor vendor = objectMapper.readValue(vendorJson, Vendor.class);

			Vendor savedVendor = vendorServiceImpl.createVendor(vendor, msaFile);

			return ResponseEntity.ok(new RestAPIResponse("success", "Vendor registered successfully", savedVendor));

		} catch (DuplicateVendorException ex) {
			return ResponseEntity.badRequest().body(new RestAPIResponse("fail", ex.getMessage(), null));

		} catch (Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.internalServerError()
					.body(new RestAPIResponse("fail", "Error: " + ex.getMessage(), null));
		}
	}

	@PutMapping(value = "/{vendorId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<RestAPIResponse> updateVendor(@PathVariable("vendorId") Long vendorId,
			@RequestPart("vendor") String vendorJson,
			@RequestPart(value = "msaFile", required = false) MultipartFile msaFile) {

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			Vendor vendor = objectMapper.readValue(vendorJson, Vendor.class);

			Vendor updatedVendor = vendorServiceImpl.updateVendor(vendorId, vendor, msaFile);

			return ResponseEntity.ok(new RestAPIResponse("success", "Vendor Data Updated Successfully", updatedVendor));

		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new RestAPIResponse("error", e.getMessage()));
		}
	}

	@GetMapping("/exists")
	public ResponseEntity<Map<String, Object>> checkVendorFieldExists(@RequestParam("field") String field,
			@RequestParam("value") String value) {

		boolean exists = vendorServiceImpl.checkFieldExists(field, value);

		Map<String, Object> response = new HashMap<>();
		response.put("field", field);
		response.put("value", value);
		response.put("exists", exists);
		response.put("message", exists ? field + "already exists" : field + "is available");

		return ResponseEntity.ok(response);
	}

	@GetMapping("/{vendorId:\\d+}")
	public ResponseEntity<RestAPIResponse> getById(@PathVariable Long vendorId) {
		try {
			return new ResponseEntity<>(new RestAPIResponse("Success", "Getting the Vendor Data successfully By ID",
					vendorServiceImpl.getById(vendorId)), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new RestAPIResponse("Error", "Vendor is not found or not exist", null),
					HttpStatus.OK);
		}
	}

	@GetMapping("/domain/{domain}")
	public ResponseEntity<RestAPIResponse> getByCompanyDomain(@PathVariable("domain") String domain) {

		try {
			List<Vendor> vendors = vendorServiceImpl.getVendorByDomain(domain);
			return new ResponseEntity<>(new RestAPIResponse("Success", "Vendor found for domain", vendors),
					HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new RestAPIResponse("error", e.getMessage()), HttpStatus.OK);
		}

	}

	@GetMapping("/by-name")
	public ResponseEntity<List<VendorDTO>> searchVendors(@RequestParam("name") String name) {
		List<Vendor> vendors = vendorServiceImpl.searchByName(name);

		List<VendorDTO> response = vendors.stream().map(vendor -> {
			VendorAddressDTO addr = new VendorAddressDTO(vendor.getVendorAddress().getStreet(),
					vendor.getVendorAddress().getSuite(), vendor.getVendorAddress().getCity(),
					vendor.getVendorAddress().getState(), vendor.getVendorAddress().getZipCode());

			return new VendorDTO(vendor.getVendorId(), vendor.getVendorName(), vendor.getEmail(),
					vendor.getPhoneNumber(), vendor.getMsaAgreement(), vendor.getAddress(), vendor.getWebsite(), addr);
		}).collect(Collectors.toList());

		return ResponseEntity.ok(response);
	}

	@GetMapping("/getall")
	public ResponseEntity<RestAPIResponse> getAllVendors() {
		try {
			return new ResponseEntity<>(
					new RestAPIResponse("success", "All Vendors Data Successfully", vendorServiceImpl.getAll()),
					HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new RestAPIResponse("error", "Getting Data failed"), HttpStatus.OK);
		}
	}

	@GetMapping("/exists/vendor-name/{vendorName}")
	public ResponseEntity<RestAPIResponse> checkVendorName(@PathVariable("vendorName") String vendorName,
			@RequestParam(value = "vendorId", required = false) Long vendorId) {

		boolean exists = vendorServiceImpl.isVendorNameDuplicate(vendorName, vendorId);

		return ResponseEntity.ok(new RestAPIResponse("success",
				exists ? "Vendor name already exists" : "Vendor name is available", exists));
	}

	@GetMapping("/exists/email/{email}")
	public ResponseEntity<RestAPIResponse> checkEmail(@PathVariable("email") String email,
			@RequestParam(value = "vendorId", required = false) Long vendorId) {

		boolean exists = vendorServiceImpl.isEmailDuplicate(email, vendorId);

		return ResponseEntity
				.ok(new RestAPIResponse("success", exists ? "Email already exists" : "Email is available", exists));
	}

	@GetMapping("/exists/ein-number/{einNumber}")
	public ResponseEntity<RestAPIResponse> checkEinNumber(@PathVariable("einNumber") String einNumber,
			@RequestParam(value = "vendorId", required = false) Long vendorId) {

		boolean exists = vendorServiceImpl.isEinNumberDuplicate(einNumber, vendorId);

		return ResponseEntity.ok(new RestAPIResponse("success",
				exists ? "EIN Number already exists" : "EIN Number is available", exists));
	}

	@GetMapping("/exists/phone-number/{phoneNumber}")
	public ResponseEntity<RestAPIResponse> checkPhoneNumber(@PathVariable("phoneNumber") String phoneNumber,
			@RequestParam(value = "vendorId", required = false) Long vendorId) {

		boolean exists = vendorServiceImpl.isPhoneNumberDuplicate(phoneNumber, vendorId);

		return ResponseEntity.ok(new RestAPIResponse("success",
				exists ? "Phone number already exists" : "Phone number is available", exists));
	}

	@GetMapping("/searchAndSort")
	public ResponseEntity<RestAPIResponse> searchAndSortVendors(
			@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size,
			@RequestParam(value = "sortField", defaultValue = "vendorId") String sortField,
			@RequestParam(value = "sortDir", defaultValue = "asc") String sortDir) {

		try {
			Page<Vendor> result = vendorServiceImpl.getVendors(page, size, sortField, sortDir, keyword);

			return new ResponseEntity<>(new RestAPIResponse("Success",
					"Vendors Retrieved Successfully (Search + Sort + Pagination)", result), HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(
					new RestAPIResponse("Error", "Failed to search and sort vendors: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/count")
	public ResponseEntity<RestAPIResponse> getVendorCount() {

		Long vendorCount = vendorServiceImpl.fetchVendorCount();

		return new ResponseEntity<>(new RestAPIResponse("success", "Vendor count fetched successfully", vendorCount),
				HttpStatus.OK);
	}

	@GetMapping("/recent")
	public ResponseEntity<RestAPIResponse> getRecentVendors() {
		List<String> vendors = vendorServiceImpl.getVendorsAddedLast24Hours();

		String message = vendors.isEmpty() ? "No vendors added in the last 24 hours"
				: vendors.size() + "vendors added in the last 24 hours";
		return ResponseEntity.ok(new RestAPIResponse("success", message, vendors));
	}

	@GetMapping("/count-per-month")
	public ResponseEntity<RestAPIResponse> getVendorCountPerMonth() {
		Map<String, Object> counts = vendorServiceImpl.fetchVendorCountPerMonth(LocalDate.now().getYear());
		return ResponseEntity.ok(new RestAPIResponse("success", "Vendor count per month fetched", counts));
	}

	@DeleteMapping("/{vendorId}")
	public ResponseEntity<RestAPIResponse> deleteVendor(@PathVariable("vendorId") Long vendorId) {

		vendorServiceImpl.deleteVendor(vendorId);

		return ResponseEntity.ok(new RestAPIResponse("success", "Vendor deleted successfully", null));
	}

}
