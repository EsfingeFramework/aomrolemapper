package net.sf.esfinge.aom.rolemapper.core.testclasses.entitytypetest;

import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.Name;
import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.PropertyType;
import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.PropertyValue;

@PropertyType
public class MetadatasAccountType {
	
	@Name
	private String name;
	
	@PropertyValue
	private Object info;

	public MetadatasAccountType(String name, Object info){
		setName(name);
		setInfo(info);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getInfo() {
		return info;
	}

	public void setInfo(Object info) {
		this.info = info;
	}
}