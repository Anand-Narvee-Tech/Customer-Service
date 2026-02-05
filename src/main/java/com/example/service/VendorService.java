package com.example.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.example.entity.Vendor;

public interface VendorService {

	public Vendor createVendor(Vendor vendor, MultipartFile msaFile);

	public boolean checkFieldExists(String field, String value);

	public Vendor getById(Long vendorId);

	public List<Vendor> searchByName(String name);

	public List<Vendor> getAll();

	public Vendor updateVendor(Long vendorId, Vendor vendor);

	public void deleteVendor(Long vendorId);

	public Optional<Vendor> getVendorByName(String vendorName);

	public List<Vendor> searchVendorsByName(String keyword);

	public Page<Vendor> getVendors(int page, int size, String sortField, String sortDir, String search);

	public Long fetchVendorCount();

	boolean isVendorNameDuplicate(String vendorName, Long vendorId);

	boolean isEmailDuplicate(String email, Long vendorId);

	boolean isEinNumberDuplicate(String einNumber, Long vendorId);

	boolean isPhoneNumberDuplicate(String phoneNumber, Long vendorId);

	public List<String> getVendorsAddedLast24Hours();

	public Map<String, Object> fetchVendorCountPerMonth(int year);
}
