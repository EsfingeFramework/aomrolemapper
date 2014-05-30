package org.esfinge.aom.model.rolemapper.core;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.WeakHashMap;

import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.api.model.IEntityType;
import org.esfinge.aom.api.model.IPropertyType;
import org.esfinge.aom.exceptions.EsfingeAOMException;
import org.esfinge.aom.model.rolemapper.metadata.descriptors.PropertyTypeDescriptor;
import org.esfinge.aom.model.rolemapper.metadata.repository.EntityTypeMetadataRepository;
import org.esfinge.aom.model.rolemapper.metadata.repository.PropertyTypeMetadataRepository;
import org.esfinge.aom.utils.Utils;

public class AdapterPropertyType implements IPropertyType {

	private Object dsObject;

	private PropertyTypeDescriptor propertyTypeDescriptor;
	
	private static Map<Object, AdapterPropertyType> objectMap = new WeakHashMap<Object, AdapterPropertyType>();
	
	public AdapterPropertyType (String propertyTypeClass) throws EsfingeAOMException
	{
		this(propertyTypeClass, null);
	}
	
	private AdapterPropertyType (String propertyTypeClass, Object dsPropertyType) throws EsfingeAOMException
	{		
		try
		{		
			Class<?> clazz = Class.forName(propertyTypeClass);	
			PropertyTypeDescriptor propertyTypeDescriptor = (PropertyTypeDescriptor)PropertyTypeMetadataRepository.getInstance().getMetadata(clazz);
			this.propertyTypeDescriptor = propertyTypeDescriptor;
			
			Object dsObj = dsPropertyType;
			
			if (dsObj == null)
			{
				dsObj = clazz.newInstance();
			}
			this.dsObject = dsObj;
			objectMap.put(dsObj, this);
				
		}
		catch (Exception e)
		{
			throw new EsfingeAOMException(e);
		}
	}
	
	
	public static AdapterPropertyType getAdapter (Object dsPropertyType) throws EsfingeAOMException
	{
		if (dsPropertyType != null)
		{
			if (objectMap.containsKey(dsPropertyType))
			{
				return objectMap.get(dsPropertyType);
			}
			return new AdapterPropertyType(dsPropertyType.getClass().getName(), dsPropertyType);
		}
		return null;
	}
		
	@Override
	public String getName() throws EsfingeAOMException {
		try {
			Method getNameMethod = propertyTypeDescriptor.getNameDescriptor().getGetFieldMethod();			
			return (String) getNameMethod.invoke(dsObject);
				
		} catch (Exception e) {
			throw new EsfingeAOMException(e);
		}
	}

	@Override
	public void setName(String name) throws EsfingeAOMException {
		try {
			Method setNameMethod = propertyTypeDescriptor.getNameDescriptor().getSetFieldMethod();			
			setNameMethod.invoke(dsObject, name);
				
		} catch (Exception e) {
			throw new EsfingeAOMException(e);
		}
	}

	@Override
	public Object getType() throws EsfingeAOMException {
		try {
			Method getTypeMethod = propertyTypeDescriptor.getTypeDescriptor().getGetFieldMethod();			
			Object type = getTypeMethod.invoke(dsObject);
			if (type instanceof Class<?>)
			{
				return Utils.convertToBoxingClass((Class<?>)type);
			}
			if (EntityTypeMetadataRepository.getInstance().isReaderApplicable(type.getClass()))
			{
				return AdapterEntityType.getAdapter(type);
			}
		} catch (Exception e) {
			throw new EsfingeAOMException(e);
		}
		
		throw new EsfingeAOMException("Invalid type");
	}

	@Override
	public void setType(Object type) throws EsfingeAOMException {

		Object typeToSet = null;
		
		if (type instanceof Class<?>)
		{
			// The Property value is an Object. Therefore, its type will never be a primitive type
			// We convert any primitive type to its boxing class, otherwise there would not have
			// any suitable value
			typeToSet = Utils.convertToBoxingClass((Class<?>) type);
		}
		else if (type instanceof IEntityType)
		{
			IEntityType entityType = (IEntityType)type;
			Object associatedObject = entityType.getAssociatedObject();
			typeToSet = associatedObject;
		}	
		
		if (typeToSet != null)
		{
			try {
				Method setTypeMethod = propertyTypeDescriptor.getTypeDescriptor().getSetFieldMethod();			
				setTypeMethod.invoke(dsObject, typeToSet);
			} catch (Exception e) {
				throw new EsfingeAOMException(e);
			}
		}
		else
		{
			throw new EsfingeAOMException("Invalid type");
		}
	}

	@Override
	public Object getAssociatedObject() {
		return dsObject;
	}
	
	@Override
	public String getTypeAsString() throws EsfingeAOMException {
		Object type = getType();
		if (type instanceof Class<?>)
		{
			return ((Class<?>)type).getName();
		}
		return ((IEntityType)type).getName();
	}
	
	@Override
	public boolean isRelationshipProperty() throws EsfingeAOMException {
		return getType() instanceof IEntityType;
	}
	
	@Override
	public boolean isValidValue(Object value) throws EsfingeAOMException {
		if (value == null)
		{
			return true;
		}
		
		Object type = getType();
		if (type instanceof Class<?>)
		{
			Class<?> classType = (Class<?>)type;			
			return Utils.valueIsAssignable(classType, value);
		}
		IEntityType entityType = (IEntityType)type;
		IEntity entity = (IEntity)value;
		IEntityType valueEntityType = entity.getEntityType();
		return valueEntityType.equals(entityType);
	}
}
