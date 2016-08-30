package org.esfinge.aom.model.impl;

import org.esfinge.aom.api.model.IEntityType;
import org.esfinge.aom.api.model.IProperty;
import org.esfinge.aom.api.model.IPropertyType;
import org.esfinge.aom.exceptions.EsfingeAOMException;

public class GenericProperty extends ThingWithProperties implements IProperty {

	private IPropertyType propertyType;
	
	private Object value;
	
	private String name;
	
	public GenericProperty(IPropertyType propertyType, Object value) throws EsfingeAOMException
	{
		this.propertyType = propertyType;
		setValue(value);
	}
	
	public GenericProperty(IPropertyType propertyType) throws EsfingeAOMException
	{
		this(propertyType, null);
	}
	
	public GenericProperty(String name, Object value) throws EsfingeAOMException
	{
		setName(name);
		setValue(value);
	}
	
	@Override
	public IPropertyType getPropertyType() {
		return propertyType;
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public void setPropertyType(IPropertyType propertyType) throws EsfingeAOMException {
		if(name == null || propertyType.equals(name)){
			this.propertyType = propertyType;
		}else{
			throw new EsfingeAOMException("This property already have a different name associated");
		}
	}

	@Override
	public void setValue(Object value) throws EsfingeAOMException {
		if (propertyType != null && !propertyType.isValidValue(value))
		{
			throw new EsfingeAOMException("The given value " + value + " is not valid for type " + propertyType.getName());
		}
		this.value = value;
	}

	@Override
	public Object getAssociatedObject() {
		return null;
	}

	@Override
	public String getName() throws EsfingeAOMException {
		return propertyType.getName();
	}

	@Override
	public void setName(String value) throws EsfingeAOMException {
		if(propertyType == null){
			this.name = value;
		}else{
			throw new EsfingeAOMException("This property already have an entity property associated");
		}
		
	}

	@Override
	public IEntityType getEntityType() {
		return null;
	}
}
