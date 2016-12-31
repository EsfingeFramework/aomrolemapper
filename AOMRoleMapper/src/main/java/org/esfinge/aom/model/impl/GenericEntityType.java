package org.esfinge.aom.model.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.api.model.IEntityType;
import org.esfinge.aom.api.model.IProperty;
import org.esfinge.aom.api.model.IPropertyType;
import org.esfinge.aom.api.model.RuleObject;
import org.esfinge.aom.exceptions.EsfingeAOMException;


public class GenericEntityType extends ThingWithProperties implements IEntityType {

	private Map<String, IPropertyType> propertyTypes = new WeakHashMap<String, IPropertyType>();
	
	private String name;
	
	private String packageName;
	
	private Map<String, RuleObject> operations = new LinkedHashMap<>();
	
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
			System.out.println("property: "+ propertyType.getName());
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
	
	@Override
	public IProperty getProperty(String propertyName) throws EsfingeAOMException {
		IPropertyType propertyType = getPropertyType(propertyName);
		
		if (propertyType != null)
		{
			return properties.get(propertyType);
		}
		
		return null;
	}
	
	@Override
	public void setProperty(String propertyName, Object propertyValue)
			throws EsfingeAOMException {				
		if (properties.containsKey(propertyName))
		{
			properties.get(propertyName).setValue(propertyValue);
		}
		
		IPropertyType propertyType = getPropertyType(propertyName);
		
		if(propertyType == null){
			addPropertyType(new GenericPropertyType(propertyName, propertyValue.getClass()));
			propertyType = getPropertyType(propertyName);
		}
		
		properties.put(propertyType, new GenericProperty(propertyType, propertyValue));
	}

	@Override
	public void removeProperty(String propertyName) throws EsfingeAOMException {		
		IPropertyType propertyType = getPropertyType(propertyName);
		properties.remove(propertyType);
	}
	
	@Override
	public void addOperation(String name, RuleObject rule) {
		operations.put(name, rule);		
	}

	@Override
	public RuleObject getOperation(String name) {
		return operations.get(name);
	}

	@Override
	public Collection<RuleObject> getAllRules() {
		return operations.values();
	}
	
	public  Map<String, RuleObject> getAllOperation() {
		return operations;
	}
}
