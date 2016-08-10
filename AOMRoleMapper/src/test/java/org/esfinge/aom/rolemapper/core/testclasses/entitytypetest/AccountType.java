package org.esfinge.aom.rolemapper.core.testclasses.entitytypetest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.esfinge.aom.api.model.IPropertyType;
import org.esfinge.aom.model.rolemapper.metadata.annotations.CreateEntityMethod;
import org.esfinge.aom.model.rolemapper.metadata.annotations.EntityType;
import org.esfinge.aom.model.rolemapper.metadata.annotations.FixedMetadata;
import org.esfinge.aom.model.rolemapper.metadata.annotations.Metadata;
import org.esfinge.aom.model.rolemapper.metadata.annotations.Name;
import org.esfinge.aom.model.rolemapper.metadata.annotations.PropertyType;
import org.esfinge.aom.rolemapper.core.testclasses.propertytypetest.MetadatasAccountPropertyType;

@EntityType
public class AccountType implements IAccountType {

	@Name
	private String name;
	
	@PropertyType
	private Set<AccountPropertyType> propertyTypes = new HashSet<AccountPropertyType>();
	
	@FixedMetadata
	private boolean persist = false;
	
	@FixedMetadata
	private String description = "account_type";
	
	@Metadata
	private List<MetadatasAccountType> metadatas = new ArrayList<MetadatasAccountType>();
	
	@CreateEntityMethod
	public Account createAccount()
	{
		Account account = new Account();
		
		account.setAccountType(this);
		return account;
	}

	/* (non-Javadoc)
	 * @see org.esfinge.aom.rolemapper.core.testclasses.entitytypetest.IAccountType#getPropertyTypes()
	 */
	@Override
	public Set<AccountPropertyType> getPropertyTypes() {
		return propertyTypes;
	}

	/* (non-Javadoc)
	 * @see org.esfinge.aom.rolemapper.core.testclasses.entitytypetest.IAccountType#setPropertyTypes(java.util.Set)
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
	
	public boolean isPersist() {
		return persist;
	}

	public void setPersist(boolean persist) {
		this.persist = persist;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<MetadatasAccountType> getMetadatas() {
		return metadatas;
	}

	public void setMetadatas(List<MetadatasAccountType> metadatas) {
		this.metadatas = metadatas;
	}
}
