package org.esfinge.aom.model.rolemapper.core;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.api.model.IEntityType;
import org.esfinge.aom.api.model.IProperty;
import org.esfinge.aom.api.model.IPropertyType;
import org.esfinge.aom.exceptions.EsfingeAOMException;
import org.esfinge.aom.model.factories.PropertyFactory;
import org.esfinge.aom.model.impl.GenericEntityType;
import org.esfinge.aom.model.impl.GenericProperty;
import org.esfinge.aom.model.impl.GenericPropertyType;
import org.esfinge.aom.model.rolemapper.metadata.descriptors.EntityDescriptor;
import org.esfinge.aom.model.rolemapper.metadata.descriptors.FieldDescriptor;
import org.esfinge.aom.model.rolemapper.metadata.descriptors.PropertyDescriptor;
import org.esfinge.aom.model.rolemapper.metadata.repository.EntityMetadataRepository;
import org.esfinge.aom.model.rolemapper.metadata.repository.PropertyMetadataRepository;

public class AdapterEntity implements IEntity {
	
	private Object dsObject;
	
	private EntityDescriptor entityDescriptor;
		
	private IEntityType entityType;
	
	private Class<?> dsPropertiesClass;
	
	private static Map<Object, AdapterEntity> objectMap = new WeakHashMap<Object, AdapterEntity>();
	
	private Map<String, IProperty> relationshipProperties = new WeakHashMap<String, IProperty>();
	
	private Map<String, AdapterFixedProperty> fixedPropertiesPerName = new WeakHashMap<String, AdapterFixedProperty>();
			
	public AdapterEntity (IEntityType entityType, Class clazz) throws EsfingeAOMException
	{
		this(entityType, clazz, null);
	}
	
	AdapterEntity (IEntityType entityType, Class clazz, Object dsEntity) throws EsfingeAOMException
	{		
		try
		{			
			EntityDescriptor entityDescriptor = (EntityDescriptor)EntityMetadataRepository.getInstance().getMetadata(clazz);
			this.entityDescriptor = entityDescriptor;
			
			Object dsObj = dsEntity;
			
			if (dsObj == null) {
				dsObj = clazz.newInstance();
			}
			this.dsObject = dsObj;
			objectMap.put(dsObj, this);
			
			if(entityType != null || entityDescriptor.getEntityTypeDescriptor() != null){
				setEntityType(entityType);
			}else{
				setEntityType(new GenericEntityType(clazz.getPackage().getName(), clazz.getSimpleName()));
			}

			FieldDescriptor propertiesDescriptor = entityDescriptor.getDynamicPropertiesDescriptor();
			
			if(propertiesDescriptor != null)
				dsPropertiesClass = propertiesDescriptor.getInnerFieldClass();
						
			Map<String, FieldDescriptor> fixedPropertyDescriptorMap = entityDescriptor.getFixedPropertiesDescriptors();
			for (String fieldName : fixedPropertyDescriptorMap.keySet())
			{
				IPropertyType propertyType = getEntityType().getPropertyType(fieldName);
				if(propertyType == null){
					Class proptype = fixedPropertyDescriptorMap.get(fieldName).getFieldClass();
					propertyType = new GenericPropertyType(fieldName, proptype);
					getEntityType().addPropertyType(propertyType);
				}
				AdapterFixedProperty property = new AdapterFixedProperty(dsObj, propertyType);
				fixedPropertiesPerName.put(fieldName, property);				
			}	
		}
		catch (Exception e)
		{
			throw new EsfingeAOMException(e);
		}		
	}
	
	public static AdapterEntity getAdapter (IEntityType entityType, Object dsEntity) throws EsfingeAOMException
	{
		if (dsEntity != null)
		{
			if (objectMap.containsKey(dsEntity))
				return objectMap.get(dsEntity);
			return new AdapterEntity(entityType, dsEntity.getClass(), dsEntity);
		}
		return null;
	}
	
	@Override
	public IEntityType getEntityType() throws EsfingeAOMException {
		try {
			if(entityDescriptor.getEntityTypeDescriptor() != null){
				Method getEntityTypeMethod = entityDescriptor.getEntityTypeDescriptor().getGetFieldMethod();
				Object dsEntityType = getEntityTypeMethod.invoke(dsObject);
				return AdapterEntityType.getAdapter(dsEntityType);
			}else{
				return entityType;
			}
		} catch (Exception e) {
			throw new EsfingeAOMException(e);
		}
	}

	@Override
	public void setEntityType(IEntityType entityType) throws EsfingeAOMException {
		try {
			
			if(entityDescriptor.getEntityTypeDescriptor() != null){
				Method setEntityTypeMethod = entityDescriptor.getEntityTypeDescriptor().getSetFieldMethod();
				if(entityType == null){
					entityType = AdapterEntityType.getAdapter(setEntityTypeMethod.getParameterTypes()[0].newInstance());
				}
				setEntityTypeMethod.invoke(dsObject, entityType.getAssociatedObject());
			}else{
				this.entityType = entityType;
			}
		} catch (Exception e) {
			throw new EsfingeAOMException(e);
		}
	}
	
	@Override
	public List<IProperty> getProperties() throws EsfingeAOMException {
		try 
		{
			List<IPropertyType> validPropertyTypes = getEntityType().getPropertyTypes();
			List<IProperty> properties = new ArrayList<IProperty>();
			
			if(entityDescriptor.getDynamicPropertiesDescriptor() != null){
				Method getPropertiesMethod = entityDescriptor.getDynamicPropertiesDescriptor().getGetFieldMethod();		
				//TODO We consider that the ds class initializes the collection objects properly
				Collection<?> dsProperties = (Collection<?>) getPropertiesMethod.invoke(dsObject);
			
				for (Object property : dsProperties)
				{
					IProperty adapterProperty = AdapterProperty.getAdapter(property);
					if (adapterProperty.getPropertyType()== null || validPropertyTypes.contains(adapterProperty.getPropertyType()))
					{
						properties.add(adapterProperty);
					}
				}
			}
			
			// Fixed properties
			for (IProperty property : fixedPropertiesPerName.values())
			{
				if (validPropertyTypes.contains(property.getPropertyType()))
				{
					properties.add(property);
				}
			}
			
			// Relationship properties
			for (IProperty property : relationshipProperties.values())
			{
				if (validPropertyTypes.contains(property.getPropertyType()))
				{
					properties.add(property);
				}
			}
			
			return properties;
		}
		catch (Exception e)
		{
			throw new EsfingeAOMException(e);
		}
	}
		
	@Override
	public void setProperty(String propertyName, Object propertyValue) throws EsfingeAOMException {		
		
		try 
		{
			
			IPropertyType propertyType = getPropertyType(propertyName);

			if (propertyType == null){
				if(entityDescriptor.getPropertyDescriptor().getPropertyTypeDescriptor() != null){
					throw new EsfingeAOMException("Tried to get a property that does not exist in Entity Type");
				}
			}
			
			for (IProperty property : getProperties())
			{
				if (property.getName().equals(propertyName))
				{
					property.setValue(propertyValue);
					return;
				}
			}
			
			if (propertyType != null && propertyType.isRelationshipProperty())
			{
				IProperty property = PropertyFactory.createProperty(propertyType, propertyValue);				
				relationshipProperties.put(propertyName, property);
			}

			Method getPropertiesMethod = entityDescriptor.getDynamicPropertiesDescriptor().getGetFieldMethod();				
			Collection dsProperties = (Collection<?>) getPropertiesMethod.invoke(dsObject);
			AdapterProperty property = new AdapterProperty(dsPropertiesClass.getName());
			if(propertyType == null){
				property.setName(propertyName);
			}else{
				property.setPropertyType(propertyType);
			}
			property.setValue(propertyValue);
			dsProperties.add(property.getAssociatedObject());	
		}
		catch (Exception e)
		{
			throw new EsfingeAOMException(e);
		}
	}
	
	@Override
	public void removeProperty(String propertyName) throws EsfingeAOMException {
		
		if (fixedPropertiesPerName.containsKey(propertyName))
		{
			throw new EsfingeAOMException("This property is a fixed property and cannot be removed");
		}
		
		IPropertyType propertyType = getPropertyType(propertyName);
		
		try 
		{				
			for (IProperty property : getProperties())
			{
				if (property.getPropertyType().equals(propertyType))
				{
					if (property instanceof AdapterProperty)
					{
						Method getPropertiesMethod = entityDescriptor.getDynamicPropertiesDescriptor().getGetFieldMethod();				
						Collection dsProperties = (Collection<?>) getPropertiesMethod.invoke(dsObject);
						dsProperties.remove(property.getAssociatedObject());
					}
					else if (relationshipProperties.containsKey(propertyType.getName()))
					{
						relationshipProperties.remove(propertyType.getName());
					}					
				}
			}			
		}
		catch (Exception e)
		{
			throw new EsfingeAOMException(e);
		}
	}
	
	@Override
	public IProperty getProperty(String propertyName) throws EsfingeAOMException {
		
		for (IProperty property : getProperties())
		{
			if (property.getName().equals(propertyName))
				return property;
		}
		
		return null;
	}
	
	private IPropertyType getPropertyType(String propertyName) throws EsfingeAOMException {
		IPropertyType propertyType = getEntityType().getPropertyType(propertyName);
		return propertyType;
	}
	
	@Override
	public Object getAssociatedObject() {
		return dsObject;
	}
	
}
