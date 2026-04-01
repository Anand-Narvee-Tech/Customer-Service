package com.example.DTO;

import lombok.Data;

@Data
public class ContributionDTO {
	
    private String contributionId;
    private String contribution;
    private String otherContribution;
    private String contributionType;
    private Double amount;
    private Double amountType;

}
