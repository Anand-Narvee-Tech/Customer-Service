package com.example.serviceImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.entity.Consultant;
import com.example.entity.ConsultantBankAccount;
import com.example.exception.ResourceNotFoundException;
import com.example.repository.ConsultantBankAccountRepository;
import com.example.repository.ConsultantRepository;
import com.example.service.ConsultantBankAccountService;

@Service
public class ConsultantBankAccountServiceImpl implements ConsultantBankAccountService {
	    @Autowired
	    private ConsultantBankAccountRepository repository;

	    @Autowired
	    private ConsultantRepository consultantRepository;

	 // ✅ CREATE
	    public ConsultantBankAccount create(ConsultantBankAccount bank) {

	        if (bank == null) {
	            throw new IllegalArgumentException("Bank account data must not be null");
	        }

	        return repository.save(bank);
	    }


	    // ✅ GET BY ID
	    public ConsultantBankAccount getByConsultantBankAccount(Long id) {

	        return repository.findById(id)
	                .orElseThrow(() ->
	                        new ResourceNotFoundException("Consultant Bank Account not found with id: " + id));
	    }


	    // ✅ GET ALL
	    @Override
	    public List<ConsultantBankAccount> getByAllConsultantBankAccounts() {

	        List<ConsultantBankAccount> list = repository.findAll();

	        if (list.isEmpty()) {
	            throw new ResourceNotFoundException("No Consultant Bank Accounts found");
	        }

	        return list;
	    }


	    // ✅ UPDATE
	    public ConsultantBankAccount update(Long id, ConsultantBankAccount bank) {

	        if (bank == null) {
	            throw new IllegalArgumentException("Updated bank account data must not be null");
	        }

	        ConsultantBankAccount existing = repository.findById(id)
	                .orElseThrow(() ->
	                        new ResourceNotFoundException("Cannot update. Bank account not found with id: " + id));

	        existing.setAccountHolderName(bank.getAccountHolderName());
	        existing.setBankName(bank.getBankName());
	        existing.setAccountNumber(bank.getAccountNumber());
	        existing.setIfscCode(bank.getIfscCode());
	        existing.setRoutingNumber(bank.getRoutingNumber());

	        return repository.save(existing);
	    }


	    // ✅ DELETE
	    public void delete(Long id) {

	        ConsultantBankAccount existing = repository.findById(id)
	                .orElseThrow(() ->
	                        new ResourceNotFoundException("Cannot delete. Bank account not found with id: " + id));

	        repository.delete(existing);
	    }
	}