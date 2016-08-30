package org.esfinge.aom.model.rolemapper.metadata.repository;

import org.esfinge.aom.model.rolemapper.metadata.reader.provider.PropertyTypeReaderProvider;

public class PropertyTypeMetadataRepository extends ElementMetadataRepository {
	
	private static PropertyTypeMetadataRepository instance;

	private PropertyTypeMetadataRepository()
	{
		aomMetadataReader = PropertyTypeReaderProvider.getInstance().getMetadataReader();
	}
	
	public static PropertyTypeMetadataRepository getInstance() {		
		if (instance == null)
			instance = new PropertyTypeMetadataRepository();
		return instance;
	}

}
