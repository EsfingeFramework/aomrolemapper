package org.esfinge.aom.rolemapper.core.testclasses.propertytypetest;

import org.esfinge.aom.model.rolemapper.metadata.annotations.EntityProperty;
import org.esfinge.aom.model.rolemapper.metadata.annotations.PropertyType;
import org.esfinge.aom.model.rolemapper.metadata.annotations.PropertyValue;

@EntityProperty
public class AccountProperty {

	@PropertyType
	private AccountPropertyType propertyType;
	
	@PropertyValue
	private Object value;

	public AccountPropertyType getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(AccountPropertyType propertyType) {
		this.propertyType = propertyType;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
}
