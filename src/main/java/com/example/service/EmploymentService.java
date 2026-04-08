package com.example.service;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.example.DTO.EmploymentDTO;
import com.example.entity.Employments;

public interface EmploymentService {

	public  Employments saveEmployment(Employments emp, MultipartFile poFile);

	public  Employments updateEmployment(Long id, Employments emp, MultipartFile poFile);
	
	public  ResponseEntity<List<Employments>> getAll();

	public  ResponseEntity<Employments> getById(Long id);


	public ResponseEntity<String> deleteEmployment(Long id);

	public Resource getPoFile(Long id);

	public Resource getPoFile(String fileName);
	
	public EmploymentDTO mapToDTO(Employments emp);
}
