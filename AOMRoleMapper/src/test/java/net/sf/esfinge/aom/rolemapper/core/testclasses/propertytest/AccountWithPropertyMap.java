package net.sf.esfinge.aom.rolemapper.core.testclasses.propertytest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.Entity;
import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.EntityPropertyMap;
import net.sf.esfinge.aom.rolemapper.core.testclasses.entitytest.AccountProperty;
import net.sf.esfinge.aom.rolemapper.core.testclasses.entitytest.AccountType;
import net.sf.esfinge.aom.rolemapper.core.testclasses.entitytest.IAccount;

@Entity
public class AccountWithPropertyMap implements IAccount {
	
	@EntityPropertyMap
	private Map<String, Object> propertiesMap = new HashMap<String, Object>();

	public Map<String, Object> getPropertiesMap() {
		return propertiesMap;
	}

	public void setPropertiesMap(Map<String, Object> propertiesMap) {
		this.propertiesMap = propertiesMap;
	}

	@Override
	public AccountType getAccountType() {
		return null;
	}

	@Override
	public void setAccountType(AccountType accountType) {
		
	}

	@Override
	public List<AccountProperty> getProperties() {
		return null;
	}

	@Override
	public void setProperties(List<AccountProperty> properties) {
		
	}
}
