package com.example.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.example.DTO.NetTerm;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "consultant")
public class Consultant {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Long id;

	@Column(nullable = false, unique = true, updatable = false)
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private String cid;

	private String firstName;
	private String lastName;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(name = "mobile_number")
	private String mobileNumber;
	
//30_03_26	
	
	@Column(name = "date_of_birth")
	private LocalDate dateOfBirth;

	@Column(name = "gender")
	private String gender;

	@Column(name = "marital_status")
	private String maritalStatus;

	@Column(name = "number_of_children")
	private Long numberOfChildren;

	@Column(name = "security_number")
	private String securityNumber;

	@Column(name = "personal_email")
	private String personalEmail;

	@Column(name = "hire_date")
	private LocalDate hireDate;
	

	@Column(name = "client_hire_date")
	private LocalDate clienthireDate;

	@Column(name = "work_location")
	private String workLocation;
	
	@Column(name = "alternate_Number")
	private String alternateNumber;

	@Column(name = "visatype")
	private String visaType;
	
	@Column(name = "visastartdate")
	private LocalDate visaStartDate;
	
	@Column(name = "visaenddate")
	private LocalDate visaEndDate;
	
	@Column(name = "w4form")
	private String w4Form;
	
	@Column(name = "voidcheque")
	private String voidCheque;
	
	@Column(name = "payment_frequency")
	private String paymentFrequency;
	
	
	@Column(name = "project_end_date")
	private LocalDate projectEndDate;
	
	
	@Column(name = "project_start_date")
	private LocalDate projectStartDate;
	

	
	// ✅ FIX HERE
	@OneToMany(mappedBy = "consultant", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private List<ConsultantBankAccount> bankAccounts;
	
	
	@OneToMany(mappedBy = "consultant", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private List<Contribution> contributions;
	
//30_03_26	
	

	/* private String mobileNumber; */
	private BigDecimal billRate;
	private String documentPath;
	private String status;

	@Column(name = "adminId")
	private Long adminId;

	@ManyToOne
	@JoinColumn(name = "vendor_id", nullable = false)
	private Vendor vendor;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private LocalDateTime createdAt;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private LocalDateTime updatedAt;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Long createdBy;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Long updatedBy;

	@Enumerated(EnumType.STRING)
	@Column(name = "net_term")
	private NetTerm netTerm;

	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	private String client;

	@Column(name = "invoice_mails")
	private String invoiceMail;

	// consultant address -vasim
	private String address;
	private String suite;
	private String city;
	private String state;
	private String country;
	private String pincode;

	
	
	@PrePersist
	public void prePersist() {
		if (this.cid == null) {
			this.cid = "CONS-" + UUID.randomUUID();
		}
		this.createdAt = LocalDateTime.now();
	}

	@PreUpdate
	public void preUpdate() {
		this.updatedAt = LocalDateTime.now();
	}

	
}