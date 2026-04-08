package com.example.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ConsultantDTO {
	private Long id;	
	private String cid;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String gender;
    private String maritalStatus;
    private Long numberOfChildren;
    private String securityNumber;
    private String personalEmail;
    private String email;
    private String mobileNumber;
    private String alternateNumber;
    private String address;
    private String suite;
    private String city;
    private String state;
    private String country;
    private String pincode;
    private String hireDate;
    private String visaType;
    private String visaStartDate;
    private String visaEndDate;
    private String w4Form;
    private String voidCheque;

    
}