package net.sf.esfinge.aom.rolemapper.core.testclasses.propertytypetest;

import java.util.HashMap;
import java.util.Map;

import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.MetadataMap;
import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.PropertyType;

@PropertyType
public class AccountPropertyTypeWithMetadataMap {
	
	@MetadataMap
	private Map<String, Object> metadatas = new HashMap<String, Object>();

	public Map<String, Object> getMetadatas() {
		return metadatas;
	}

	public void setMetadatas(Map<String, Object> metadatas) {
		this.metadatas = metadatas;
	}
}