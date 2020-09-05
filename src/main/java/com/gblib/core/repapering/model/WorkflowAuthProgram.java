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
@Table(name = "ContractWorkflowAuthProgram")
public class WorkflowAuthProgram {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int workflowAuthProgramId;
	
	@Column(name = "contractId")
	private int contractId; 
	
	@Column(name = "createdOn")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdOn;
	
	@Column(name = "assignedTo")
	private String assignedTo; 
	
	@Column(name = "updatedOn")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedOn;
	
	@Column(name = "updatedBy")
	private String updatedBy; 
	
	@Column(name = "statusId")
	private int statusId;
	
	@Column(name = "comments")
	private String comments;

	public int getWorkflowAuthProgramId() {
		return workflowAuthProgramId;
	}

	public void setWorkflowAuthProgramId(int workflowAuthProgramId) {
		this.workflowAuthProgramId = workflowAuthProgramId;
	}

	public int getContractId() {
		return contractId;
	}

	public void setContractId(int contractId) {
		this.contractId = contractId;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public String getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

}
