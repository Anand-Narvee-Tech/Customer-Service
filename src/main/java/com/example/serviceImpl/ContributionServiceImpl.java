package com.example.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Consultant;
import com.example.entity.Contribution;
import com.example.exception.ResourceNotFoundException;
import com.example.repository.ContributionRepository;
import com.example.service.ContributionService;

@Service
public class ContributionServiceImpl implements ContributionService {

    @Autowired
    private ContributionRepository contributionRepository;

    @Override
    public Contribution create(Contribution contribution) {

        // ✅ FIX: set consultant properly (if coming from request)
        if (contribution.getConsultant() != null) {
            Long consultantId = contribution.getConsultant().getId();

            Consultant consultant = new Consultant();
            consultant.setId(consultantId);

            contribution.setConsultant(consultant);
        }

        return contributionRepository.save(contribution);
    }

    @Override
    public List<Contribution> getAll() {
        return contributionRepository.findAll();
    }

    @Override
    public Contribution findById(long id) {
        return contributionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contribution not found with id: " + id));
    }

    @Override
    public Contribution update(long id, Contribution contribution) {
        Contribution existing = findById(id);

        existing.setOtherContribution(contribution.getOtherContribution());
        existing.setContributionType(contribution.getContributionType());
        existing.setAmount(contribution.getAmount());
        existing.setAmountType(contribution.getAmountType());

        return contributionRepository.save(existing);
    }

    @Override
    public void delete(long id) {
        Contribution existing = findById(id);
        contributionRepository.delete(existing);
    }
}