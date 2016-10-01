package net.sf.esfinge.aom.api.model.rolemapper.metadata.reader;

import net.sf.esfinge.aom.exceptions.EsfingeAOMException;

public interface IAOMMetadataReader {

	public Object getDescriptor(Class<?> c) throws EsfingeAOMException;
	
	public boolean isReaderApplicable (Class<?> c);
	
}
