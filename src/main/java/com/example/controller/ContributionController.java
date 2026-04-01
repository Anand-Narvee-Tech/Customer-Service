package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.DTO.RestAPIResponse;
import com.example.entity.Contribution;
import com.example.exception.ResourceNotFoundException;
import com.example.service.ContributionService;

@RestController
@RequestMapping("/contributions")
public class ContributionController {

    @Autowired
    private ContributionService contributionService;

    // CREATE
    @PostMapping("/savecontributions")
    public ResponseEntity<RestAPIResponse> create(@RequestBody Contribution contribution) {
        try {
            Contribution saved = contributionService.create(contribution);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new RestAPIResponse("Success", "Contribution created successfully", saved)
            );

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new RestAPIResponse("Fail", "Failed to create contribution: " + e.getMessage(), null)
            );
        }
    }

    // GET ALL
    @GetMapping("/getAllcontributions")
    public ResponseEntity<RestAPIResponse> getAll() {

        List<Contribution> list = contributionService.getAll(); // ✅ FIX

        if (list.isEmpty()) {
            return ResponseEntity.ok(
                    new RestAPIResponse("Success", "No contributions found", list)
            );
        }

        return ResponseEntity.ok(
                new RestAPIResponse("Success", "Contributions fetched successfully", list, list.size())
        );
    }

    // GET BY ID
    @GetMapping("/getById/{id}")
    public ResponseEntity<RestAPIResponse> getById(@PathVariable long id) {
        try {
            Contribution contribution = contributionService.findById(id);

            return ResponseEntity.ok(
                    new RestAPIResponse("Success", "Contribution fetched successfully", contribution)
            );

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new RestAPIResponse("Fail", e.getMessage(), null, 0)
            );
        }
    }

    // UPDATE
    @PutMapping("/updatedbyid/{id}")
    public ResponseEntity<RestAPIResponse> update(@PathVariable long id,
                                                  @RequestBody Contribution contribution) {
        try {
            Contribution updated = contributionService.update(id, contribution);

            return ResponseEntity.ok(
                    new RestAPIResponse("Success", "Contribution updated successfully", updated)
            );

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new RestAPIResponse("Fail", e.getMessage(), null, 0)
            );
        }
    }

    // DELETE
    @DeleteMapping("/deletebyid/{id}")
    public ResponseEntity<RestAPIResponse> delete(@PathVariable long id) {
        try {
            contributionService.delete(id);

            return ResponseEntity.ok(
                    new RestAPIResponse("Success", "Contribution deleted successfully", null)
            );

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new RestAPIResponse("Fail", e.getMessage(), null, 0)
            );
        }
    }
}