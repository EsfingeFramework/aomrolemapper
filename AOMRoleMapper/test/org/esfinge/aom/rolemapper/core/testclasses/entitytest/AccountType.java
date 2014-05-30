package org.esfinge.aom.rolemapper.core.testclasses.entitytest;

import java.util.HashSet;
import java.util.Set;

import org.esfinge.aom.model.rolemapper.metadata.annotations.CreateEntityMethod;
import org.esfinge.aom.model.rolemapper.metadata.annotations.EntityType;
import org.esfinge.aom.model.rolemapper.metadata.annotations.PropertyType;

@EntityType
public class AccountType {

	
	@PropertyType
	private Set<AccountPropertyType> propertyTypes = new HashSet<AccountPropertyType>();

	public enum AccountTypeToCreate 
	{
		Account,
		AccountWithFixedProperties
	};
	
	private AccountTypeToCreate typeToCreate = AccountTypeToCreate.Account;
	
	@CreateEntityMethod
	public IAccount createAccount()
	{
		IAccount account = null;
		
		switch (typeToCreate)
		{
		case Account:
			account = new Account();
			break;
			
		case AccountWithFixedProperties:
			account = new AccountWithFixedProperties();
			break;
		}		
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

	public void setTypeToCreate(AccountTypeToCreate typeToCreate) {
		this.typeToCreate = typeToCreate;
	}

}
