package org.esfinge.aom.rolemapper.core.testclasses.entitytest;

import java.util.List;

public interface IAccount {

	public abstract AccountType getAccountType();

	public abstract void setAccountType(AccountType accountType);

	public abstract List<AccountProperty> getProperties();

	public abstract void setProperties(List<AccountProperty> properties);

}