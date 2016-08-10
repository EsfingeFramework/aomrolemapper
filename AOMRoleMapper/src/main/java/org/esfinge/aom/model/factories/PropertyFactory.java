package org.esfinge.aom.model.factories;

import org.esfinge.aom.api.model.IProperty;
import org.esfinge.aom.api.model.IPropertyType;
import org.esfinge.aom.exceptions.EsfingeAOMException;
import org.esfinge.aom.model.impl.GenericProperty;
import org.esfinge.aom.model.rolemapper.core.AdapterProperty;

public class PropertyFactory {

	public static IProperty createProperty (Object dsObject) throws EsfingeAOMException
	{
		return AdapterProperty.getAdapter(dsObject);
	}
	
	public static IProperty createProperty (IPropertyType propertyType, Object value, String associatedClass) throws EsfingeAOMException
	{
		if (associatedClass != null)
		{
			IProperty property = new AdapterProperty(associatedClass);
			property.setPropertyType(propertyType);
			property.setValue(value);
			return property;
		}
		return createProperty(propertyType, value);
	}
	
	public static IProperty createProperty (IPropertyType propertyType, Object value) throws EsfingeAOMException
	{
		return new GenericProperty(propertyType, value);
	}
	
}
