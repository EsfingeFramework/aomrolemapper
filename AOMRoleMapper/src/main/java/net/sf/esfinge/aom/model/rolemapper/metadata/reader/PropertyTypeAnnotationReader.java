package net.sf.esfinge.aom.model.rolemapper.metadata.reader;

import java.util.List;

import net.sf.esfinge.aom.api.model.rolemapper.metadata.reader.IAOMMetadataReader;
import net.sf.esfinge.aom.exceptions.EsfingeAOMException;
import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.EntityProperty;
import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.FixedMetadata;
import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.Metadata;
import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.MetadataMap;
import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.Name;
import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.PropertyType;
import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.PropertyTypeType;
import net.sf.esfinge.aom.model.rolemapper.metadata.descriptors.FieldDescriptor;
import net.sf.esfinge.aom.model.rolemapper.metadata.descriptors.PropertyTypeDescriptor;

public class PropertyTypeAnnotationReader implements IAOMMetadataReader {
		
	private FieldAnnotationReader fieldAnnotationReader = new FieldAnnotationReader();
	
	public PropertyTypeDescriptor getDescriptor (Class<?> c) throws EsfingeAOMException
	{
		PropertyTypeDescriptor propertyTypeDescriptor = new PropertyTypeDescriptor();
		
		List<FieldDescriptor> propertiesDesc = fieldAnnotationReader.getDescriptor(c, EntityProperty.class);
		
		if (!propertiesDesc.isEmpty())
		{
			// We consider that only one field is annotated with @EntityProperty
			propertyTypeDescriptor.setPropertiesDescriptor(propertiesDesc.get(0));
		}
		
		List<FieldDescriptor> nameDesc = fieldAnnotationReader.getDescriptor(c, Name.class);
		
		if (!nameDesc.isEmpty())
		{
			// We consider that only one field is annotated with @PropertyTypeName
			propertyTypeDescriptor.setNameDescriptor(nameDesc.get(0));
		}
		
		List<FieldDescriptor> typeDesc = fieldAnnotationReader.getDescriptor(c, PropertyTypeType.class);
		
		if (!typeDesc.isEmpty())
		{
			// We consider that only one field is annotated with @PropertyTypeType
			propertyTypeDescriptor.setTypeDescriptor(typeDesc.get(0));
		}
		
		// We consider that the properties attribute will always be a collection
		List<FieldDescriptor> metaDesc = fieldAnnotationReader.getDescriptor(c, Metadata.class);
		if (!metaDesc.isEmpty())
		{
			// We consider that only one field is annotated with @Metadata
			propertyTypeDescriptor.setMetadataDescriptor(metaDesc.get(0));
		}
		
	    List<FieldDescriptor> fixedMetaDesc = fieldAnnotationReader.getDescriptor(c, FixedMetadata.class);
	    propertyTypeDescriptor.setFixedMetadataDescriptor(fixedMetaDesc);
	    
	    // We consider that the properties attribute will always be a List
	    List<FieldDescriptor> mapMetaDesc = fieldAnnotationReader.getDescriptor(c, MetadataMap.class);
		if (!mapMetaDesc.isEmpty())
		{
			// We consider that only one field is annotated with @MetadataMap
			propertyTypeDescriptor.setMapMetadataDescriptor(mapMetaDesc.get(0));
		}
		
		return propertyTypeDescriptor;
	}

	@Override
	public boolean isReaderApplicable(Class<?> c) {		
		return (c.getAnnotation(PropertyType.class) != null);		
	}	
}