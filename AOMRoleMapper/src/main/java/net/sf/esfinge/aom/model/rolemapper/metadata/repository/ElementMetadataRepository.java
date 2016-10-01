package net.sf.esfinge.aom.model.rolemapper.metadata.repository;

import java.util.HashMap;
import java.util.Map;

import net.sf.esfinge.aom.api.model.rolemapper.metadata.reader.IAOMMetadataReader;
import net.sf.esfinge.aom.exceptions.EsfingeAOMException;

public abstract class ElementMetadataRepository {

	protected IAOMMetadataReader aomMetadataReader;
	
	protected Map<Class<?>, Object> cache = new HashMap<Class<?>, Object>();
	
	public Object getMetadata (Class<?> c) throws EsfingeAOMException
	{
		if (cache.containsKey(c))
		{
			return cache.get(c);
		}
		
		Object descriptor = aomMetadataReader.getDescriptor(c);		
		cache.put(c, descriptor);		
		return descriptor;
	}
	
	public boolean isReaderApplicable(Class<?> c) {	
		return aomMetadataReader.isReaderApplicable(c);
	}
}
