package net.sf.esfinge.aom.model.rolemapper.metadata.reader.provider;

import net.sf.esfinge.aom.model.rolemapper.metadata.reader.PropertyAnnotationReader;

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
