package net.sf.esfinge.aom.api.model.rolemapper.metadata.reader;

import net.sf.esfinge.aom.exceptions.EsfingeAOMException;
import net.sf.esfinge.aom.model.rolemapper.metadata.descriptors.FixedPropertyDescriptor;

public interface IFixedPropertyReader {

	public FixedPropertyDescriptor getDescriptor() throws EsfingeAOMException;

}