package br.inpe.dga.beans;


import java.util.List;

import org.esfinge.aom.api.model.IProperty;
import org.esfinge.aom.api.model.IPropertyType;
import org.esfinge.aom.exceptions.EsfingeAOMException;
import org.esfinge.aom.model.rolemapper.metadata.annotations.FixedMetadata;
import org.esfinge.aom.model.rolemapper.metadata.annotations.Name;
import org.esfinge.aom.model.rolemapper.metadata.annotations.PropertyType;

@PropertyType
public class CarPropertyType{
	@Name		
	private String name;
	
	@FixedMetadata
	private boolean persist = true;
	
	@FixedMetadata
	private String description = "car_property";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isPersist() {
		return persist;
	}

	public void setPersist(boolean persist) {
		this.persist = persist;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
