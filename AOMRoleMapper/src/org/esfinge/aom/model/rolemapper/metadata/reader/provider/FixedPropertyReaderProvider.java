package org.esfinge.aom.model.rolemapper.metadata.reader.provider;

import org.esfinge.aom.api.model.rolemapper.metadata.reader.IFixedPropertyReader;
import org.esfinge.aom.model.rolemapper.metadata.reader.FixedPropertyAnnotationReader;

public class FixedPropertyReaderProvider {
		
	private static FixedPropertyReaderProvider instance;

	protected IFixedPropertyReader metadataReader;

	public IFixedPropertyReader getMetadataReader() {
		return metadataReader;
	}

	public void setMetadataReader(IFixedPropertyReader metadataReader) {
		this.metadataReader = metadataReader;
	}	
		
	public static FixedPropertyReaderProvider getInstance() {
		
		if (instance == null)
			instance = new FixedPropertyReaderProvider();
		return instance;
	}
	
	private FixedPropertyReaderProvider()
	{		
		metadataReader = new FixedPropertyAnnotationReader();
	}
	
}
