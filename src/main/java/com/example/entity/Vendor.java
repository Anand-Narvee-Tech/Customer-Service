
package com.example.entity;

import java.time.LocalDateTime;

import com.example.DTO.VendorAddress;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

	@Column(name = "ein_number", nullable = false)
	private String einNumber;

	@Column(name = "phone_number", nullable = false)
	private String phoneNumber;

	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Embedded
	private VendorAddress vendorAddress; // ✅ embeddable
    
	@Column(name = "msa")
	private String msaAgreement;
	
	@Column(name = "address")
	private String address;
	
	@Column(name = "website")
	private String website;
	
	@PrePersist
	public void prePersist() {
		this.createdAt = LocalDateTime.now();
	}
}
