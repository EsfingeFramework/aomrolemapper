package org.esfinge.aom.example.bankingsystem.accounts;

import org.esfinge.aom.model.rolemapper.metadata.annotations.Name;
import org.esfinge.aom.model.rolemapper.metadata.annotations.PropertyType;
import org.esfinge.aom.model.rolemapper.metadata.annotations.PropertyTypeType;

@PropertyType
public class AccountPropertyType {

	@Name
	private String name;
	
	@PropertyTypeType
	private Object propertyType;
	
	public boolean validate(Object value)
	{
		if (value.getClass().equals(propertyType))
			return true;
		return false;
	}

	public Object getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(Object propertyType) {
		this.propertyType = propertyType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
