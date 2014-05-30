package org.esfinge.aom.simpletypesquare;

import org.esfinge.aom.model.rolemapper.metadata.annotations.EntityProperties;
import org.esfinge.aom.model.rolemapper.metadata.annotations.PropertyType;
import org.esfinge.aom.model.rolemapper.metadata.annotations.PropertyValue;

@EntityProperties
public class Information {

	public Information(InformationType type, Object info) {
		this.type = type;
		this.info = info;
	}
	
	public Information() {}

	@PropertyType
	private InformationType type;
	
	@PropertyValue
	private Object info;

	public InformationType getType() {
		return type;
	}

	public void setType(InformationType type) {
		this.type = type;
	}

	public Object getInfo() {
		return info;
	}

	public void setInfo(Object info) {
		this.info = info;
	}
	
}
