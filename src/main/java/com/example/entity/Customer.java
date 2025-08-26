package com.example.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customer-info")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Customer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long customerId;
	private String firstName;
	private String lastName;
	private String email;
	private String moblieNumber;
	
	 private String billingAddress;
	    private String shippingAddress;

	    private String taxId;
	    private String paymentTerms;   
	    private String paymentMethod;

	    private String status; 


}
