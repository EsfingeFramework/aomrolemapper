package org.esfinge.aom.model.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.api.model.IEntityType;
import org.esfinge.aom.api.model.IPropertyType;
import org.esfinge.aom.exceptions.EsfingeAOMException;


public class GenericEntityType extends ThingWithProperties implements IEntityType {

	private Map<String, IPropertyType> propertyTypes = new WeakHashMap<String, IPropertyType>();
	
	private String name;
	
	private String packageName;
	
	public GenericEntityType (String packageName, String name)
	{
		this.name = name;
		this.packageName = (packageName != null) ? packageName : "";
	}
	
	public GenericEntityType (String name)
	{
		this("", name);
	}
	
	@Override
	public List<IPropertyType> getPropertyTypes() {
		List<IPropertyType> result = new ArrayList<IPropertyType>();
		result.addAll(propertyTypes.values());
		return result;
	}

	@Override
	public void addPropertyType(IPropertyType propertyType) throws EsfingeAOMException {
		propertyTypes.put(propertyType.getName(), propertyType);
	}

	@Override
	public void removePropertyType(IPropertyType propertyType) throws EsfingeAOMException {
		removePropertyType(propertyType.getName());
	}
	
	@Override
	public void removePropertyType(String propertyName) throws EsfingeAOMException {
		propertyTypes.remove(propertyName);
	}

	@Override
	public IEntity createNewEntity() throws EsfingeAOMException {
		GenericEntity entity = new GenericEntity();
		entity.setEntityType(this);
		for (IPropertyType propertyType : propertyTypes.values())
		{
			entity.setProperty(propertyType.getName(), null);
		}
		
		return entity;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public Object getAssociatedObject() {
		return null;
	}

	@Override
	public IPropertyType getPropertyType(String name) throws EsfingeAOMException {
		return propertyTypes.get(name);
	}

	@Override
	public String getPackageName() {
		return packageName;
	}

	@Override
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	@Override
	public IEntityType getEntityType() {
		return null;
	}
}
