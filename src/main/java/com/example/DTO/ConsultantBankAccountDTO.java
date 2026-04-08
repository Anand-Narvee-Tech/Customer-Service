package com.example.DTO;

import lombok.Data;

@Data
public class ConsultantBankAccountDTO {
	private Long id;
    private String accountHolderName;
    private String bankName;
    private String accountNumber;
    private String routingNumber;
    private String accountType;
    private String  ifscCode;
}
