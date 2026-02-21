package com.example.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import com.example.DTO.ConsultantRequest;
import com.example.DTO.SearchRequest;
import com.example.entity.Consultant;

public interface ConsulanatService {

	public Consultant save(Consultant req, MultipartFile file);

	public Consultant getById(Long cid);

	//public Page<Consultant> getAllOrSearch(SearchRequest request);

	public Consultant update(Long id, Consultant req, MultipartFile file);

	public void deactivate(Long cid);

	List<Consultant> getConsultantsByVendorId(Long vendorId);

	//public Page<Consultant> getAllOrSearch(SearchRequest request, Long vendorId);

	
	Page<Consultant> getConsultants(String keyword, Long adminId, PageRequest pageable);

}
