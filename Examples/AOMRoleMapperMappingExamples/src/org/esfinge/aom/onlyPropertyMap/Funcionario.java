package org.esfinge.aom.onlyPropertyMap;

import java.util.HashMap;
import java.util.Map;

import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.Entity;
import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.EntityPropertyMap;

@Entity
public class Funcionario {
	
	@EntityPropertyMap
	private Map<String, Object> informations = new HashMap<>();

	public Map<String, Object> getInformations() {
		return informations;
	}

	public void setInformations(Map<String, Object> informations) {
		this.informations = informations;
	}

}
