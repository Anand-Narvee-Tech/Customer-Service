package com.example.entity;

import com.example.DTO.VendorAddress;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "vendor_info")
@Data
public class Vendor {

	@Id
    private Long vendorId; // assuming auto-generated or manually set

    private String vendorName;
    private String vendorAccountNumber; // optional
    private String email;
    private String einNumber;
    private String phoneNumber; // now String to allow formatted numbers    
    @Embedded
    private VendorAddress vendorAddress; }

