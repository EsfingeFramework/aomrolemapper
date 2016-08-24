package org.esfinge.aom.rolemapper.core.testclasses.entitytypetest;

import java.util.HashSet;
import java.util.Set;

import org.esfinge.aom.model.rolemapper.metadata.annotations.EntityType;
import org.esfinge.aom.model.rolemapper.metadata.annotations.Name;
import org.esfinge.aom.model.rolemapper.metadata.annotations.PropertyType;

@EntityType
public class SimpleAccountType implements IAccountType {

	@Name
	private String name;
	
	@PropertyType
	private Set<AccountPropertyType> propertyTypes = new HashSet<AccountPropertyType>();


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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
