package org.esfinge.aom.example.bankingsystem.person;

import java.util.HashSet;
import java.util.Set;

import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.EntityProperty;
import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.Name;
import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.PropertyType;
import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.PropertyTypeType;

@PropertyType
public class PersonPropertyType {

	@EntityProperty
	private Set<PersonProperty> properties = new HashSet<PersonProperty>();

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

	public Set<PersonProperty> getProperties() {
		return properties;
	}

	public void setProperties(Set<PersonProperty> properties) {
		this.properties = properties;
	}

	public void addProperties (PersonProperty property)
	{
		properties.add(property);
	}
	
	public void removeProperties (PersonProperty property)
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
