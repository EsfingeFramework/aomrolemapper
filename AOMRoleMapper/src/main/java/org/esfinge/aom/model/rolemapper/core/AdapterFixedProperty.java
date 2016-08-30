package org.esfinge.aom.model.rolemapper.core;

import java.lang.reflect.Field;
import java.util.List;

import org.esfinge.aom.api.model.IProperty;
import org.esfinge.aom.api.model.IPropertyType;
import org.esfinge.aom.exceptions.EsfingeAOMException;
import org.esfinge.aom.model.rolemapper.metadata.descriptors.FieldDescriptor;
import org.esfinge.aom.model.rolemapper.metadata.reader.FieldMetadataReader;

public class AdapterFixedProperty implements IProperty {

	private Object dsObject;
	
	private IPropertyType propertyType;
			
	private FieldDescriptor fieldDescriptor;
	
	public AdapterFixedProperty(Object dsEntity, IPropertyType propertyType) throws EsfingeAOMException
	{
		this.propertyType = propertyType;
		this.dsObject = dsEntity;
				
		try {
			Field field = dsEntity.getClass().getDeclaredField(propertyType.getName());
			// TODO use repository?
			FieldMetadataReader metadataReader = new FieldMetadataReader();
			fieldDescriptor = metadataReader.getDescriptor(dsEntity.getClass(), field);
		} catch (Exception e) {
			throw new EsfingeAOMException(e);
		}	
	}

	@Override
	public IPropertyType getPropertyType() {
		return propertyType;
	}

	@Override
	public void setPropertyType(IPropertyType propertyType) throws EsfingeAOMException {
		throw new EsfingeAOMException("Cannot change property type");
	}

	@Override
	public Object getValue() throws EsfingeAOMException {		
		try {
			return fieldDescriptor.getGetFieldMethod().invoke(dsObject);
		} catch (Exception e) {
			throw new EsfingeAOMException(e);
		} 
	}

	@Override
	public void setValue(Object value) throws EsfingeAOMException {
		IPropertyType propertyType = getPropertyType();
		if (!propertyType.isValidValue(value))
		{
			throw new EsfingeAOMException("The given value " + value + " is not valid for type " + propertyType.getName());
		}
		try {
			fieldDescriptor.getSetFieldMethod().invoke(dsObject, value);
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
		return getPropertyType().getName();
	}

	@Override
	public void setName(String value) throws EsfingeAOMException {
		throw new EsfingeAOMException("The name of a fixed property cannot be changed");
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
