package org.esfinge.aom.api.model.rolemapper.metadata.reader;

import org.esfinge.aom.exceptions.EsfingeAOMException;
import org.esfinge.aom.model.rolemapper.metadata.descriptors.FixedPropertyDescriptor;

public interface IFixedPropertyReader {

	public FixedPropertyDescriptor getDescriptor() throws EsfingeAOMException;

}