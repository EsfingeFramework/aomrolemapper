package net.sf.esfinge.aom.simpletypesquare;

import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.Name;
import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.PropertyType;
import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.PropertyTypeType;

@PropertyType
public class InformationType {
	
	public InformationType(String name, Object type) {
		super();
		this.name = name;
		this.type = type;
	}
	
	public InformationType() {
		super();
	}

	@Name
	private String name;
	
	@PropertyTypeType
	private Object type;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getType() {
		return type;
	}

	public void setType(Object type) {
		this.type = type;
	}
}
