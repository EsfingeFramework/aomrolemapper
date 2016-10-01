package net.sf.esfinge.aom.model.rolemapper.core;

import java.lang.reflect.Field;
import java.util.List;

import net.sf.esfinge.aom.api.model.IProperty;
import net.sf.esfinge.aom.api.model.IPropertyType;
import net.sf.esfinge.aom.exceptions.EsfingeAOMException;
import net.sf.esfinge.aom.utils.Utils;

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

	@Override
	public List<IProperty> getProperties() throws EsfingeAOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setProperty(String propertyName, Object propertyValue)
			throws EsfingeAOMException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeProperty(String propertyName) throws EsfingeAOMException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IProperty getProperty(String propertyName)
			throws EsfingeAOMException {
		// TODO Auto-generated method stub
		return null;
	}
}
