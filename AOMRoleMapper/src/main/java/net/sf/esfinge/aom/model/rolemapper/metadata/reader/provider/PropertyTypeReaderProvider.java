package net.sf.esfinge.aom.model.rolemapper.metadata.reader.provider;

import net.sf.esfinge.aom.model.rolemapper.metadata.reader.PropertyTypeAnnotationReader;

public class PropertyTypeReaderProvider extends ElementReaderProvider {
		
	private static PropertyTypeReaderProvider instance;

	public static PropertyTypeReaderProvider getInstance() {
		
		if (instance == null)
			instance = new PropertyTypeReaderProvider();
		return instance;
	}
	
	private PropertyTypeReaderProvider()
	{		
		metadataReader = new PropertyTypeAnnotationReader();
	}

}
