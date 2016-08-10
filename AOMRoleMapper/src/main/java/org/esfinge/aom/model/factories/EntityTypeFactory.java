package org.esfinge.aom.model.factories;

import org.esfinge.aom.api.model.IEntityType;
import org.esfinge.aom.exceptions.EsfingeAOMException;
import org.esfinge.aom.model.impl.GenericEntityType;
import org.esfinge.aom.model.rolemapper.core.AdapterEntityType;

public class EntityTypeFactory {

	public static IEntityType createEntityType (Object dsObject) throws EsfingeAOMException
	{
		return AdapterEntityType.getAdapter(dsObject);
	}
	
	public static IEntityType createEntityType (String packageName, String name, String associatedClass) throws EsfingeAOMException, ClassNotFoundException
	{
		if (associatedClass != null)
		{
			IEntityType type = new AdapterEntityType(associatedClass);
			type.setName(name);
			if (packageName != null)
			{
				type.setPackageName(packageName);
			}
			return type;
		}
		return createEntityType(packageName, name);
	}
	
	public static IEntityType createEntityType (String packageName, String name, Class<?> associatedClass) throws EsfingeAOMException
	{
		if (associatedClass != null)
		{
			IEntityType type = new AdapterEntityType(associatedClass);
			type.setName(name);
			if (packageName != null)
			{
				type.setPackageName(packageName);
			}
			return type;
		}
		return createEntityType(packageName, name);
	}
	
	public static IEntityType createEntityType (String packageName, String name) throws EsfingeAOMException
	{
		return new GenericEntityType(packageName, name);
	}
}
