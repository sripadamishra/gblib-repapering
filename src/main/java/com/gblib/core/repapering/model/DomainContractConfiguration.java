package com.gblib.core.repapering.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "DomainContractConfiguration")
public class DomainContractConfiguration {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int domainContractConfigurationId;
	
	@Column(name = "contractId")
	private int contractId;
	
	@Column(name = "regulatoryEventId")
	private int regulatoryEventId;
	
	@Column(name = "domainContextDictionaryId")
	private String domainContextDictionaryId;
	
	@Column(name = "isContextConfigurationActive")
	private boolean isContextConfigurationActive;
	
	@Column(name = "textSimilarity")
	private int textSimilarity;
	
	@Column(name = "domaincontextCurrentFieldValue")
	private String domaincontextCurrentFieldValue;
	
	@Column(name = "domaincontextProposedFieldValue")
	private String domaincontextProposedFieldValue;
}
