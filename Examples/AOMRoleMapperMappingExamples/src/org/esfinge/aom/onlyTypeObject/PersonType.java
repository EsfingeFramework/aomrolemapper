package org.esfinge.aom.onlyTypeObject;

import org.esfinge.aom.model.rolemapper.metadata.annotations.EntityType;
import org.esfinge.aom.model.rolemapper.metadata.annotations.Name;

@EntityType
public class PersonType {
	
	@Name
	private String typeName;

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

}
