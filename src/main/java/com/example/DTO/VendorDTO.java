package com.example.DTO;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

public class VendorDTO {

	private Long vendorId;
	private String vendorName;
	private String email;
	private String phoneNumber;
	private String msaAgreement;
	private String address;
	private String website;
	private String attentionTo;
	private String additionDoc;
	private String vendorType;
	private String einNumber;
	private LocalDateTime createdAt;
	private String gstin;
	private Long adminId;
	private Double discount;
	private VendorDTO vendorDTO;
	private VendorAddressDTO vendorAddressDTO;
	
	public VendorDTO(Long vendorId, String vendorName, String email, String phoneNumber, String msaAgreement,
			String address, String website, String attentionTo, String additionDoc, String vendorType, String einNumber,
			LocalDateTime createdAt, String gstin, Long adminId, Double discount, VendorAddressDTO vendorAddressDTO) {

		this.vendorId = vendorId;
		this.vendorName = vendorName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.msaAgreement = msaAgreement;
		this.address = address;
		this.website = website;
		this.attentionTo = attentionTo;
		this.additionDoc = additionDoc;
		this.vendorType = vendorType;
		this.einNumber = einNumber;
		this.createdAt = createdAt;
		this.gstin = gstin;
		this.adminId = adminId;
		this.discount = discount;
		this.vendorAddressDTO = vendorAddressDTO;
	}
	public VendorDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
}
