package org.esfinge.aom.model.rolemapper.metadata.reader.provider;

import org.esfinge.aom.api.model.rolemapper.metadata.reader.IAOMMetadataReader;

public abstract class ElementReaderProvider {

	protected IAOMMetadataReader metadataReader;

	public IAOMMetadataReader getMetadataReader() {
		return metadataReader;
	}

	public void setMetadataReader(IAOMMetadataReader metadataReader) {
		this.metadataReader = metadataReader;
	}	
	
}
