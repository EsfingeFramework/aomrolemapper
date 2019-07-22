package org.esfinge.aom.api.model;

import java.util.Map;

import org.esfinge.aom.exceptions.EsfingeAOMException;

public interface RuleObject extends HasProperties {
	
	public Object execute(IEntity obj, Object... params) throws EsfingeAOMException;	
	public void setOperationProperties(Map<String, Object> operationProperties);
	public Map<String, Object> getOperationProperties();

}
