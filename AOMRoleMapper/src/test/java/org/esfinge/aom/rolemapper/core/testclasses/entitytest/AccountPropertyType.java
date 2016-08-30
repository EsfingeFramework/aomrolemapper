package org.esfinge.aom.rolemapper.core.testclasses.entitytest;

import java.util.HashSet;
import java.util.Set;

import org.esfinge.aom.model.rolemapper.metadata.annotations.EntityProperty;
import org.esfinge.aom.model.rolemapper.metadata.annotations.Name;
import org.esfinge.aom.model.rolemapper.metadata.annotations.PropertyType;
import org.esfinge.aom.model.rolemapper.metadata.annotations.PropertyTypeType;

@PropertyType
public class AccountPropertyType {

	@EntityProperty
	private Set<AccountProperty> properties = new HashSet<AccountProperty>();

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

	public Set<AccountProperty> getProperties() {
		return properties;
	}

	public void setProperties(Set<AccountProperty> properties) {
		this.properties = properties;
	}

	public void addProperties (AccountProperty property)
	{
		properties.add(property);
	}
	
	public void removeProperties (AccountProperty property)
	{
		properties.remove(property);
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
