package com.example.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Consultant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Business ID (auto-generated, NOT NULL, immutable)
    @Column(nullable = false, unique = true, updatable = false)
    private String cid;

    private String firstName;
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    private String mobileNumber;
    private BigDecimal billRate;
    private String documentPath;
    private String status;

    @ManyToOne
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long createdBy;
    private Long updatedBy;

    // ------------------------
    // Auto lifecycle handlers
    // ------------------------

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
