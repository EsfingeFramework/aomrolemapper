package org.esfinge.aom.example.medical;

import java.util.ArrayList;
import java.util.List;

import org.esfinge.aom.model.rolemapper.metadata.annotations.CreateEntityMethod;
import org.esfinge.aom.model.rolemapper.metadata.annotations.EntityType;
import org.esfinge.aom.model.rolemapper.metadata.annotations.Name;
import org.esfinge.aom.model.rolemapper.metadata.annotations.PropertyType;

@EntityType
public class PartyType {
	
	@Name
	private String partyTypeId;
	
	@PropertyType
	private List<ObservationType> propertyTypes = new ArrayList<ObservationType>();
		
	@CreateEntityMethod
	public Party createParty()
	{
		Party party = new Party();
				
		party.setPartyType(this);
		return party;
	}

	public String getPartyTypeId() {
		return partyTypeId;
	}

	public void setPartyTypeId(String partyTypeId) {
		this.partyTypeId = partyTypeId;
	}

	public List<ObservationType> getPropertyTypes() {
		return propertyTypes;
	}

	public void setPropertyTypes(List<ObservationType> propertyTypes) {
		this.propertyTypes = propertyTypes;
	}
	
}
