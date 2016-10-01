package net.sf.esfinge.aom.model.rolemapper.metadata.reader;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.scannotation.AnnotationDB;
import org.scannotation.ClasspathUrlFinder;

import net.sf.esfinge.aom.api.model.rolemapper.metadata.reader.IFixedPropertyReader;
import net.sf.esfinge.aom.exceptions.EsfingeAOMException;
import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.Entity;
import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.EntityType;
import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.FixedEntityProperty;
import net.sf.esfinge.aom.model.rolemapper.metadata.descriptors.FixedPropertyDescriptor;

public class FixedPropertyAnnotationReader implements IFixedPropertyReader {

	@Override
	public FixedPropertyDescriptor getDescriptor () throws EsfingeAOMException
	{
		try
		{
			FixedPropertyDescriptor descriptor = new FixedPropertyDescriptor();
			URL[] urls = ClasspathUrlFinder.findClassPaths(); 
			AnnotationDB db = new AnnotationDB();
			db.setScanFieldAnnotations(true);
			db.setScanClassAnnotations(false);
			db.setScanMethodAnnotations(false);
			db.setScanParameterAnnotations(false);			
			db.scanArchives(urls);
			Map<String, Set<String>> annotations = db.getAnnotationIndex();
			for (String clazz : annotations.get(FixedEntityProperty.class.getName()))
			{
				Class<?> c = Class.forName(clazz);				
				if (c.isAnnotationPresent(Entity.class))
				{
					Class<?> type = null;
					List<Field> fixedProperties = new ArrayList<Field>();
					for (Field f : c.getDeclaredFields())
					{
						if (f.isAnnotationPresent(EntityType.class))
						{
							type = f.getType();
						}
						else if (f.isAnnotationPresent(FixedEntityProperty.class))
						{
							fixedProperties.add(f);
						}
					}
					if (type != null && fixedProperties.size() > 0)
					{
						descriptor.addFixedProperty(type, fixedProperties);
					}
				}
			}
			return descriptor;
		}
		catch (Exception e)
		{
			throw new EsfingeAOMException(e);
		}
	}
	
}
