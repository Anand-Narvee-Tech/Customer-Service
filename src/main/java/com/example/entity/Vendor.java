//package com.example.entity;
//
//import com.example.DTO.VendorAddress;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@AllArgsConstructor
//@NoArgsConstructor
//@Entity
//@Table(name = "vendor_info")
//@Data
//public class Vendor {
//
//	@Id
//    private Long vendorId; // assuming auto-generated or manually set
//
//    private String vendorName;
//    private String email;
//    private String einNumber;
//    private String phoneNumber; // now String to allow formatted numbers    
//    @Embedded
//    private VendorAddress vendorAddress; }
//


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
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
	    name = "vendor_info",
	    uniqueConstraints = {
	        @UniqueConstraint(columnNames = "vendor_name"),
	        @UniqueConstraint(columnNames = "email"),
	        @UniqueConstraint(columnNames = "ein_number"),
	        @UniqueConstraint(columnNames = "phone_number")
	    }
	)
@Data
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
    
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
