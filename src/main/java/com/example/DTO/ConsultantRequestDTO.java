package com.example.DTO;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ConsultantRequestDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String mobileNumber;
    private BigDecimal billRate;
    private Long vendorId;
}
