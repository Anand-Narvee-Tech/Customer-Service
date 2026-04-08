package com.example.controller;

import java.io.IOException;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.DTO.EmploymentDTO;
import com.example.DTO.EmploymentSortingRequestDTO;
import com.example.common.RestAPIResponse;
import com.example.entity.Employments;
import com.example.service.EmploymentService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.ws.rs.core.HttpHeaders;
import org.springframework.core.io.UrlResource;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

@RestController
@RequestMapping("/employments")
public class EmploymentController {

    @Autowired
    private EmploymentService service;
    
    @Autowired
    private ObjectMapper objectMapper;

    // ================= SAVE =================
    @PostMapping(value = "/employmentsave", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RestAPIResponse> saveEmployment(
            @RequestPart("data") String dataJson,
            @RequestPart(value = "poFile", required = false) MultipartFile poFile) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.findAndRegisterModules();

            Employments data = objectMapper.readValue(dataJson, Employments.class);

            // ✅ Set original filename (optional)
            if (poFile != null && !poFile.isEmpty()) {
                data.setPoUpload(poFile.getOriginalFilename());
            }

            Employments saved = service.saveEmployment(data, poFile);

         // ✅ convert to DTO
         EmploymentDTO response = service.mapToDTO(saved);

         return ResponseEntity.ok(
                 new RestAPIResponse("success", "Employment created successfully", response));
         
        } catch (DataIntegrityViolationException ex) {

            String message = ex.getMostSpecificCause() != null
                    ? ex.getMostSpecificCause().getMessage()
                    : ex.getMessage();

            if (message != null && message.toLowerCase().contains("not-null")) {
                message = "Required field is missing. Please check your input.";
            } else if (message != null && message.toLowerCase().contains("foreign key")) {
                message = "Invalid Consultant/Admin reference.";
            } else {
                message = "Database error: " + message;
            }

            return ResponseEntity.badRequest()
                    .body(new RestAPIResponse("fail", message, null));

        } catch (Exception ex) {
            ex.printStackTrace();

            return ResponseEntity.internalServerError()
                    .body(new RestAPIResponse("fail", ex.getMessage(), null));
        }
    }

 // ================= UPDATE =================
    @PutMapping(value = "employment/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RestAPIResponse> updateEmployment(
            @PathVariable Long id,
            @RequestPart("data") String dataJson,
            @RequestPart(value = "poFile", required = false) MultipartFile poFile) {

        try {
            // Deserialize request JSON into entity
            Employments request = objectMapper.readValue(dataJson, Employments.class);

            // Set PO file if present
            if (poFile != null && !poFile.isEmpty()) {
                request.setPoUpload(poFile.getOriginalFilename());
            }

            // Update employment in DB
            Employments updated = service.updateEmployment(id, request, poFile);

            // ✅ Convert entity to DTO to avoid infinite recursion
            EmploymentDTO response = service.mapToDTO(updated);

            return ResponseEntity.ok(
                    new RestAPIResponse("success", "Employment updated successfully", response));

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(new RestAPIResponse("fail", ex.getMessage(), null));

        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(new RestAPIResponse("fail", "Error: " + ex.getMessage(), null));
        }
    }
    @GetMapping("/pofileid/{id}")
    public ResponseEntity<Resource> getPoFile(@PathVariable Long id) {

        try {
            Resource resource = service.getPoFile(id);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
    
    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return service.getById(id);
    }

   
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return service.deleteEmployment(id);
    }
    
    @GetMapping("/po/file/{fileName:.+}")
    public ResponseEntity<Resource> getPoFile(@PathVariable String fileName) throws IOException {

        Resource resource = service.getPoFile(fileName);

        String contentType = Files.probeContentType(resource.getFile().toPath());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(
                        contentType != null ? contentType : "application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
    
    @PostMapping("/emps/searchAndSorting")
    public ResponseEntity<RestAPIResponse> getEmploymentsByAdmin(
            @RequestBody EmploymentSortingRequestDTO requestDTO) {

        Page<Employments> employments =
        		service.getEmploymentsByAdmin(requestDTO);

        return ResponseEntity.ok(
                new RestAPIResponse(
                        "Success",
                        "Employments fetched successfully",
                        employments.getContent()
                )
        );
    }
}