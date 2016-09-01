package org.esfinge.aom.model.rolemapper.metadata.reader;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

import org.esfinge.aom.api.model.rolemapper.metadata.reader.IAOMMetadataReader;
import org.esfinge.aom.exceptions.EsfingeAOMException;
import org.esfinge.aom.model.rolemapper.metadata.annotations.CreateEntityMethod;
import org.esfinge.aom.model.rolemapper.metadata.annotations.Entity;
import org.esfinge.aom.model.rolemapper.metadata.annotations.EntityType;
import org.esfinge.aom.model.rolemapper.metadata.annotations.FixedMetadata;
import org.esfinge.aom.model.rolemapper.metadata.annotations.Metadata;
import org.esfinge.aom.model.rolemapper.metadata.annotations.MetadataMap;
import org.esfinge.aom.model.rolemapper.metadata.annotations.Name;
import org.esfinge.aom.model.rolemapper.metadata.annotations.PropertyType;
import org.esfinge.aom.model.rolemapper.metadata.descriptors.EntityTypeDescriptor;
import org.esfinge.aom.model.rolemapper.metadata.descriptors.FieldDescriptor;

public class EntityTypeAnnotationReader implements IAOMMetadataReader {
	
	private FieldAnnotationReader fieldAnnotationReader = new FieldAnnotationReader();
	
	public EntityTypeDescriptor getDescriptor (Class<?> c) throws EsfingeAOMException
	{
		EntityTypeDescriptor entityTypeDescriptor = new EntityTypeDescriptor();

		List<FieldDescriptor> nameDesc = fieldAnnotationReader.getDescriptor(c, Name.class);

		if (!nameDesc.isEmpty())
		{
			// We consider that only one field is annotated with @Name
			entityTypeDescriptor.setNameDescriptor(nameDesc.get(0));
		}
		
		List<FieldDescriptor> entitiesDesc = fieldAnnotationReader.getDescriptor(c, Entity.class);
		
		if (!entitiesDesc.isEmpty())
		{
			// We consider that only one field is annotated with @Entity
			entityTypeDescriptor.setEntitiesDescriptor(entitiesDesc.get(0));
		}
		
		// We consider that the properties attribute will always be a collection
		List<FieldDescriptor> metaDesc = fieldAnnotationReader.getDescriptor(c, Metadata.class);
		if (!metaDesc.isEmpty())
		{
			// We consider that only one field is annotated with @Metadata
			entityTypeDescriptor.setMetadataDescriptor(metaDesc.get(0));
		}
		
	    List<FieldDescriptor> fixedMetaDesc = fieldAnnotationReader.getDescriptor(c, FixedMetadata.class);
	    entityTypeDescriptor.setFixedMetadataDescriptor(fixedMetaDesc);
	    
		// We consider that the properties attribute will always be a List
	    List<FieldDescriptor> mapMetaDesc = fieldAnnotationReader.getDescriptor(c, MetadataMap.class);
		if (!mapMetaDesc.isEmpty())
		{
			// We consider that only one field is annotated with @MetadataMap
			entityTypeDescriptor.setMapMetadataDescriptor(mapMetaDesc.get(0));
		}

		// We consider that the properties attribute will always be a collection
		List<FieldDescriptor> propertyTypesDesc = fieldAnnotationReader.getDescriptor(c, PropertyType.class);
		
		if (!propertyTypesDesc.isEmpty())
		{
			// We consider that only one field is annotated with @PropertyType
			FieldDescriptor propertyTypesDescriptor = propertyTypesDesc.get(0);
			entityTypeDescriptor.setPropertyTypesDescriptor(propertyTypesDescriptor);
		}
		
		for (Method m : c.getDeclaredMethods())
		{
			Annotation annotation = m.getAnnotation(CreateEntityMethod.class);

			if (annotation != null)
			{
				entityTypeDescriptor.setCreateEntityMethod(m);
				break;
			}
		}
		return entityTypeDescriptor;
	}
	
	@Override
	public boolean isReaderApplicable(Class<?> c) {		
		return (c.getAnnotation(EntityType.class) != null);		
	}	
}
