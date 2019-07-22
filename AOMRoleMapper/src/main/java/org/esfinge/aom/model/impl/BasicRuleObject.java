package org.esfinge.aom.model.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.api.model.IEntityType;
import org.esfinge.aom.api.model.RuleObject;
import org.esfinge.aom.exceptions.EsfingeAOMException;

public abstract class BasicRuleObject extends ThingWithProperties implements RuleObject {

	private IEntityType entityType;
	private Map<String, Object> operationProperties = new LinkedHashMap<>();

	public void setEntityType(IEntityType entityType) {
		this.entityType = entityType;
	}

	@Override
	public abstract Object execute(IEntity obj, Object... params) throws EsfingeAOMException;

	@Override
	public IEntityType getEntityType() {
		return entityType;
	}
	
	@Override
	public Map<String, Object> getOperationProperties() {
		return operationProperties;
	}

	@Override
	public void setOperationProperties(Map<String, Object> operationProperties) {
		this.operationProperties = operationProperties;
	}

}
