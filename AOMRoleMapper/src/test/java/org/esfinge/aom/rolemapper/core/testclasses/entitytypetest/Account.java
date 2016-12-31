package org.esfinge.aom.rolemapper.core.testclasses.entitytypetest;

import java.util.ArrayList;
import java.util.List;

import org.esfinge.aom.model.rolemapper.metadata.annotations.Entity;
import org.esfinge.aom.model.rolemapper.metadata.annotations.EntityProperty;
import org.esfinge.aom.model.rolemapper.metadata.annotations.EntityType;
import org.esfinge.aom.model.rolemapper.metadata.annotations.RuleMethod;

@Entity
public class Account {

	@EntityType
	private IAccountType accountType;
	
	private String owner;
	
	@EntityProperty
	private List<AccountProperty> properties = new ArrayList<AccountProperty>();

	public IAccountType getAccountType() {
		return accountType;
	}

	public void setAccountType(IAccountType accountType) {
		this.accountType = accountType;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public List<AccountProperty> getProperties() {
		return properties;
	}

	public void setProperties(List<AccountProperty> properties) {
		this.properties = properties;
	}
	
	public void addProperties (AccountProperty property)
	{
		this.properties.add(property);
	}
	
	public void removeProperties (AccountProperty property)
	{
		this.properties.remove(property);
	}
	
	@RuleMethod
	public void testeRule(){
		System.out.println(" teste regra ");
	}
}
