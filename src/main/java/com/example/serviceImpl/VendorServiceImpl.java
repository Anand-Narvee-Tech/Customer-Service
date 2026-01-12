package com.example.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.DTO.VendorAddress;
import com.example.entity.Vendor;
import com.example.exception.DuplicateVendorException;
import com.example.repository.VendorRepository;
import com.example.service.VendorService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

@Service
public class VendorServiceImpl implements VendorService {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private VendorRepository vendorRepository;

    // ---------------- CREATE VENDOR ----------------
    @Override
    public Vendor createVendor(Vendor vendor) {

        List<String> duplicateFields = new ArrayList<>();

        if (vendorRepository.existsByVendorNameIgnoreCase(vendor.getVendorName()))
            duplicateFields.add("vendorName");

        if (vendorRepository.existsByEmailIgnoreCase(vendor.getEmail()))
            duplicateFields.add("email");

        if (vendorRepository.existsByEinNumber(vendor.getEinNumber()))
            duplicateFields.add("einNumber");

        if (vendorRepository.existsByPhoneNumber(vendor.getPhoneNumber()))
            duplicateFields.add("phoneNumber");

        if (!duplicateFields.isEmpty()) {
            throw new DuplicateVendorException(
                "Duplicate vendor found in fields: " + String.join(", ", duplicateFields)
            );
        }

        return vendorRepository.save(vendor);
    }

    
    // ---------------- DUPLICATES CHECKING ----------------
    @Override
    public boolean checkFieldExists(String field, String value) {

        return switch (field) {
            case "vendorName" ->
                vendorRepository.existsByVendorNameIgnoreCase(value);

            case "email" ->
                vendorRepository.existsByEmailIgnoreCase(value);

            case "einNumber" ->
                vendorRepository.existsByEinNumber(value);

            case "phoneNumber" ->
                vendorRepository.existsByPhoneNumber(value);

            default ->
                throw new IllegalArgumentException("Invalid field: " + field);
        };
    }


    // ---------------- GET ALL VENDORS ----------------
    @Override
    public List<Vendor> getAll() {
        return vendorRepository.findAll();
    }

    // ---------------- SEARCH BY NAME ----------------
    @Override
    public List<Vendor> searchByName(String name) {
        String searchText = name.trim().toLowerCase();

        List<Vendor> vendors = vendorRepository.findByVendorNameContainingIgnoreCase(searchText);

        return vendors.stream()
            .sorted((a, b) -> {
                String aName = a.getVendorName().toLowerCase();
                String bName = b.getVendorName().toLowerCase();

                if (aName.equals(searchText) && !bName.equals(searchText)) return -1;
                if (!aName.equals(searchText) && bName.equals(searchText)) return 1;
                if (aName.startsWith(searchText) && !bName.startsWith(searchText)) return -1;
                if (!aName.startsWith(searchText) && bName.startsWith(searchText)) return 1;
                if (aName.contains(searchText) && !bName.contains(searchText)) return -1;
                if (!aName.contains(searchText) && bName.contains(searchText)) return 1;

                return aName.compareTo(bName);
            })
            .toList();
    }

    // ---------------- UPDATE VENDOR ----------------
    @Override
    public Vendor updateVendor(Long vendorId, Vendor vendor) {
        return vendorRepository.findById(vendorId).map(existing -> {
            existing.setVendorName(vendor.getVendorName());
            existing.setPhoneNumber(vendor.getPhoneNumber());
            existing.setEmail(vendor.getEmail());
            existing.setEinNumber(vendor.getEinNumber());

            if (vendor.getVendorAddress() != null) {
                existing.setVendorAddress(vendor.getVendorAddress());
            }

            return vendorRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Vendor not found with id " + vendorId));
    }

    // ---------------- DELETE VENDOR ----------------
    @Override
    public void deleteVendor(Long vendorId) {
        vendorRepository.deleteById(vendorId);
    }

    // ---------------- GET VENDORS WITH PAGINATION, SORTING & SEARCH ----------------
    @Override
    public Page<Vendor> getVendors(int page, int size, String sortField, String sortDir, String search) {

        // 🔹 Resolve embedded field names
        String resolvedSortField = sortField;

        if ("city".equalsIgnoreCase(sortField)) {
            resolvedSortField = "vendorAddress.city";
        } else if ("state".equalsIgnoreCase(sortField)) {
            resolvedSortField = "vendorAddress.state";
        }

        final String finalSortField = resolvedSortField;
        final String finalSortDir = sortDir;

        // 🔹 Pageable sorting (ONLY for root fields)
        Sort sort = Sort.unsorted();

        if (StringUtils.hasText(finalSortField) && !finalSortField.contains(".")) {
            sort = "desc".equalsIgnoreCase(finalSortDir)
                    ? Sort.by(finalSortField).descending()
                    : Sort.by(finalSortField).ascending();
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<Vendor> spec = (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            //  Search
            if (StringUtils.hasText(search)) {

                String pattern = "%" + search.toLowerCase() + "%";

                Join<Vendor, VendorAddress> address =
                        root.join("vendorAddress", JoinType.LEFT);

                predicates.add(cb.like(cb.lower(root.get("vendorName")), pattern));
                predicates.add(cb.like(cb.lower(root.get("email")), pattern));
                predicates.add(cb.like(cb.lower(root.get("einNumber")), pattern));
                predicates.add(cb.like(cb.lower(root.get("phoneNumber")), pattern));
                predicates.add(cb.like(cb.lower(address.get("city")), pattern));
                predicates.add(cb.like(cb.lower(address.get("state")), pattern));
            }

            //  Embedded field sorting (handled here, NOT pageable)
            if (StringUtils.hasText(finalSortField) && finalSortField.contains(".")) {
                String[] parts = finalSortField.split("\\.");

                Path<?> sortPath = root.get(parts[0]).get(parts[1]);

                query.orderBy(
                        "desc".equalsIgnoreCase(finalSortDir)
                                ? cb.desc(sortPath)
                                : cb.asc(sortPath)
                );
            }

            return predicates.isEmpty()
                    ? cb.conjunction()
                    : cb.or(predicates.toArray(new Predicate[0]));
        };

        return vendorRepository.findAll(spec, pageable);
    }



    // ---------------- GET VENDOR BY ID ----------------
    @Override
    public Vendor getById(Long vendorId) {
        return vendorRepository.findById(vendorId)
                .orElseThrow(() -> new RuntimeException("Vendor not found with id " + vendorId));
    }

    // ---------------- GET VENDOR BY NAME ----------------
    @Override
    public Optional<Vendor> getVendorByName(String vendorName) {
        return vendorRepository.findByVendorName(vendorName);
    }

    // ---------------- SEARCH VENDORS BY NAME ----------------
    @Override
    public List<Vendor> searchVendorsByName(String keyword) {
        return vendorRepository.findByVendorNameContainingIgnoreCase(keyword);
    }

    // ---------------- GET VENDORS BY EMAIL DOMAIN ----------------
    public List<Vendor> getVendorByDomain(String domain) {
        List<Vendor> vendors = vendorRepository.findByEmailEndingWith(domain);
        if (vendors.isEmpty()) throw new RuntimeException("No vendors found for domain: " + domain);
        return vendors;
    }
    
    @Override
    public boolean isVendorNameDuplicate(String vendorName, Long vendorId) {
        if (vendorName == null || vendorName.isBlank()) {
            return false;
        }
        if (vendorId != null) {
            return vendorRepository
                    .existsByVendorNameIgnoreCaseAndVendorIdNot(vendorName, vendorId);
        }
        return vendorRepository.existsByVendorNameIgnoreCase(vendorName);
    }
    
    @Override
    public boolean isEmailDuplicate(String email, Long vendorId) {
        if (email == null || email.isBlank()) {
            return false;
        }
        if (vendorId != null) {
            return vendorRepository
                    .existsByEmailIgnoreCaseAndVendorIdNot(email, vendorId);
        }
        return vendorRepository.existsByEmailIgnoreCase(email);
    }
    
    @Override
    public boolean isEinNumberDuplicate(String einNumber, Long vendorId) {
        if (einNumber == null || einNumber.isBlank()) {
            return false;
        }
        if (vendorId != null) {
            return vendorRepository
                    .existsByEinNumberIgnoreCaseAndVendorIdNot(einNumber, vendorId);
        }
        return vendorRepository.existsByEinNumberIgnoreCase(einNumber);
    }
    
    @Override
    public boolean isPhoneNumberDuplicate(String phoneNumber, Long vendorId) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            return false;
        }
        if (vendorId != null) {
            return vendorRepository
                    .existsByPhoneNumberAndVendorIdNot(phoneNumber, vendorId);
        }
        return vendorRepository.existsByPhoneNumber(phoneNumber);
    }
}
