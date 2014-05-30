package org.esfinge.aom.model.rolemapper.core;

import java.lang.reflect.Field;

import org.esfinge.aom.api.model.IPropertyType;
import org.esfinge.aom.exceptions.EsfingeAOMException;
import org.esfinge.aom.utils.Utils;

public class AdapterFixedPropertyType implements IPropertyType {
	
	private Field adaptedField;
	
	public AdapterFixedPropertyType(Field adaptedField) {
		this.adaptedField = adaptedField;
	}

	@Override
	public String getName() throws EsfingeAOMException {
		return adaptedField.getName();
	}

	@Override
	public void setName(String name) throws EsfingeAOMException {
		throw new EsfingeAOMException("You can't set the name of a fixed property");
	}

	@Override
	public Object getType() throws EsfingeAOMException {
		return adaptedField.getType();
	}

	@Override
	public void setType(Object type) throws EsfingeAOMException {
		throw new EsfingeAOMException("You can't set the type of a fixed property");
	}

	@Override
	public Object getAssociatedObject() {
		return adaptedField;
	}

	@Override
	public String getTypeAsString() throws EsfingeAOMException {
		return getType().toString();
	}

	@Override
	public boolean isRelationshipProperty() throws EsfingeAOMException {
		return false;
	}

	@Override
	public boolean isValidValue(Object value) throws EsfingeAOMException {
		Class<?> classType = (Class<?>) getType();			
		return Utils.valueIsAssignable(classType, value);
	}

}
