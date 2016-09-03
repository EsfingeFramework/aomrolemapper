package org.esfinge.aom.onlyPropertyList;

import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.EntityProperty;
import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.Name;
import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.PropertyValue;

@EntityProperty
public class FuncionarioInfo {

	public FuncionarioInfo(String name, Object info) {
		this.name = name;
		this.info = info;
	}

	public FuncionarioInfo(){}
	
	@Name
	private String name;
	
	@PropertyValue
	private Object info;

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
