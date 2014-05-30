package org.esfinge.aom.rolemapper.core.testclasses.entitytypetest;

import java.util.ArrayList;
import java.util.List;

import org.esfinge.aom.model.rolemapper.metadata.annotations.Entity;
import org.esfinge.aom.model.rolemapper.metadata.annotations.EntityProperties;
import org.esfinge.aom.model.rolemapper.metadata.annotations.EntityProperty;
import org.esfinge.aom.model.rolemapper.metadata.annotations.EntityType;

@Entity
public class AccountWithFixedProperties {

	@EntityType
	private AccountTypeFixedProperties accountType;
	
	@EntityProperty
	private String owner;
	
	@EntityProperty
	private double balance;
	
	@EntityProperties
	private List<AccountProperty> properties = new ArrayList<AccountProperty>();

	public AccountTypeFixedProperties getAccountType() {
		return accountType;
	}

	public void setAccountType(AccountTypeFixedProperties accountType) {
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

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
}
