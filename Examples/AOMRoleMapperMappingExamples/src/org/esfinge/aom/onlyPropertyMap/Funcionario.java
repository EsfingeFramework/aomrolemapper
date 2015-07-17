package org.esfinge.aom.onlyPropertyMap;

import java.util.HashMap;
import java.util.Map;

import org.esfinge.aom.model.rolemapper.metadata.annotations.Entity;
import org.esfinge.aom.model.rolemapper.metadata.annotations.PropertyMap;

@Entity
public class Funcionario {
	
	@PropertyMap
	private Map<String, Object> informations = new HashMap<>();

	public Map<String, Object> getInformations() {
		return informations;
	}

	public void setInformations(Map<String, Object> informations) {
		this.informations = informations;
	}

}
