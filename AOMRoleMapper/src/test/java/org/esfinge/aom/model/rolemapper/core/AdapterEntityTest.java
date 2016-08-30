package org.esfinge.aom.model.rolemapper.core;

import junit.framework.Assert;

import org.esfinge.aom.api.model.IProperty;
import org.esfinge.aom.exceptions.EsfingeAOMException;
import org.esfinge.aom.model.impl.GenericEntityType;
import org.esfinge.aom.model.impl.GenericPropertyType;
import org.esfinge.aom.rolemapper.core.testclasses.entitytest.Account;
import org.esfinge.aom.rolemapper.core.testclasses.entitytest.AccountProperty;
import org.esfinge.aom.rolemapper.core.testclasses.entitytest.AccountPropertyType;
import org.esfinge.aom.rolemapper.core.testclasses.entitytest.AccountType;
import org.esfinge.aom.rolemapper.core.testclasses.entitytest.AccountWithFixedProperties;
import org.junit.Test;

public class AdapterEntityTest {

	private static final String accountClass = "org.esfinge.aom.rolemapper.core.testclasses.entitytest.Account";
	private static final String accountTypeClass = "org.esfinge.aom.rolemapper.core.testclasses.entitytest.AccountType";
	private static final String accountPropertyTypeClass = "org.esfinge.aom.rolemapper.core.testclasses.entitytest.AccountPropertyType";

	@Test
	public void testConstructor() throws Exception 
	{	
		AdapterEntityType entityType = new AdapterEntityType(accountTypeClass);
		AdapterEntity entity = new AdapterEntity(entityType, Account.class);
		Assert.assertTrue(entity.getAssociatedObject() instanceof Account);
	}	
	
	@Test
	public void testConstructor_fromDsObjectTest() throws Exception 
	{	
		Account account = new Account();
		AdapterEntityType entityType = new AdapterEntityType(accountTypeClass);
		AdapterEntity entity = AdapterEntity.getAdapter(entityType, account);
		Assert.assertEquals(account, entity.getAssociatedObject());
		
		Account account2 = new Account();
		AdapterEntity entity2 = AdapterEntity.getAdapter(entityType, account2);
		Assert.assertNotSame(entity, entity2);
		
		AdapterEntity entity3 = AdapterEntity.getAdapter(entityType, account);
		Assert.assertEquals(entity, entity3);
	}	
	
	@Test
	public void testConstructor_dsWithFixedProperties() throws Exception 
	{	
		AccountWithFixedProperties account = new AccountWithFixedProperties();
		AccountType accountType = new AccountType();
		AdapterEntityType entityType = AdapterEntityType.getAdapter(accountType);
		account.setAccountType(accountType);
		AdapterEntity entity = AdapterEntity.getAdapter(entityType, account);
		Assert.assertEquals(2, entity.getProperties().size());
		Assert.assertEquals(2, entity.getEntityType().getPropertyTypes().size());
		Assert.assertEquals(0, accountType.getPropertyTypes().size());
	}
		
	@Test
	public void testSetProperty() throws Exception 
	{	
		AccountPropertyType accountPropertyType = new AccountPropertyType();
		accountPropertyType.setName("Balance");
		accountPropertyType.setPropertyType(double.class);
		AccountType accountType = new AccountType();		
		Account account = new Account();
		accountType.addPropertyTypes(accountPropertyType);
		account.setAccountType(accountType);
		
		double value = 100000.25;
		AdapterEntityType entityType = AdapterEntityType.getAdapter(accountType);
		AdapterEntity entity = AdapterEntity.getAdapter(entityType, account);
		entity.setProperty("Balance", value);
		
		Assert.assertEquals(value, entity.getProperty("Balance").getValue());		
	}
	
	@Test
	public void testSetProperty_notInEntityType() throws Exception 
	{	
		try
		{
			AccountType accountType = new AccountType();		
			Account account = new Account();
			account.setAccountType(accountType);
			
			double value = 100000.25;
			AdapterEntityType entityType = AdapterEntityType.getAdapter(accountType);
			AdapterEntity entity = AdapterEntity.getAdapter(entityType, account);
			entity.setProperty("Balance", value);	
			Assert.fail("An EsfingeAOMException should be thrown");
		}
		catch (EsfingeAOMException e)
		{
		}
		catch (Exception e)
		{
			Assert.fail("An EsfingeAOMException should be thrown");
		}
	}

	@Test
	public void testSetProperty_fixedProperty() throws Exception 
	{	
		AccountType accountType = new AccountType();	
		AccountWithFixedProperties account = new AccountWithFixedProperties();
		account.setAccountType(accountType);
		account.setOwner("Fabio");
		AdapterEntityType entityType = new AdapterEntityType(accountTypeClass);
		AdapterEntity entity = AdapterEntity.getAdapter(entityType, account);
		entity.setProperty("owner", "Marcelo");
		Assert.assertEquals("Marcelo", account.getOwner());
	}
	
	@Test
	public void testSetGetProperty_notAdaptedProperty() throws Exception 
	{	
		AccountType accountType = new AccountType();	
		Account account = new Account();
		account.setAccountType(accountType);
		
		AdapterEntityType entityType = new AdapterEntityType(accountTypeClass);
		AdapterEntity entity = AdapterEntity.getAdapter(entityType, account);
		
		GenericPropertyType notAdaptedPropertyType = new GenericPropertyType("notAdapted", String.class);
		entityType.addPropertyType(notAdaptedPropertyType);
		
		entity.setProperty("notAdapted", "Test not adapted");
		Assert.assertEquals("Test not adapted", entity.getProperty("notAdapted").getValue());
	}
	
	@Test
	public void testSetGetProperty_relationshipProperty() throws Exception 
	{	
		AccountType accountType = new AccountType();	
		Account account = new Account();
		account.setAccountType(accountType);
		
		Account account2 = new Account();
		account2.setAccountType(accountType);
		
		AdapterEntityType entityType = new AdapterEntityType(accountTypeClass);
		AdapterEntity entity = AdapterEntity.getAdapter(entityType, account);
		
		AdapterEntity relationshipEntity = AdapterEntity.getAdapter(entityType, account2);
		
		GenericPropertyType relationshipPropertyType = new GenericPropertyType("relationship", entityType);
		entityType.addPropertyType(relationshipPropertyType);
		
		entity.setProperty("relationship", relationshipEntity);
		Assert.assertEquals(relationshipEntity, entity.getProperty("relationship").getValue());
	}
	
	@Test
	public void testRemoveProperty() throws Exception 
	{	
		AccountType accountType = new AccountType();		
		AdapterEntityType entityType = AdapterEntityType.getAdapter(accountType);
		Account account = new Account();
		account.setAccountType(accountType);
		AdapterEntity entity = AdapterEntity.getAdapter(entityType, account);
		entity.setEntityType(entityType);
		AdapterPropertyType propertyType = new AdapterPropertyType(accountPropertyTypeClass);
		propertyType.setType(double.class);
		propertyType.setName("Balance");
		entityType.addPropertyType(propertyType);
		entity.setProperty("Balance", 10000.0);
		
		entity.removeProperty("Balance");
		for (AccountProperty property : account.getProperties())
		{
			if (property.getPropertyType().getName().equals("Balance"))
			{
				Assert.fail("The adapted object should not have a removed property");
			}
		}		
	}
	
	@Test
	public void testRemoveProperty_fixedField() throws Exception 
	{	
		try
		{
			AccountType accountType = new AccountType();		
			AdapterEntityType entityType = AdapterEntityType.getAdapter(accountType);
			AccountWithFixedProperties account = new AccountWithFixedProperties();
			account.setAccountType(accountType);
			AdapterEntity entity = AdapterEntity.getAdapter(entityType, account);
			entity.setEntityType(entityType);
			entity.removeProperty("balance");
			Assert.assertTrue(false);
		}
		catch (EsfingeAOMException e)
		{
			Assert.assertEquals("This property is a fixed property and cannot be removed", e.getMessage());
		}
	}
	
	@Test
	public void testRemoveProperty_notAdaptedProperty() throws Exception 
	{	
		AccountType accountType = new AccountType();	
		Account account = new Account();
		account.setAccountType(accountType);
		
		AdapterEntityType entityType = new AdapterEntityType(accountTypeClass);
		AdapterEntity entity = AdapterEntity.getAdapter(entityType, account);
		
		GenericPropertyType notAdaptedPropertyType = new GenericPropertyType("notAdapted", String.class);
		entityType.addPropertyType(notAdaptedPropertyType);
		
		entity.setProperty("notAdapted", "Test not adapted");
		entity.removeProperty("notAdapted");
		Assert.assertEquals(null, entity.getProperty("notAdapted"));
	}
	
	@Test
	public void testRemoveProperty_relationshipProperty() throws Exception 
	{	
		AccountType accountType = new AccountType();	
		Account account = new Account();
		account.setAccountType(accountType);
		
		Account account2 = new Account();
		account2.setAccountType(accountType);
		
		AdapterEntityType entityType = new AdapterEntityType(accountTypeClass);
		AdapterEntity entity = AdapterEntity.getAdapter(entityType, account);
		
		AdapterEntity relationshipEntity = AdapterEntity.getAdapter(entityType, account2);
		
		GenericPropertyType relationshipPropertyType = new GenericPropertyType("relationship", entityType);
		entityType.addPropertyType(relationshipPropertyType);
		
		entity.setProperty("relationship", relationshipEntity);
		entity.removeProperty("relationship");
		Assert.assertEquals(null, entity.getProperty("relationship"));
	}
	
	@Test
	public void testGetProperty() throws Exception 
	{	
		AccountType accountType = new AccountType();		
		AdapterEntityType entityType = AdapterEntityType.getAdapter(accountType);
		Account account = new Account();
		account.setAccountType(accountType);
		AdapterEntity entity = AdapterEntity.getAdapter(entityType, account);
		AdapterPropertyType propertyType = new AdapterPropertyType(accountPropertyTypeClass);
		propertyType.setType(double.class);
		propertyType.setName("Balance");
		entityType.addPropertyType(propertyType);
		entity.setProperty("Balance", 10000);
		
		AdapterPropertyType propertyType1 = new AdapterPropertyType(accountPropertyTypeClass);
		propertyType1.setType(String.class);
		propertyType1.setName("OwnerName");
		entityType.addPropertyType(propertyType1);
		entity.setProperty("OwnerName", "Joe");
		
		IProperty property = entity.getProperty("OwnerName");
		Assert.assertEquals("OwnerName", property.getPropertyType().getName());
		Assert.assertEquals("Joe", property.getValue());
		Assert.assertTrue(account.getProperties().contains(((AdapterProperty)property).getAssociatedObject()));
	}
	
	@Test
	public void testGetProperty_typeNotFound() throws Exception 
	{	
		AccountType accountType = new AccountType();		
		AdapterEntityType entityType = AdapterEntityType.getAdapter(accountType);
		Account account = new Account();
		account.setAccountType(accountType);
		AdapterEntity entity = AdapterEntity.getAdapter(entityType, account);
		
		AdapterPropertyType propertyType = new AdapterPropertyType(accountPropertyTypeClass);
		propertyType.setType(String.class);
		propertyType.setName("OwnerName");
		entityType.addPropertyType(propertyType);
		
		IProperty property = entity.getProperty("OwnerName");
		Assert.assertEquals(null, property);
	}
	
	@Test
	public void testGetAdapter_nullDsObject() throws Exception 
	{	
		GenericEntityType entityType = new GenericEntityType("dummyType");
		AdapterEntity entity = AdapterEntity.getAdapter(entityType, null);
		Assert.assertEquals(null, entity);
	}
	
	@Test
	public void testJavaBeanAdapter(){
		
	}
	
}
