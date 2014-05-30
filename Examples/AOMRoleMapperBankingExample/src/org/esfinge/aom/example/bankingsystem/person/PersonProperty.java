package org.esfinge.aom.example.bankingsystem.person;

import org.esfinge.aom.model.rolemapper.metadata.annotations.EntityProperties;
import org.esfinge.aom.model.rolemapper.metadata.annotations.PropertyType;
import org.esfinge.aom.model.rolemapper.metadata.annotations.PropertyValue;

@EntityProperties
public class PersonProperty {

	@PropertyType
	private PersonPropertyType propertyType;
	
	@PropertyValue
	private Object value;

	public PersonPropertyType getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(PersonPropertyType propertyType) {
		this.propertyType = propertyType;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	
}
