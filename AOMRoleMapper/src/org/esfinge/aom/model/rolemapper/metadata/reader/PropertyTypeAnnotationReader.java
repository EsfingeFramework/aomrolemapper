package org.esfinge.aom.model.rolemapper.metadata.reader;

import java.util.List;

import org.esfinge.aom.api.model.rolemapper.metadata.reader.IAOMMetadataReader;
import org.esfinge.aom.exceptions.EsfingeAOMException;
import org.esfinge.aom.model.rolemapper.metadata.annotations.EntityProperties;
import org.esfinge.aom.model.rolemapper.metadata.annotations.Name;
import org.esfinge.aom.model.rolemapper.metadata.annotations.PropertyType;
import org.esfinge.aom.model.rolemapper.metadata.annotations.PropertyTypeType;
import org.esfinge.aom.model.rolemapper.metadata.descriptors.FieldDescriptor;
import org.esfinge.aom.model.rolemapper.metadata.descriptors.PropertyTypeDescriptor;

public class PropertyTypeAnnotationReader implements IAOMMetadataReader {
		
	private FieldAnnotationReader fieldAnnotationReader = new FieldAnnotationReader();
	
	public PropertyTypeDescriptor getDescriptor (Class<?> c) throws EsfingeAOMException
	{
		PropertyTypeDescriptor propertyDescriptor = new PropertyTypeDescriptor();
		
		List<FieldDescriptor> propertiesDesc = fieldAnnotationReader.getDescriptor(c, EntityProperties.class);
		
		if (!propertiesDesc.isEmpty())
		{
			// We consider that only one field is annotated with @EntityProperty
			propertyDescriptor.setPropertiesDescriptor(propertiesDesc.get(0));
		}
		
		List<FieldDescriptor> nameDesc = fieldAnnotationReader.getDescriptor(c, Name.class);
		
		if (!nameDesc.isEmpty())
		{
			// We consider that only one field is annotated with @PropertyTypeName
			propertyDescriptor.setNameDescriptor(nameDesc.get(0));
		}
		
		List<FieldDescriptor> typeDesc = fieldAnnotationReader.getDescriptor(c, PropertyTypeType.class);
		
		if (!typeDesc.isEmpty())
		{
			// We consider that only one field is annotated with @PropertyTypeType
			propertyDescriptor.setTypeDescriptor(typeDesc.get(0));
		}
		
		return propertyDescriptor;
	}

	@Override
	public boolean isReaderApplicable(Class<?> c) {		
		return (c.getAnnotation(PropertyType.class) != null);		
	}
	
}
