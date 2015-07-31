package org.esfinge.aom.model.impl;

import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.api.model.IEntityType;


public class GenericEntity extends ThingWithProperties implements IEntity {
	
	private IEntityType entityType;
	
	protected GenericEntity () {
	}
	
	@Override
	public IEntityType getEntityType() {
		return entityType;
	}

	@Override
	public void setEntityType(IEntityType entityType) {
		this.entityType = entityType; 
	}
	
	@Override
	public Object getAssociatedObject() {
		return null;
	}
}
