package org.esfinge.aom.model.rolemapper.metadata.reader.provider;

import org.esfinge.aom.model.rolemapper.metadata.reader.PropertyAnnotationReader;

public class PropertyReaderProvider extends ElementReaderProvider {
		
	private static PropertyReaderProvider instance;

	public static PropertyReaderProvider getInstance() {
		
		if (instance == null)
			instance = new PropertyReaderProvider();
		return instance;
	}
	
	private PropertyReaderProvider()
	{		
		metadataReader = new PropertyAnnotationReader();
	}

}
