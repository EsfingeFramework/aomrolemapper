package net.sf.esfinge.aom.model.impl;

import java.util.HashMap;

import net.sf.esfinge.aom.api.model.IEntity;
import net.sf.esfinge.aom.api.model.IEntityType;
import net.sf.esfinge.aom.api.model.IPropertyType;
import net.sf.esfinge.aom.exceptions.EsfingeAOMException;
import net.sf.esfinge.aom.utils.Utils;

public class GenericPropertyType extends ThingWithProperties implements IPropertyType {

	protected String name;
	protected Object type;
	
	public GenericPropertyType (String name, Object type)
	{
		this.name = name;
		if (type instanceof Class<?>)
		{
			this.type = Utils.convertToBoxingClass((Class<?>)type);
		}
	}

	public GenericPropertyType (String name, IEntityType type)
	{
		this.name = name;
		this.type = type;
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
	public Object getType() {
		return type;
	}

	@Override
	public void setType(Object type) throws EsfingeAOMException {
		if (type instanceof Class<?>)
		{
			// The Property value is an Object. Therefore, its type will never be a primitive type
			// We convert any primitive type to its boxing class, otherwise there would not have
			// any suitable value
			this.type = Utils.convertToBoxingClass((Class<?>) type);
		}
		else if (type instanceof IEntityType)
		{
			this.type = type;
		}
		else
		{
			throw new EsfingeAOMException("Invalid value for type");
		}
	}

	@Override
	public boolean equals(Object obj) {
		
		if (obj instanceof GenericPropertyType)
		{
			GenericPropertyType propertyType = (GenericPropertyType)obj;
			if (propertyType.getName().equals(this.name) && propertyType.getType().equals(this.type))
			{
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public Object getAssociatedObject() {
		return null;
	}

	@Override
	public String getTypeAsString() throws EsfingeAOMException {
		if (type instanceof Class<?>)
		{
			return ((Class<?>)type).getName();
		}
		return ((IEntityType)type).getName();
	}

	@Override
	public boolean isRelationshipProperty() throws EsfingeAOMException {
		return type instanceof IEntityType;
	}

	@Override
	public boolean isValidValue(Object value) throws EsfingeAOMException {
		//TODO
		if (value == null){
			return true;
		}
		
		Object type = getType();
		if (type instanceof Class<?>){
			if (type.equals(Object.class) || (value.getClass().equals(HashMap.class)))
				return true;
			
			Class<?> classType = (Class<?>)type;			
			return Utils.valueIsAssignable(classType, value);
		}
		
		IEntityType entityType = (IEntityType)type;
		IEntity entity = (IEntity) value;
		
		return entity.getEntityType().equals(entityType);
	}

	@Override
	public IEntityType getEntityType() {
		return null;
	}
}