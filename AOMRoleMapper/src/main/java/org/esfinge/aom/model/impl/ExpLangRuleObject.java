package org.esfinge.aom.model.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.api.model.IProperty;
import org.esfinge.aom.api.model.RuleObject;
import org.esfinge.aom.exceptions.EsfingeAOMException;
import org.esfinge.aom.rule.ELContextAOM;

public class ExpLangRuleObject implements RuleObject {

	String el;
	private Map<String, Object> operationProperties = new LinkedHashMap<>();
	private Map<String, Object> mapValues = new HashMap<>();

	public Map<String, Object> getMapValues() {
		return mapValues;
	}

	public void setMapValues(Map<String, Object> mapValues) {
		this.mapValues = mapValues;
	}

	public ExpLangRuleObject(String expression) {
		el = expression;
	}

	@Override
	public Object execute(IEntity obj, Object... params) throws EsfingeAOMException {
		Map<String, Object> mapa = new HashMap<>(getMapValues());

		try {
			List<IProperty> properties = obj.getProperties();

			for (IProperty iProperty : properties) {
				String name = iProperty.getName();
				Object value = iProperty.getValue();
				if (value != null) {
					mapa.put(name, value);
				}
			}

			Object result = ELContextAOM.execute(el, obj.getClass(), mapa);
			return result;
		} catch (Exception e) {
			return -1;
		}
	}

	@Override
	public Map<String, Object> getOperationProperties() {
		return operationProperties;
	}

	@Override
	public void setOperationProperties(Map<String, Object> operationProperties) {
		this.operationProperties = operationProperties;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<IProperty> getProperties() throws EsfingeAOMException {
		return Collections.EMPTY_LIST;
	}

	@Override
	public void setProperty(String propertyName, Object propertyValue) throws EsfingeAOMException {

	}

	@Override
	public void removeProperty(String propertyName) throws EsfingeAOMException {

	}

	@Override
	public IProperty getProperty(String propertyName) throws EsfingeAOMException {
		return null;
	}
}
