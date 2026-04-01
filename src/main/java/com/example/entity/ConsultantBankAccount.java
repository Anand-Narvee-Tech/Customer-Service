package com.example.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "consultant_bank_account")
public class ConsultantBankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountHolderName;
    private String bankName;
    private String accountNumber;
    private String routingNumber;
    private String  ifscCode;

    @ManyToOne
    @JoinColumn(name = "consultant_id")
    @JsonBackReference
    private Consultant consultant;
}