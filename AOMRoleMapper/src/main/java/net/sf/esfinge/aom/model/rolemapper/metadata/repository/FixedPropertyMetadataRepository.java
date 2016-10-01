package net.sf.esfinge.aom.model.rolemapper.metadata.repository;

import net.sf.esfinge.aom.api.model.rolemapper.metadata.reader.IFixedPropertyReader;
import net.sf.esfinge.aom.model.rolemapper.metadata.descriptors.FixedPropertyDescriptor;
import net.sf.esfinge.aom.model.rolemapper.metadata.reader.provider.FixedPropertyReaderProvider;

public class FixedPropertyMetadataRepository {
	
	private FixedPropertyDescriptor descriptor;
	
	private static FixedPropertyMetadataRepository instance;

	private FixedPropertyMetadataRepository()
	{
		try
		{
			IFixedPropertyReader fixedPropertyReader = FixedPropertyReaderProvider.getInstance().getMetadataReader();
			descriptor = fixedPropertyReader.getDescriptor();
		}
		catch (Exception e)
		{
			
		}
	}
	
	public static FixedPropertyMetadataRepository getInstance() {		
		if (instance == null)
			instance = new FixedPropertyMetadataRepository();
		return instance;
	}

	public FixedPropertyDescriptor getDescriptor() {
		return descriptor;
	}

}
