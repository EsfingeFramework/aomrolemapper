package org.esfinge.aom.model.rolemapper.metadata.repository;

import org.esfinge.aom.model.rolemapper.metadata.reader.provider.EntityReaderProvider;

public class EntityMetadataRepository extends ElementMetadataRepository {
	
	private static EntityMetadataRepository instance;

	private EntityMetadataRepository()
	{
		aomMetadataReader = EntityReaderProvider.getInstance().getMetadataReader();
	}
	
	public static EntityMetadataRepository getInstance() {
		
		if (instance == null)
			instance = new EntityMetadataRepository();
		return instance;
	}
	
}
