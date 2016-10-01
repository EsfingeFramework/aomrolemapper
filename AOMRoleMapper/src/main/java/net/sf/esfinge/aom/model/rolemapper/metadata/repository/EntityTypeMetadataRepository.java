package net.sf.esfinge.aom.model.rolemapper.metadata.repository;

import net.sf.esfinge.aom.model.rolemapper.metadata.reader.provider.EntityTypeReaderProvider;

public class EntityTypeMetadataRepository extends ElementMetadataRepository {
	
	private static EntityTypeMetadataRepository instance;

	private EntityTypeMetadataRepository()
	{
		aomMetadataReader = EntityTypeReaderProvider.getInstance().getMetadataReader();
	}
	
	public static EntityTypeMetadataRepository getInstance() {
		
		if (instance == null)
			instance = new EntityTypeMetadataRepository();
		return instance;
	}

}
