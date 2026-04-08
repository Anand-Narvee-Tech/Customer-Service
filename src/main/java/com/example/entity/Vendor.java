
package com.example.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.example.DTO.VendorAddress;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "vendor_info", uniqueConstraints = { @UniqueConstraint(columnNames = "vendor_name"),
		@UniqueConstraint(columnNames = "email"), @UniqueConstraint(columnNames = "ein_number"),
		@UniqueConstraint(columnNames = "phone_number") })

@Setter
@Getter
public class Vendor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long vendorId;

	@Column(name = "vendor_name", nullable = false)
	private String vendorName;

	@Column(nullable = false)
	private String email;

	@Column(name = "ein_number")
	private String einNumber;

	@Column(name = "phone_number", nullable = false)
	private String phoneNumber;

	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Embedded
	private VendorAddress vendorAddress;

	@Column(name = "gstin")
	private String gstin;

	@Column(name = "msa")
	private String msaAgreement;

	@Column(name = "address")
	private String address;

	@Column(name = "website")
	private String website;

	@Column(name = "adminId")
	private Long adminId;

	@Column(name = "attention_to")
	private String attentionTo;

	@Column(name = "addition_doc")
	private String additionDoc;
	
	//Bhargav 20-03-26
	@Column(name = "discount")
	private Double discount = 0.0;
	
	@Column(name = "vendorType")
	private String vendorType;
	
	
	@ManyToMany(mappedBy = "vendors")
	@JsonIgnore
	private List<Consultant> consultants;
	
	//Bhargav 23-03-26


	
	@PrePersist
	public void prePersist() {
		this.createdAt = LocalDateTime.now();
	}
}
