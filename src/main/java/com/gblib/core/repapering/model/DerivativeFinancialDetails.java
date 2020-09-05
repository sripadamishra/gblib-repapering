package com.gblib.core.repapering.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;



@Entity
@Table(name = "DerivativeFinancialDetails")
public class DerivativeFinancialDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int derivativeContractFinancialId;
	
	@Column(name = "contractId")
	private int contractId;
	
	@Column(name = "counterPartyId")
	private int counterPartyId;
	
	@Column(name = "jurisdiction")
	private String jurisdiction;
	
	@Column(name = "governingLaw")
	private String governingLaw;
	
	@Column(name = "masterAgreementType")
	private String masterAgreementType;
	 	
	@Column(name = "masterAgreementActive")
	private char masterAgreementActive;
	
	@Column(name = "creditSupportAnnex")
	private String creditSupportAnnex;
	
	@Column(name = "creditSupportAnnexActive")
	private char creditSupportAnnexActive;
	
	@Column(name = "Triparty")
	private char Triparty;
	
	@Column(name = "creditSupByTitleTransfer")
	private char creditSupByTitleTransfer;
	
	@Column(name = "initialMargin")
	private double initialMargin;
	
	@Column(name = "nettedAgainstVariation")
	private char nettedAgainstVariation;
	
	@Column(name = "nettingEligible")
	private char nettingEligible;
	
	@Column(name = "collateralEnforceability")
	private char collateralEnforceability;
	
	@Column(name = "triggerDowngrade")
	private char triggerDowngrade;
	
	@Column(name = "rehypothicationRights")
	private char rehypothicationRights;
	
	@Column(name = "colleteralType")
	private String colleteralType;
	
	@Column(name = "validCurrencies")
	private String validCurrencies;
	
	@Column(name = "baseCurrency")
	private String baseCurrency;
	
	@Column(name = "valuationPercentage")
	private double valuationPercentage;
	
	@Column(name = "minTransferAmount")
	private double minTransferAmount;
	
	@Column(name = "thresholdAmount")
	private double thresholdAmount;
	
	@Column(name = "variationMargin")
	private double variationMargin;

	public int getDerivativeContractFinancialId() {
		return derivativeContractFinancialId;
	}

	public void setDerivativeContractFinancialId(int derivativeContractFinancialId) {
		this.derivativeContractFinancialId = derivativeContractFinancialId;
	}

	public int getContractId() {
		return contractId;
	}

	public void setContractId(int contractId) {
		this.contractId = contractId;
	}

	public int getCounterPartyId() {
		return counterPartyId;
	}

	public void setCounterPartyId(int counterPartyId) {
		this.counterPartyId = counterPartyId;
	}

	public String getJurisdiction() {
		return jurisdiction;
	}

	public void setJurisdiction(String jurisdiction) {
		this.jurisdiction = jurisdiction;
	}

	public String getGoverningLaw() {
		return governingLaw;
	}

	public void setGoverningLaw(String governingLaw) {
		this.governingLaw = governingLaw;
	}

	public String getMasterAgreementType() {
		return masterAgreementType;
	}

	public void setMasterAgreementType(String masterAgreementType) {
		this.masterAgreementType = masterAgreementType;
	}

	public char getMasterAgreementActive() {
		return masterAgreementActive;
	}

	public void setMasterAgreementActive(char masterAgreementActive) {
		this.masterAgreementActive = masterAgreementActive;
	}

	public String getCreditSupportAnnex() {
		return creditSupportAnnex;
	}

	public void setCreditSupportAnnex(String creditSupportAnnex) {
		this.creditSupportAnnex = creditSupportAnnex;
	}

	public char getCreditSupportAnnexActive() {
		return creditSupportAnnexActive;
	}

	public void setCreditSupportAnnexActive(char creditSupportAnnexActive) {
		this.creditSupportAnnexActive = creditSupportAnnexActive;
	}

	public char getTriparty() {
		return Triparty;
	}

	public void setTriparty(char triparty) {
		Triparty = triparty;
	}

	public char getCreditSupByTitleTransfer() {
		return creditSupByTitleTransfer;
	}

	public void setCreditSupByTitleTransfer(char creditSupByTitleTransfer) {
		this.creditSupByTitleTransfer = creditSupByTitleTransfer;
	}

	public double getInitialMargin() {
		return initialMargin;
	}

	public void setInitialMargin(double initialMargin) {
		this.initialMargin = initialMargin;
	}

	public char getNettedAgainstVariation() {
		return nettedAgainstVariation;
	}

	public void setNettedAgainstVariation(char nettedAgainstVariation) {
		this.nettedAgainstVariation = nettedAgainstVariation;
	}

	public char getNettingEligible() {
		return nettingEligible;
	}

	public void setNettingEligible(char nettingEligible) {
		this.nettingEligible = nettingEligible;
	}

	public char getCollateralEnforceability() {
		return collateralEnforceability;
	}

	public void setCollateralEnforceability(char collateralEnforceability) {
		this.collateralEnforceability = collateralEnforceability;
	}

	public char getTriggerDowngrade() {
		return triggerDowngrade;
	}

	public void setTriggerDowngrade(char triggerDowngrade) {
		this.triggerDowngrade = triggerDowngrade;
	}

	public char getRehypothicationRights() {
		return rehypothicationRights;
	}

	public void setRehypothicationRights(char rehypothicationRights) {
		this.rehypothicationRights = rehypothicationRights;
	}

	public String getColleteralType() {
		return colleteralType;
	}

	public void setColleteralType(String colleteralType) {
		this.colleteralType = colleteralType;
	}

	public String getValidCurrencies() {
		return validCurrencies;
	}

	public void setValidCurrencies(String validCurrencies) {
		this.validCurrencies = validCurrencies;
	}

	public String getBaseCurrency() {
		return baseCurrency;
	}

	public void setBaseCurrency(String baseCurrency) {
		this.baseCurrency = baseCurrency;
	}

	public double getValuationPercentage() {
		return valuationPercentage;
	}

	public void setValuationPercentage(double valuationPercentage) {
		this.valuationPercentage = valuationPercentage;
	}

	public double getMinTransferAmount() {
		return minTransferAmount;
	}

	public void setMinTransferAmount(double minTransferAmount) {
		this.minTransferAmount = minTransferAmount;
	}

	public double getThresholdAmount() {
		return thresholdAmount;
	}

	public void setThresholdAmount(double thresholdAmount) {
		this.thresholdAmount = thresholdAmount;
	}

	public double getVariationMargin() {
		return variationMargin;
	}

	public void setVariationMargin(double variationMargin) {
		this.variationMargin = variationMargin;
	}
	
	
}
