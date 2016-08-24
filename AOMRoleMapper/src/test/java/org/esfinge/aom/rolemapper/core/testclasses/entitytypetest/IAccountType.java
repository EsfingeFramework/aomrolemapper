package org.esfinge.aom.rolemapper.core.testclasses.entitytypetest;

import java.util.Set;

public interface IAccountType {

	public String getName(); 
	
	public void setName (String name);

	public Set<AccountPropertyType> getPropertyTypes();

	public void setPropertyTypes(Set<AccountPropertyType> propertyTypes);

}