package org.esfinge.aom.model.rolemapper.core;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.api.model.IProperty;
import org.esfinge.aom.api.model.IPropertyType;
import org.esfinge.aom.exceptions.EsfingeAOMException;
import org.esfinge.aom.model.rolemapper.metadata.descriptors.PropertyDescriptor;
import org.esfinge.aom.model.rolemapper.metadata.repository.EntityMetadataRepository;
import org.esfinge.aom.model.rolemapper.metadata.repository.PropertyMetadataRepository;

public class AdapterProperty implements IProperty {
	
	private Object dsObject;
	
	private PropertyDescriptor propertyDescriptor;
	
	private static Map<Object, AdapterProperty> objectMap = new WeakHashMap<Object, AdapterProperty>();

	public AdapterProperty (String propertyClass) throws EsfingeAOMException
	{
		this(propertyClass, null);
	}
	
	private AdapterProperty (String propertyClass, Object dsProperty) throws EsfingeAOMException
	{		
		try
		{		
			Class<?> clazz = Class.forName(propertyClass);	
			PropertyDescriptor propertyDescriptor = (PropertyDescriptor)PropertyMetadataRepository.getInstance().getMetadata(clazz);
			this.propertyDescriptor = propertyDescriptor;
			
			Object dsObj = dsProperty;
			
			if (dsObj == null)
			{
				dsObj = clazz.newInstance();
			}
			dsObject = dsObj;
			objectMap.put(dsObj, this);
						
		}
		catch (Exception e)
		{
			throw new EsfingeAOMException(e);
		}
	}
	
	public static AdapterProperty getAdapter(Object dsProperty) throws EsfingeAOMException
	{
		if (dsProperty != null)
		{
			if (objectMap.containsKey(dsProperty))
				return objectMap.get(dsProperty);
			return new AdapterProperty(dsProperty.getClass().getName(), dsProperty);
		}
		return null;
	}

	@Override
	public Object getValue() throws EsfingeAOMException {
		try {			
			Method getValueMethod = propertyDescriptor.getValueDescriptor().getGetFieldMethod();
			Object dsValue = getValueMethod.invoke(dsObject);
			if (dsValue != null)
			{
				if (EntityMetadataRepository.getInstance().isReaderApplicable(dsValue.getClass()))
				{
					return AdapterEntity.getAdapter(null, dsValue);
				}
			}
			return dsValue;
		} catch (Exception e) {
			throw new EsfingeAOMException(e);
		}
	}
	
	@Override
	public IPropertyType getPropertyType() throws EsfingeAOMException {
		
		try {
			if(propertyDescriptor != null){
				if(propertyDescriptor.getPropertyTypeDescriptor() == null){
					return null;
				}
			}
			Method getPropertyTypeMethod = propertyDescriptor.getPropertyTypeDescriptor().getGetFieldMethod();
			return AdapterPropertyType.getAdapter(getPropertyTypeMethod.invoke(dsObject));
		} catch (Exception e) {
			throw new EsfingeAOMException(e);
		}
	}

	@Override
	public void setPropertyType(IPropertyType propertyType) throws EsfingeAOMException {
		
		try {
			Method setPropertyTypeMethod = propertyDescriptor.getPropertyTypeDescriptor().getSetFieldMethod();
			setPropertyTypeMethod.invoke(dsObject, propertyType.getAssociatedObject());
		} catch (Exception e) {
			throw new EsfingeAOMException(e);
		}		
	}

	@Override
	public void setValue(Object value) throws EsfingeAOMException {
		
		IPropertyType propertyType = getPropertyType();
		
		if (propertyType != null && !propertyType.isValidValue(value))
		{
			throw new EsfingeAOMException("The given value " + value + " is not valid for type " + propertyType.getName());
		}
				
		try {
			Object valueToSet = value;
			
			if (value instanceof IEntity)
			{
				IEntity entity = (IEntity)value;
				valueToSet = entity.getAssociatedObject();
			}			
			Method setValueMethod = propertyDescriptor.getValueDescriptor().getSetFieldMethod();
			setValueMethod.invoke(dsObject, valueToSet);
		} catch (Exception e) {
			throw new EsfingeAOMException(e);
		}	
	}
	
	@Override
	public Object getAssociatedObject() {
		return dsObject;
	}

	@Override
	public String getName() throws EsfingeAOMException {
		try {			
			IPropertyType pt = getPropertyType();
			if(pt != null)
				return pt.getName();
			Method getNameMethod = propertyDescriptor.getNameDescriptor().getGetFieldMethod();
			return (String) getNameMethod.invoke(dsObject);
		} catch (Exception e) {
			throw new EsfingeAOMException(e);
		}		
	}

	@Override
	public void setName(String value) throws EsfingeAOMException {
		try {
			Method setNameMethod = propertyDescriptor.getNameDescriptor().getSetFieldMethod();
			setNameMethod.invoke(dsObject, value);
		} catch (Exception e) {
			throw new EsfingeAOMException(e);
		}
		
	}

	@Override
	public List<IProperty> getProperties() throws EsfingeAOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setProperty(String propertyName, Object propertyValue)
			throws EsfingeAOMException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeProperty(String propertyName) throws EsfingeAOMException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IProperty getProperty(String propertyName)
			throws EsfingeAOMException {
		// TODO Auto-generated method stub
		return null;
	}
}
