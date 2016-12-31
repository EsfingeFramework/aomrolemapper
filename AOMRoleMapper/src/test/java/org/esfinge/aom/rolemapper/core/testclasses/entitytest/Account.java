package org.esfinge.aom.rolemapper.core.testclasses.entitytest;

import java.util.ArrayList;
import java.util.List;

import org.esfinge.aom.model.rolemapper.metadata.annotations.Entity;
import org.esfinge.aom.model.rolemapper.metadata.annotations.EntityProperty;
import org.esfinge.aom.model.rolemapper.metadata.annotations.EntityType;
import org.esfinge.aom.model.rolemapper.metadata.annotations.RuleMethod;

@Entity
public class Account implements IAccount {

	@EntityType
	private AccountType accountType;
	
	private String owner;
	
	@EntityProperty
	private List<AccountProperty> properties = new ArrayList<AccountProperty>();

	public AccountType getAccountType() {
		return accountType;
	}
	
	@RuleMethod // isso (anotacao) tem q virar uma anotacao no adapter
	public boolean isValid(){
		boolean ret = true;
		IAccount iAccount = accountType.createAccount();
		List<AccountProperty> properties2 = iAccount.getProperties();
		for (AccountProperty accountProperty : properties2) {
			if(accountProperty.getValue() == null){
				return false;
			}
		}
		return ret;
	}

	public void setAccountType(AccountType accountType) {
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
}
