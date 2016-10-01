package net.sf.esfinge.aom.model.factories;

import net.sf.esfinge.aom.api.model.IProperty;
import net.sf.esfinge.aom.api.model.IPropertyType;
import net.sf.esfinge.aom.exceptions.EsfingeAOMException;
import net.sf.esfinge.aom.model.impl.GenericProperty;
import net.sf.esfinge.aom.model.rolemapper.core.AdapterProperty;

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
