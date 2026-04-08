package com.example.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.example.DTO.NetTerm;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Entity
@Data
@Table(name = "employments")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Employments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long empId;

    private String workLocation;

    // ✅ Dates
    private LocalDate clientHireDate;
    private LocalDate projectStartDate;
    private LocalDate projectEndDate;

    private String client;

    // ✅ Multiple emails
    @ElementCollection
    @CollectionTable(
        name = "employment_invoice_mails",
        joinColumns = @JoinColumn(name = "employment_id")
    )
    @Column(name = "email")
    private List<String> invoiceMail;

    // ✅ Enum
    @Enumerated(EnumType.STRING)
    @Column(name = "net_term")
    private NetTerm netTerm;

    private String paymentFrequency;

    private BigDecimal billRate;

    private Long adminId;

    // ✅ File name
    private String poUpload;

    // ================= RELATIONSHIPS =================

    // Vendor (optional)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Vendor vendor;

    // Consultant (mandatory)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consultant_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "employments", "bankAccounts", "contributions"})
    private Consultant consultant;
    
}