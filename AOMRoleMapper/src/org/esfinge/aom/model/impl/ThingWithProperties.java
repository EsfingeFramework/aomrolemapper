package org.esfinge.aom.model.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.esfinge.aom.api.model.HasProperties;
import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.api.model.IEntityType;
import org.esfinge.aom.api.model.IProperty;
import org.esfinge.aom.api.model.IPropertyType;
import org.esfinge.aom.exceptions.EsfingeAOMException;

public abstract class ThingWithProperties implements HasProperties{

	protected Map<IPropertyType, IProperty> properties = new WeakHashMap<IPropertyType, IProperty>();

	public ThingWithProperties() {
		super();
	}

	public abstract IEntityType getEntityType(); //esse cara pode ser nulo
	
	@Override
	public List<IProperty> getProperties() throws EsfingeAOMException {
		
		ArrayList<IProperty> result = new ArrayList<IProperty>();
		
		if(getEntityType() == null){
			//Esse é a maneira de como criar um novo entity type ele não exista
			/*if(entityType != null || entityDescriptor.getEntityTypeDescriptor() != null){
				setEntityType(entityType);
			}else{
				setEntityType(new GenericEntityType(clazz.getPackage().getName(), clazz.getSimpleName()));
			}*/			
		}
		
		List<IPropertyType> validPropertyTypes = getEntityType().getPropertyTypes();
		List<IPropertyType> invaliPropertyTypes = new ArrayList<IPropertyType>();
	
		for (IProperty property : properties.values())
		{
			IPropertyType propertyType = property.getPropertyType();
			if (validPropertyTypes.contains(propertyType))
			{
				result.add(property);
			}
			else
			{
				invaliPropertyTypes.add(propertyType);
			}
		}
		
		for (IPropertyType propertyType : invaliPropertyTypes)
		{
			properties.remove(propertyType);
		}
		
		return result;
	}

	@Override
	public void setProperty(String propertyName, Object propertyValue)
			throws EsfingeAOMException {
				
		if (properties.containsKey(propertyName))
		{
			properties.get(propertyName).setValue(propertyValue);
		}
		else
		{
			IPropertyType propertyType = getEntityType().getPropertyType(propertyName);
			properties.put(propertyType, new GenericProperty(propertyType, propertyValue));
		}	
	}

	@Override
	public void removeProperty(String propertyName) throws EsfingeAOMException {
		
		IPropertyType propertyType = getEntityType().getPropertyType(propertyName);
		
		if (propertyType != null)
		{
			properties.remove(propertyType);
		}
	}

	@Override
	public IProperty getProperty(String propertyName) throws EsfingeAOMException {
		IPropertyType propertyType = getEntityType().getPropertyType(propertyName);
		
		if (propertyType != null)
		{
			return properties.get(propertyType);
		}
		
		return null;
	}

}