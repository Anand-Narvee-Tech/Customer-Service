package com.example.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest {

	private Integer pageNo;
	private Integer pageSize;
	private String sortBy;
	private String sortField;
	private String keyword;
}
