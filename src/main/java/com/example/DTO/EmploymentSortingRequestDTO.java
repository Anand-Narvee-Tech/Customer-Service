package com.example.DTO;
import lombok.Data;

@Data
public class EmploymentSortingRequestDTO {

    private String search;
    private String sortField;
    private String sortOrder;
    private Integer pageNumber;
    private Integer pageSize;
    private Long adminId;
}
