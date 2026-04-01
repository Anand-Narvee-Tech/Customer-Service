package com.example.service;

import java.util.List;
import com.example.entity.Contribution;

public interface ContributionService {

    public Contribution create(Contribution contribution);

    public List<Contribution> getAll(); // ✅ FIX

    public Contribution findById(long id);

    public Contribution update(long id, Contribution contribution);

    public void delete(long id);
}