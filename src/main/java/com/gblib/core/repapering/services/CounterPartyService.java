package com.gblib.core.repapering.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gblib.core.repapering.model.CounterParty;
import com.gblib.core.repapering.repository.ICounterPartyRepository;

@Service
public class CounterPartyService {

	@Autowired
	ICounterPartyRepository<CounterParty> counterPartyRepository;
	
	@Transactional
	public CounterParty findByCounterPartyId(int counterPartyId) {
		
		CounterParty customer = counterPartyRepository.findByCounterPartyId(counterPartyId);
		return customer;
	}
	
	@Transactional
	public CounterParty findByCustomerId(int customerId) {
		
		CounterParty customer = counterPartyRepository.findByCustomerId(customerId);
		return customer;
	}
	
	@Transactional
	public CounterParty findByCounterPartyName(String counterPartyName) {
		
		CounterParty customer = counterPartyRepository.findByCounterPartyName(counterPartyName);
		return customer;
	}
	
	
}
