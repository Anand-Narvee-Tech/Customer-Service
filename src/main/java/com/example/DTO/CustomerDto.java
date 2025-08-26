package com.example.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerDto {
	private long customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String moblieNumber;
    private String billingAddress;
    private String shippingAddress;
    private String status;
}
