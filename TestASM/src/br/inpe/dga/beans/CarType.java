package br.inpe.dga.beans;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.api.model.IEntityType;
import org.esfinge.aom.api.model.IProperty;
import org.esfinge.aom.api.model.IPropertyType;
import org.esfinge.aom.exceptions.EsfingeAOMException;
import org.esfinge.aom.model.rolemapper.metadata.annotations.CreateEntityMethod;
import org.esfinge.aom.model.rolemapper.metadata.annotations.EntityType;
import org.esfinge.aom.model.rolemapper.metadata.annotations.FixedMetadata;
import org.esfinge.aom.model.rolemapper.metadata.annotations.Metadata;
import org.esfinge.aom.model.rolemapper.metadata.annotations.Name;
import org.esfinge.aom.model.rolemapper.metadata.annotations.PropertyType;
import org.esfinge.aom.rolemapper.core.testclasses.propertytypetest.MetadatasAccountPropertyType;

@EntityType
public class CarType {
	@Name
	private String name;
	
	@FixedMetadata
	private boolean persist = false;
	
	@FixedMetadata
	private String description = "car_type";

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
