package com.afn.realstat;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.data.domain.Example;

import com.afn.realstat.framework.SpringApplicationContext;

@Entity
@Table(name = "agent_volume", uniqueConstraints = @UniqueConstraint(columnNames = { "userCode", "year" }))
public class AgentVolume extends AbstractEntity {
	
	private static AgentVolumeRepostitory repo;

	@Basic(optional = false)
	private String userCode;
	@Basic(optional = false)
	private Date year;
	private String agentRaw;
	// TODO add FirstName and LastName
	private Integer rank;
	private Integer unitsListed;
	private Double volumeListed;
	private Integer unitsSold;
	private Double volumeSold;
	private Integer unitsTotal;
	private Double volumeTotal;

	private String officeRaw;
	private Double percentMlsVolume;
	private Double avgTtlPrice;
	private Double avgDom;

	public AgentVolume() {
	}

	public AgentVolume(String agentRaw, Date year) {
		this.agentRaw = agentRaw;
		this.year = year;
		extractUserCode();
	}

	@Override
	public String toString() {
		return String.format(
				"AgentVolume [UC='%s', Year='%s',  AgentRaw='%s', OfficeRaw='%s', UnitsTotal='%s', VolumeTotal='%s',]",
				userCode, year, agentRaw, officeRaw, unitsTotal, volumeTotal);
	}

	@Override
	public void clean() {
	}
	
	private void extractUserCode() {
		userCode = agentRaw.split("-")[1];
		userCode = userCode.trim();
	}

	@Override
	public boolean isValid() {
		if (userCode != null && year != null) {
			return true;
		}
		return false;
	}

	@Override
	public Example<AgentVolume> getRefExample() {
  		Example<AgentVolume> e = Example.of(new AgentVolume(getUserCode(), getYear()));
		return e;
	}

	// All getters and setters

	public String getUserCode() {
		if (userCode == null) {
		   extractUserCode();
		}
		return userCode;

	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public Date getYear() {
		return year;
	}

	public void setYear(Date year) {
		this.year = year;
	}

	public Integer getUnitsListed() {
		return unitsListed;
	}

	public void setUnitsListed(Integer unitsListed) {
		this.unitsListed = unitsListed;
	}

	public Double getVolumeListed() {
		return volumeListed;
	}

	public void setVolumeListed(Double volumeListed) {
		this.volumeListed = volumeListed;
	}

	public Integer getUnitsSold() {
		return unitsSold;
	}

	public void setUnitsSold(Integer unitsSold) {
		this.unitsSold = unitsSold;
	}

	public Double getVolumeSold() {
		return volumeSold;
	}

	public void setVolumeSold(Double volumeSold) {
		this.volumeSold = volumeSold;
	}

	public Integer getUnitsTotal() {
		return unitsTotal;
	}

	public void setUnitsTotal(Integer unitsTotal) {
		this.unitsTotal = unitsTotal;
	}

	public Double getVolumeTotal() {
		return volumeTotal;
	}

	public void setVolumeTotal(Double volumeTotal) {
		this.volumeTotal = volumeTotal;
	}

	public String getAgentRaw() {
		return agentRaw;
	}

	public void setAgentRaw(String agentRaw) {
		this.agentRaw = agentRaw;
		extractUserCode();
	}

	public String getOfficeRaw() {
		return officeRaw;
	}

	public void setOfficeRaw(String officeRaw) {
		this.officeRaw = officeRaw;
	}

	public Double getPercentMlsVolume() {
		return percentMlsVolume;
	}

	public void setPercentMlsVolume(Double percentMlsVolume) {
		this.percentMlsVolume = percentMlsVolume;
	}

	public Double getAvgTtlPrice() {
		return avgTtlPrice;
	}

	public void setAvgTtlPrice(Double avgTtlPrice) {
		this.avgTtlPrice = avgTtlPrice;
	}

	public Double getAvgDom() {
		return avgDom;
	}

	public void setAvgDom(Double avgDom) {
		this.avgDom = avgDom;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public AgentVolumeRepostitory getRepo() {
		if (repo == null) {
			repo =  (AgentVolumeRepostitory) SpringApplicationContext.getBean("agentVolumeRepository");
		}
		return repo;
	}

	@Override
	public void save() {
		getRepo().save(this);
		
	}

	@Override
	public void saveOrUpdate() {
		getRepo().save(this);
	}

}
