package org.esfinge.aom.model.rolemapper.metadata.descriptors;

import java.util.ArrayList;
import java.util.List;

public class PropertyTypeDescriptor extends MetadataDescriptor{

	private FieldDescriptor nameDescriptor;
	
	private FieldDescriptor typeDescriptor;
	
	private FieldDescriptor propertiesDescriptor;
	
	public FieldDescriptor getNameDescriptor() {
		return nameDescriptor;
	}

	public void setNameDescriptor(FieldDescriptor nameDescriptor) {
		this.nameDescriptor = nameDescriptor;
	}

	public FieldDescriptor getPropertiesDescriptor() {
		return propertiesDescriptor;
	}

	public void setPropertiesDescriptor(FieldDescriptor propertiesDescriptor) {
		this.propertiesDescriptor = propertiesDescriptor;
	}

	public FieldDescriptor getTypeDescriptor() {
		return typeDescriptor;
	}

	public void setTypeDescriptor(FieldDescriptor typeDescriptor) {
		this.typeDescriptor = typeDescriptor;
	}	
}