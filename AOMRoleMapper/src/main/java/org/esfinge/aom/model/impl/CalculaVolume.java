package org.esfinge.aom.model.impl;

import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.api.model.IProperty;
import org.esfinge.aom.api.model.RuleObject;
import org.esfinge.aom.exceptions.EsfingeAOMException;

public class CalculaVolume implements RuleObject {

	String paramName;

	public CalculaVolume(String paramName) {
		this.paramName = paramName;
	}

	@Override
	public Object execute(IEntity obj, Object... params) {
		IProperty property;
		try {
			property = obj.getProperty(paramName);
			GenericProperty gn = (GenericProperty) property;
			Object value = gn.getValue();
			if (params != null) {
				Double altura = 0d;
				if (params.length == 1) {
					if (params[0] instanceof Double) {
						altura = (Double) params[0];
					} else if (params[0] instanceof String) {
						altura = Double.parseDouble((String) params[0]);
					}
				}
				Double raio = 0d;
				if (value instanceof Double) {
					raio = (Double) value;
				} else if (value instanceof String) {
					raio = Double.parseDouble((String) value);
				}
				double volume = Math.PI * raio * raio * altura;
				return volume + " cm3";
			}
		} catch (EsfingeAOMException e) {
			e.printStackTrace();
		}
		return -1;
	}

}
