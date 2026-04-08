package com.example.DTO;

import java.util.List;
import lombok.Data;

@Data
public class ConsultantRequestDTO {

    private ConsultantDTO consultant;  // ✅ FIXED
    private List<ConsultantBankAccountDTO> bankAccounts;
    private List<ContributionDTO> contributions;
    private List<EmploymentDTO> employments;
}