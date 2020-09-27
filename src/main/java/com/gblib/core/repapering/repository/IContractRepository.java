package com.gblib.core.repapering.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.gblib.core.repapering.model.Contract;

@Transactional
public interface IContractRepository<U> extends CrudRepository<Contract,Long> {

	Contract findByContractId(int contractId);
	
	Contract findById(int id);
	
	@Query("select c from Contract c")
	List<Contract> getAllContracts();
	
	Contract findByContractIdAndCurrStatusId(int contractId,int currStatusId);
	
	List<Contract> findByCurrStatusId(int currStatusId);
	
	@Modifying
	@Query("update Contract c set c.currStatusId = :currStatusId where c.id = :id")
	Integer setNewCurrStatusIdForContract(@Param("currStatusId") int currStatusId, 
			@Param("id") int id);
}
