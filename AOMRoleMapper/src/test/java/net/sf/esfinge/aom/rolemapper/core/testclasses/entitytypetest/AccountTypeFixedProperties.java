package net.sf.esfinge.aom.rolemapper.core.testclasses.entitytypetest;

import java.util.HashSet;
import java.util.Set;

import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.CreateEntityMethod;
import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.EntityType;
import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.Name;
import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.PropertyType;

@EntityType
public class AccountTypeFixedProperties implements IAccountType {

	@Name
	private String name;
	
	@PropertyType
	private Set<AccountPropertyType> propertyTypes = new HashSet<AccountPropertyType>();
	
	@CreateEntityMethod
	public Account createAccount()
	{
		Account account = new Account();
		
		account.setAccountType(this);
		return account;
	}

	/* (non-Javadoc)
	 * @see net.sf.esfinge.aom.rolemapper.core.testclasses.entitytypetest.IAccountType#getPropertyTypes()
	 */
	@Override
	public Set<AccountPropertyType> getPropertyTypes() {
		return propertyTypes;
	}

	/* (non-Javadoc)
	 * @see net.sf.esfinge.aom.rolemapper.core.testclasses.entitytypetest.IAccountType#setPropertyTypes(java.util.Set)
	 */
	@Override
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
