package org.esfinge.aom.model.rolemapper.metadata.reader.provider;

import org.esfinge.aom.model.rolemapper.metadata.reader.EntityTypeAnnotationReader;

public class EntityTypeReaderProvider extends ElementReaderProvider {
		
	private static EntityTypeReaderProvider instance;

	public static EntityTypeReaderProvider getInstance() {
		
		if (instance == null)
			instance = new EntityTypeReaderProvider();
		return instance;
	}
	
	private EntityTypeReaderProvider()
	{		
		metadataReader = new EntityTypeAnnotationReader();
	}
}
