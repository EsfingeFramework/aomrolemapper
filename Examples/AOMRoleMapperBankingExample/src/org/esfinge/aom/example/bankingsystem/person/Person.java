package org.esfinge.aom.example.bankingsystem.person;

import java.util.ArrayList;
import java.util.List;

import org.esfinge.aom.model.rolemapper.metadata.annotations.Entity;
import org.esfinge.aom.model.rolemapper.metadata.annotations.EntityProperties;
import org.esfinge.aom.model.rolemapper.metadata.annotations.EntityType;

@Entity
public class Person {
	
	@EntityType
	private PersonType personType;
	
	@EntityProperties
	private List<PersonProperty> properties = new ArrayList<PersonProperty>();

	public List<PersonProperty> getProperties() {
		return properties;
	}

	public void setProperties(List<PersonProperty> properties) {
		this.properties = properties;
	}

	public PersonType getPersonType() {
		return personType;
	}

	public void setPersonType(PersonType personType) {
		this.personType = personType;
	}
}
