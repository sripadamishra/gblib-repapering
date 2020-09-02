package com.gblib.core.repapering.repository;

import org.springframework.data.repository.CrudRepository;
import com.gblib.core.repapering.model.CounterParty;

public interface ICounterPartyRepository<U> extends CrudRepository<CounterParty,Long> {

	CounterParty findByCounterPartyId(int counterPartyId);
	CounterParty findByCustomerId(int customerId);
	CounterParty findByCounterPartyName(String counterPartyName);
}
