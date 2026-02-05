package com.example.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.DTO.ConsultantRequest;
import com.example.DTO.ConsultantResponse;
import com.example.entity.Consultant;

public interface ConsulanatService {

	public Consultant save(Consultant req, MultipartFile file);

	public Consultant getById(Long cid);

	public List<Consultant> getAll();

	public Consultant update(Long id, Consultant req, MultipartFile file);

	public void deactivate(Long cid);
}
