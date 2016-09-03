package net.sf.esfinge.aom.model.rolemapper.metadata.reader;

import java.util.List;

import net.sf.esfinge.aom.api.model.rolemapper.metadata.reader.IAOMMetadataReader;
import net.sf.esfinge.aom.exceptions.EsfingeAOMException;
import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.FixedEntityProperty;
import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.Name;
import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.PropertyType;
import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.PropertyValue;
import net.sf.esfinge.aom.model.rolemapper.metadata.descriptors.FieldDescriptor;
import net.sf.esfinge.aom.model.rolemapper.metadata.descriptors.PropertyDescriptor;

public class PropertyAnnotationReader implements IAOMMetadataReader {
	
	private FieldAnnotationReader fieldAnnotationReader = new FieldAnnotationReader();
	
	public PropertyDescriptor getDescriptor (Class<?> c) throws EsfingeAOMException
	{
		PropertyDescriptor propertyDescriptor = new PropertyDescriptor();
		
		List<FieldDescriptor> propertyTypeDesc = fieldAnnotationReader.getDescriptor(c, PropertyType.class);
		
		if (!propertyTypeDesc.isEmpty())
		{
			// We consider that only one field is annotated with @PropertyType
			propertyDescriptor.setPropertyTypeDescriptor(propertyTypeDesc.get(0));
		}

		// We consider that the properties attribute will always be a collection
		List<FieldDescriptor> propertyValueDesc = fieldAnnotationReader.getDescriptor(c, PropertyValue.class);
		
		if (!propertyValueDesc.isEmpty())
		{
			// We consider that only one field is annotated with @PropertyValue
			propertyDescriptor.setValueDescriptor(propertyValueDesc.get(0));
		}
		
		List<FieldDescriptor> propertyNameDesc = fieldAnnotationReader.getDescriptor(c, Name.class);
				
		if (!propertyNameDesc.isEmpty())
		{
			propertyDescriptor.setNameDescriptor(propertyNameDesc.get(0));
		}
				
		return propertyDescriptor;
	}
	
	@Override
	public boolean isReaderApplicable(Class<?> c) {		
		return (c.getAnnotation(FixedEntityProperty.class) != null);		
	}
	
}
