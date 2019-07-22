package org.esfinge.aom.model.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.api.model.IEntityType;
import org.esfinge.aom.api.model.IProperty;
import org.esfinge.aom.api.model.IPropertyType;
import org.esfinge.aom.api.model.RuleObject;
import org.esfinge.aom.exceptions.EsfingeAOMException;

public class GenericEntity extends ThingWithProperties implements IEntity {

	private IEntityType entityType;

	private Map<IPropertyType, IProperty> properties = new WeakHashMap<IPropertyType, IProperty>();
	private Map<String, String> propertiesMonitored = new WeakHashMap<String, String>();
	private Map<String, Object> ruleResult = new WeakHashMap<String, Object>();

	protected GenericEntity() {
	}

	public void addPropertyMonitored(String propertyName, String ruleName) {
		propertiesMonitored.put(propertyName, ruleName);
	}

	@Override
	public List<IProperty> getProperties() throws EsfingeAOMException {

		ArrayList<IProperty> result = new ArrayList<IProperty>();

		List<IPropertyType> validPropertyTypes = getEntityType().getPropertyTypes();
		List<IPropertyType> invaliPropertyTypes = new ArrayList<IPropertyType>();

		for (IProperty property : properties.values()) {
			IPropertyType propertyType = property.getPropertyType();
			if (validPropertyTypes.contains(propertyType)) {
				result.add(property);
			} else {
				invaliPropertyTypes.add(propertyType);
			}
		}

		for (IPropertyType propertyType : invaliPropertyTypes) {
			properties.remove(propertyType);
		}

		return result;
	}

	@Override
	public void setProperty(String propertyName, Object propertyValue) throws EsfingeAOMException {
		if (properties.containsKey(propertyName)) {
			properties.get(propertyName).setValue(propertyValue);
		} else {
			IPropertyType propertyType = getEntityType().getPropertyType(propertyName);
			properties.put(propertyType, new GenericProperty(propertyType, propertyValue));
		}
		if (propertiesMonitored.containsKey(propertyName)) {
			System.out.println("valor mudou executando regra");
			String ruleName = propertiesMonitored.get(propertyName);
			Object resultOperation = executeOperation(ruleName);
			ruleResult.put(ruleName, resultOperation);
			System.out.println("valor mudou resultado regra " + resultOperation);
		}
	}

	@Override
	public void removeProperty(String propertyName) throws EsfingeAOMException {

		IPropertyType propertyType = getEntityType().getPropertyType(propertyName);

		if (propertyType != null) {
			properties.remove(propertyType);
		}
	}

	@Override
	public IProperty getProperty(String propertyName) throws EsfingeAOMException {
		IPropertyType propertyType = getEntityType().getPropertyType(propertyName);

		if (propertyType != null) {
			return properties.get(propertyType);
		}

		return null;
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

	@Override
	public Object executeOperation(String name, Object... params) throws EsfingeAOMException {
		RuleObject operation = this.getEntityType().getOperation(name);
		return operation.execute(this, params);
	}

	@Override
	public Object getResultOperation(String ruleName) {
		return ruleResult.get(ruleName);
	}

//	public Object executeEL(String name, Object... params) {
//		try {
//			RuleObject operation = this.getEntityType().getOperation(name);
//			return operation.execute(this, params);
//		} catch (Exception e) {
//			return -1;
//		}
//	}
}
