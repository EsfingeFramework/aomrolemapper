package net.sf.esfinge.aom.model.rolemapper.metadata.descriptors;

public class PropertyDescriptor {

	private FieldDescriptor propertyTypeDescriptor;
	
	private FieldDescriptor valueDescriptor;
	
	private FieldDescriptor nameDescriptor;

	public FieldDescriptor getPropertyTypeDescriptor() {
		return propertyTypeDescriptor;
	}

	public void setPropertyTypeDescriptor(
			FieldDescriptor propertyTypeDescriptor) {
		this.propertyTypeDescriptor = propertyTypeDescriptor;
	}

	public FieldDescriptor getValueDescriptor() {
		return valueDescriptor;
	}

	public void setValueDescriptor(FieldDescriptor valueDescriptor) {
		this.valueDescriptor = valueDescriptor;
	}

	public FieldDescriptor getNameDescriptor() {
		return nameDescriptor;
	}

	public void setNameDescriptor(FieldDescriptor nameDescriptor) {
		this.nameDescriptor = nameDescriptor;
	}
}
