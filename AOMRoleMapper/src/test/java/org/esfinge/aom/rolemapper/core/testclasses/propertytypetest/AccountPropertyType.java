package org.esfinge.aom.rolemapper.core.testclasses.propertytypetest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.esfinge.aom.model.rolemapper.metadata.annotations.EntityProperty;
import org.esfinge.aom.model.rolemapper.metadata.annotations.FixedMetadata;
import org.esfinge.aom.model.rolemapper.metadata.annotations.Metadata;
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
	
	@FixedMetadata
	private boolean persist = true;
	
	@FixedMetadata
	private String description = "account_property";
	
	@Metadata
	private List<MetadatasAccountPropertyType> metadatas = new ArrayList<MetadatasAccountPropertyType>();
	
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
	
	public void setName(){
		
	}

	public boolean isPersist() {
		return persist;
	}

	public void setPersist(boolean persist) {
		this.persist = persist;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<MetadatasAccountPropertyType> getMetadatas() {
		return metadatas;
	}

	public void setMetadatas(List<MetadatasAccountPropertyType> metadatas) {
		this.metadatas = metadatas;
	}	
}