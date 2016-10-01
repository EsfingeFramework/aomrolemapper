package net.sf.esfinge.aom.rolemapper.core.testclasses.entitytest;

import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.EntityProperty;
import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.PropertyType;
import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.PropertyValue;

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