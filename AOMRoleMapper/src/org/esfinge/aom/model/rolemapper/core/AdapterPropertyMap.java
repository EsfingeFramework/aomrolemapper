package org.esfinge.aom.model.rolemapper.core;

import java.util.Map;
import java.util.WeakHashMap;

import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.api.model.IProperty;
import org.esfinge.aom.api.model.IPropertyType;
import org.esfinge.aom.exceptions.EsfingeAOMException;

public class AdapterPropertyMap implements IProperty {

	private Object dsObject;
	
	private Object dsObjectKey;
	
	private static Map<Object, AdapterPropertyMap> objectMap = new WeakHashMap<Object, AdapterPropertyMap>();

	private AdapterPropertyMap (String propertyClass, Object dsPropertyKey, Object dsProperty) throws EsfingeAOMException
	{		
		try
		{		
			Class<?> clazz = Class.forName(propertyClass);			
			Object dsObj = dsProperty;
			
			if (dsObj == null)
			{
				dsObj = clazz.newInstance();
			}
			
			dsObject = dsObj;
			dsObjectKey = dsPropertyKey;
			objectMap.put(dsPropertyKey, this);					
		}
		catch (Exception e)
		{
			throw new EsfingeAOMException(e);
		}
	}
	
	public static AdapterPropertyMap getAdapter(Object dsPropertyKey, Object dsProperty) throws EsfingeAOMException
	{
		try{
			if (dsPropertyKey != null && dsProperty != null)
			{
				if (objectMap.containsKey(dsPropertyKey))
					return objectMap.get(dsPropertyKey);
				return new AdapterPropertyMap(dsProperty.getClass().getName(), dsPropertyKey, dsProperty);
			}
			return null;
		}catch(Exception e){
			throw new EsfingeAOMException(e);
		}
	}
	
	@Override
	public IPropertyType getPropertyType() throws EsfingeAOMException {
		return null;
	}

	@Override
	public void setPropertyType(IPropertyType propertyType)
			throws EsfingeAOMException {
	}

	@Override
	public Object getValue() throws EsfingeAOMException {
		if(dsObjectKey != null){
			return dsObject;
		}
		return null;
	}

	@Override
	public void setValue(Object value) throws EsfingeAOMException {
		try{
			Object valueToSet = value;
			
			if (value instanceof IEntity)
			{
				IEntity entity = (IEntity)value;
				valueToSet = entity.getAssociatedObject();
			}
			
			dsObject = valueToSet;
	    } catch(Exception e){
			throw new EsfingeAOMException(e);
		}
	}

	@Override
	public Object getAssociatedObject() {
		return dsObject;
	}

	@Override
	public String getName() throws EsfingeAOMException {
		try{
			if(dsObjectKey != null){
				return (String) dsObjectKey;
			}
			return null;
		} catch (Exception e) {
			throw new EsfingeAOMException(e);
		}		
	}

	@Override
	public void setName(String value) throws EsfingeAOMException {
		dsObjectKey = value;
	}
}