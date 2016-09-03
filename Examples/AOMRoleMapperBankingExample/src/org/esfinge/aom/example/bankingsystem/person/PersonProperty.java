package org.esfinge.aom.example.bankingsystem.person;

import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.EntityProperty;
import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.PropertyType;
import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.PropertyValue;

@EntityProperty
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
