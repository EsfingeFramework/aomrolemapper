package org.esfinge.aom.model.dynamic.adapted;

import org.esfinge.aom.model.rolemapper.metadata.annotations.EntityProperty;
import org.esfinge.aom.model.rolemapper.metadata.annotations.PropertyType;
import org.esfinge.aom.model.rolemapper.metadata.annotations.PropertyValue;

@EntityProperty
public class SensorProperty {

	@PropertyType
	private SensorPropertyType propertyType;
	
	@PropertyValue
	private Object value;

	public SensorPropertyType getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(SensorPropertyType propertyType) {
		this.propertyType = propertyType;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
}
