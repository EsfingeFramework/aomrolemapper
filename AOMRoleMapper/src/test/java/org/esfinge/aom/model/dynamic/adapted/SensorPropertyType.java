package org.esfinge.aom.model.dynamic.adapted;

import java.util.HashSet;
import java.util.Set;

import org.esfinge.aom.model.rolemapper.metadata.annotations.EntityProperty;
import org.esfinge.aom.model.rolemapper.metadata.annotations.Name;
import org.esfinge.aom.model.rolemapper.metadata.annotations.PropertyType;
import org.esfinge.aom.model.rolemapper.metadata.annotations.PropertyTypeType;

@PropertyType
public class SensorPropertyType {

	@EntityProperty
	private Set<SensorProperty> properties = new HashSet<SensorProperty>();

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

	public Set<SensorProperty> getProperties() {
		return properties;
	}

	public void setProperties(Set<SensorProperty> properties) {
		this.properties = properties;
	}

	public void addProperties (SensorProperty property)
	{
		properties.add(property);
	}
	
	public void removeProperties (SensorProperty property)
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
