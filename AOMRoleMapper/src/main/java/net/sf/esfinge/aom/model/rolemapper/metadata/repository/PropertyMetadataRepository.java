package net.sf.esfinge.aom.model.rolemapper.metadata.repository;

import net.sf.esfinge.aom.model.rolemapper.metadata.reader.provider.PropertyReaderProvider;

public class PropertyMetadataRepository extends ElementMetadataRepository {
	
	private static PropertyMetadataRepository instance;

	private PropertyMetadataRepository()
	{
		aomMetadataReader = PropertyReaderProvider.getInstance().getMetadataReader();
	}
	
	public static PropertyMetadataRepository getInstance() {		
		if (instance == null)
			instance = new PropertyMetadataRepository();
		return instance;
	}

}
