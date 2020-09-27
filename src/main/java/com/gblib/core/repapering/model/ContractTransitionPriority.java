package com.gblib.core.repapering.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name = "ContractTransitionPriority")
public class ContractTransitionPriority {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int contractTransitionPriorityId;
	
	@Column(name = "contractId")
	private int contractId;
	
	@Column(name = "counterPartyId")
	private int counterPartyId;
	
	@Column(name = "counterPartyName")
	private int counterPartyName;
	
	@Column(name = "counterPartyPriorityVal")
	private int counterPartyPriorityVal;
	
	@Column(name = "counterPartyPriorityWeightage")
	private int counterPartyPriorityWeightage;
	
	@Column(name = "terminationDate")
	@Temporal(TemporalType.DATE)
	private Date terminationDate;
	
	@Column(name = "terminationDatePriorityVal")
	private int terminationDatePriorityVal;
	
	@Column(name = "terminationDatePriorityWeightage")
	private int terminationDatePriorityWeightage;
	
	
}
