package org.esfinge.aom.example.bankingsystem.accounts;

import java.util.HashSet;
import java.util.Set;

import org.esfinge.aom.model.rolemapper.metadata.annotations.CreateEntityMethod;
import org.esfinge.aom.model.rolemapper.metadata.annotations.EntityType;
import org.esfinge.aom.model.rolemapper.metadata.annotations.Name;
import org.esfinge.aom.model.rolemapper.metadata.annotations.PropertyType;

@EntityType
public class AccountType {
	
	@Name
	private String accountTypeIdentification;
	
	@PropertyType
	private Set<AccountPropertyType> propertyTypes = new HashSet<AccountPropertyType>();
		
	@CreateEntityMethod
	public Account createAccount()
	{
		Account account = new Account();
				
		account.setAccountType(this);
		return account;
	}
	
	public Set<AccountPropertyType> getPropertyTypes() {
		return propertyTypes;
	}

	public void setPropertyTypes(Set<AccountPropertyType> propertyTypes) {
		this.propertyTypes = propertyTypes;
	}
	
	public void addPropertyTypes (AccountPropertyType propertyType)
	{
		propertyTypes.add(propertyType);
	}
	
	public void removePropertyTypes (AccountPropertyType propertyType)
	{
		propertyTypes.remove(propertyType);
	}

	public String getAccountTypeIdentification() {
		return accountTypeIdentification;
	}

	public void setAccountTypeIdentification(String accountTypeIdentification) {
		this.accountTypeIdentification = accountTypeIdentification;
	}

}
