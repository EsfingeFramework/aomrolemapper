package org.esfinge.aom.api.model.rolemapper.metadata.reader;

import org.esfinge.aom.exceptions.EsfingeAOMException;

public interface IAOMMetadataReader {

	public Object getDescriptor(Class<?> c) throws EsfingeAOMException;
	
	public boolean isReaderApplicable (Class<?> c);
	
}
