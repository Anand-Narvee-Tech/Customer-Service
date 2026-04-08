package com.example.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class EmploymentDTO {

	
	private long empId;
    private String workLocation;
    private LocalDate clientHireDate;
    private LocalDate projectStartDate;
    private LocalDate projectEndDate;
    private String client;
    private List<String> invoiceMail;
    private String netTerm; // ✅ ADD THIS
    private String paymentFrequency;
    private BigDecimal billRate;
    private String poUpload;
    private ConsultantDTO consultant;
    private VendorDTO vendor;
    private Long consultantId;
    private String consultantName;
	
}

