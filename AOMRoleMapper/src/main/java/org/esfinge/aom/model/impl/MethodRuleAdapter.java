package org.esfinge.aom.model.impl;

import java.lang.reflect.Method;

import org.esfinge.aom.api.model.IEntity;

public class MethodRuleAdapter extends BasicRuleObject {
	
	Method method;
	Object ientity;
	
	public MethodRuleAdapter(Method method){
		this.method = method;
	}

	public MethodRuleAdapter(Method method, Object entity) {
		this.method = method;
		this.ientity = entity;
	}

	@Override
	public Object execute(IEntity obj, Object... params) {
		try {
			return method.invoke(ientity, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
