package net.sf.esfinge.aom.model.factories;

import net.sf.esfinge.aom.api.model.IPropertyType;
import net.sf.esfinge.aom.exceptions.EsfingeAOMException;
import net.sf.esfinge.aom.model.impl.GenericPropertyType;
import net.sf.esfinge.aom.model.rolemapper.core.AdapterPropertyType;

public class PropertyTypeFactory {

	public static IPropertyType createPropertyType (Object dsObject) throws EsfingeAOMException
	{
		return AdapterPropertyType.getAdapter(dsObject);
	}
	
	public static IPropertyType createPropertyType (String name, Object type, String associatedClass) throws EsfingeAOMException
	{
		if (associatedClass != null)
		{
			IPropertyType propertyType = new AdapterPropertyType(associatedClass);
			propertyType.setName(name);
			propertyType.setType(type);
			return propertyType;
		}
		return createPropertyType(name, type);
	}
	
	public static IPropertyType createPropertyType (String name, Object type) throws EsfingeAOMException
	{
		return new GenericPropertyType(name, type);
	}	
}
