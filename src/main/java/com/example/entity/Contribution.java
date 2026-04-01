package com.example.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "contributions")
@Data
public class Contribution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contribution_id")
    private long contributionId;

    @Column(name = "other_contribution") 
    private String otherContribution;

    @Column(name = "contribution_type") 
    private String contributionType;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "amount_type")
    private String amountType;

    @ManyToOne
    @JoinColumn(name = "consultant_id")
    @JsonBackReference
    private Consultant consultant;
}