package org.esfinge.aom.model.rolemapper.metadata.descriptors;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class FixedPropertyDescriptor {

	private Map<Class<?>, List<Field>> fixedPropertiesPerClass = new WeakHashMap<Class<?>, List<Field>>();
	
	public void addFixedProperty (Class<?> entityTypeClass, List<Field> properties)
	{
		for (Field f : properties)
		{
			addFixedProperty(entityTypeClass, f);
		}
	}
	
	public void addFixedProperty (Class<?> entityTypeClass, Field property)
	{
		if (!fixedPropertiesPerClass.containsKey(entityTypeClass))
		{
			fixedPropertiesPerClass.put(entityTypeClass, new ArrayList<Field>());
		}
		List<Field> properties = fixedPropertiesPerClass.get(entityTypeClass);
		if (!properties.contains(property))
		{
			properties.add(property);
		}
	}
	
	public List<Field> getFixedProperties(Class<?> entityTypeClass)
	{
		return fixedPropertiesPerClass.get(entityTypeClass);
	}
	
}
