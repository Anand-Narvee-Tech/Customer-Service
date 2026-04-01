package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.entity.Contribution;

@Repository
public interface ContributionRepository extends JpaRepository<Contribution, Long> {
}