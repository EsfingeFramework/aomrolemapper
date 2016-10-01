package net.sf.esfinge.aom.rolemapper.core.testclasses.entitytest;

import java.util.ArrayList;
import java.util.List;

import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.Entity;
import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.EntityProperty;
import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.EntityType;

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
