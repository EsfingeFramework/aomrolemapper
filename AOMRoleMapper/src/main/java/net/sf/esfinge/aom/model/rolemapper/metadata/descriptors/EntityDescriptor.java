package net.sf.esfinge.aom.model.rolemapper.metadata.descriptors;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sf.esfinge.aom.exceptions.EsfingeAOMException;
import net.sf.esfinge.aom.model.rolemapper.metadata.repository.PropertyMetadataRepository;

public class EntityDescriptor {

	private FieldDescriptor entityTypeDescriptor;
	
	private FieldDescriptor dynamicPropertiesDescriptor;
	
	private Map<String, FieldDescriptor> fixedPropertiesDescriptors = new HashMap<String, FieldDescriptor>();

	private FieldDescriptor mapPropertiesDescriptor;

	public FieldDescriptor getEntityTypeDescriptor() {
		return entityTypeDescriptor;
	}

	public void setEntityTypeDescriptor(FieldDescriptor entityTypeDescriptor) {
		this.entityTypeDescriptor = entityTypeDescriptor;
	}

	public FieldDescriptor getDynamicPropertiesDescriptor() {
		return dynamicPropertiesDescriptor;
	}

	public void setDynamicPropertiesDescriptor(FieldDescriptor dynamicPropertiesDescriptor) {
		this.dynamicPropertiesDescriptor = dynamicPropertiesDescriptor;
	}
	
	public FieldDescriptor getMapPropertiesDescriptor() {
		return this.mapPropertiesDescriptor;		
	}

	public void setMapPropertiesDescriptor(FieldDescriptor fieldDescriptor) {
		this.mapPropertiesDescriptor = fieldDescriptor;		
	}

	public final Map<String, FieldDescriptor> getFixedPropertiesDescriptors() {
		return fixedPropertiesDescriptors;
	}
	
	public FieldDescriptor getFixedPropertiesDescriptor (String name)
	{
		if (fixedPropertiesDescriptors.containsKey(name))
			return fixedPropertiesDescriptors.get(name);
		return null;
	}
	
	public void setFixedPropertiesDescriptor (Collection<FieldDescriptor> descriptors)
	{
		this.fixedPropertiesDescriptors.clear();
		
		for (FieldDescriptor descriptor : descriptors)
			addFixedPropertiesDescriptors(descriptor);
	}
	
	public void addFixedPropertiesDescriptors(FieldDescriptor descriptor)
	{
		this.fixedPropertiesDescriptors.put(descriptor.getFieldName(), descriptor);
	}
	
	public void removeFixedPropertiesDescriptors(FieldDescriptor descriptor)
	{
		this.fixedPropertiesDescriptors.remove(descriptor.getFieldName());
	}
	
	public PropertyDescriptor getPropertyDescriptor() throws EsfingeAOMException{
		Class<?> propertyClass = getDynamicPropertiesDescriptor().getInnerFieldClass();
		return (PropertyDescriptor) PropertyMetadataRepository.getInstance().getMetadata(propertyClass);
	}
}