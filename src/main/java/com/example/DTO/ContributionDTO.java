package com.example.DTO;

import lombok.Data;

@Data
public class ContributionDTO {
	
    private long contributionId;
    private String otherContribution;
    private String contributionType;
    private Double amount;
    private String amountType;

}
