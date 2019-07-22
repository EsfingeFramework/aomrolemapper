package org.esfinge.aom.api.model;

import org.esfinge.aom.exceptions.EsfingeAOMException;

public interface ELRuleObject extends RuleObject {
	public Object execute(IEntity obj, Object... params) throws EsfingeAOMException;	
}
