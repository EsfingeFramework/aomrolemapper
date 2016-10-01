package net.sf.esfinge.aom.model.rolemapper.metadata.descriptors;

import java.lang.reflect.Method;

public class EntityTypeDescriptor extends MetadataDescriptor{

	private FieldDescriptor entitiesDescriptor;
	
	private FieldDescriptor propertyTypesDescriptor;
	
	private FieldDescriptor nameDescriptor;
	
	private Method createEntityMethod;

	public FieldDescriptor getEntitiesDescriptor() {
		return entitiesDescriptor;
	}

	public void setEntitiesDescriptor(FieldDescriptor entitiesDescriptor) {
		this.entitiesDescriptor = entitiesDescriptor;
	}

	public FieldDescriptor getPropertyTypesDescriptor() {
		return propertyTypesDescriptor;
	}

	public void setPropertyTypesDescriptor(
			FieldDescriptor propertyTypesDescriptor) {
		this.propertyTypesDescriptor = propertyTypesDescriptor;
	}

	public Method getCreateEntityMethod() {
		return createEntityMethod;
	}

	public void setCreateEntityMethod(Method createEntityMethod) {
		this.createEntityMethod = createEntityMethod;
	}

	public FieldDescriptor getNameDescriptor() {
		return nameDescriptor;
	}

	public void setNameDescriptor(FieldDescriptor nameDescriptor) {
		this.nameDescriptor = nameDescriptor;
	}
}
