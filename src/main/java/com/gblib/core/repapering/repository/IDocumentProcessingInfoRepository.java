package com.gblib.core.repapering.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import com.gblib.core.repapering.model.DocumentProcessingInfo;

public interface IDocumentProcessingInfoRepository<U> extends CrudRepository<DocumentProcessingInfo,Long> {

	List<DocumentProcessingInfo> findByContractId(int contractId);
}
