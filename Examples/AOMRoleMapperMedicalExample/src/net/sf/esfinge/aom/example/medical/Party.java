package net.sf.esfinge.aom.example.medical;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.Entity;
import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.EntityProperty;
import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.EntityType;
import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.FixedEntityProperty;

@Entity
public class Party {

	@EntityType
	private PartyType partyType;

	@FixedEntityProperty
	private String name;
	
	@FixedEntityProperty
	private Date recordDate;
	
	@EntityProperty
	private List<Observation> observations = new ArrayList<Observation>();

	public PartyType getPartyType() {
		return partyType;
	}

	public void setPartyType(PartyType partyType) {
		this.partyType = partyType;
	}

	public Date getRecordDate() {
		return recordDate;
	}

	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}

	public List<Observation> getObservations() {
		return observations;
	}

	public void setObservations(List<Observation> observations) {
		this.observations = observations;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


}
