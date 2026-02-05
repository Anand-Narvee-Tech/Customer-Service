package com.example.DTO;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultantRequest {
	private String cid;
	private String firstName;
	private String lastName;
	private String email;
	private String mobileNumber;
	private Long vendorId;
	private BigDecimal billRate;
	private String status; // ACTIVE / INACTIce
}
