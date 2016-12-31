package org.esfinge.aom.model.rolemapper.metadata.reader;

import java.util.List;

import org.esfinge.aom.api.model.rolemapper.metadata.reader.IAOMMetadataReader;
import org.esfinge.aom.exceptions.EsfingeAOMException;
import org.esfinge.aom.model.rolemapper.metadata.annotations.Entity;
import org.esfinge.aom.model.rolemapper.metadata.annotations.EntityProperty;
import org.esfinge.aom.model.rolemapper.metadata.annotations.EntityPropertyMap;
import org.esfinge.aom.model.rolemapper.metadata.annotations.EntityType;
import org.esfinge.aom.model.rolemapper.metadata.annotations.FixedEntityProperty;
import org.esfinge.aom.model.rolemapper.metadata.descriptors.EntityDescriptor;
import org.esfinge.aom.model.rolemapper.metadata.descriptors.FieldDescriptor;

public class EntityAnnotationReader implements IAOMMetadataReader {

	private FieldAnnotationReader fieldAnnotationReader = new FieldAnnotationReader();
	
	public EntityDescriptor getDescriptor (Class<?> c) throws EsfingeAOMException
	{
		EntityDescriptor entityDescriptor = new EntityDescriptor();
		
		List<FieldDescriptor> entityTypeDesc = fieldAnnotationReader.getDescriptor(c, EntityType.class);
		
		if (!entityTypeDesc.isEmpty())
		{
			// We consider that only one field is annotated with @EntityType
			entityDescriptor.setEntityTypeDescriptor(entityTypeDesc.get(0));
		}
		
		List<FieldDescriptor> propertiesDesc = fieldAnnotationReader.getDescriptor(c, EntityProperty.class);
		
		if (!propertiesDesc.isEmpty())
		{
			// We consider that only one field is annotated with @EntityProperties
			entityDescriptor.setDynamicPropertiesDescriptor(propertiesDesc.get(0));
		}
		
		List<FieldDescriptor> propertyMapDesc = fieldAnnotationReader.getDescriptor(c, EntityPropertyMap.class);
		
		if (!propertyMapDesc.isEmpty())
		{
			// We consider that only one field is annotated with @EntityPropertyMap
			entityDescriptor.setMapPropertiesDescriptor(propertyMapDesc.get(0));
		}
		
		List<FieldDescriptor> propertyDesc = fieldAnnotationReader.getDescriptor(c, FixedEntityProperty.class);
		
		entityDescriptor.setFixedPropertiesDescriptor(propertyDesc);
				
		return entityDescriptor;
	}	
	
	@Override
	public boolean isReaderApplicable(Class<?> c) {		
		return (c.getAnnotation(Entity.class) != null);		
	}
	
}
