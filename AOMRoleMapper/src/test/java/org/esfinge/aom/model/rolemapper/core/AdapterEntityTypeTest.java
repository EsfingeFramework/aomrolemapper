package org.esfinge.aom.model.rolemapper.core;

import junit.framework.Assert;

import java.util.List;

import org.esfinge.aom.api.model.HasProperties;
import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.api.model.IPropertyType;
import org.esfinge.aom.exceptions.EsfingeAOMException;
import org.esfinge.aom.model.impl.GenericPropertyType;
import org.esfinge.aom.model.impl.MethodRuleAdapter;
import org.esfinge.aom.rolemapper.core.testclasses.entitytypetest.Account;
import org.esfinge.aom.rolemapper.core.testclasses.entitytypetest.AccountPropertyType;
import org.esfinge.aom.rolemapper.core.testclasses.entitytypetest.AccountType;
import org.esfinge.aom.rolemapper.core.testclasses.entitytypetest.AccountTypeFixedProperties;
import org.esfinge.aom.rolemapper.core.testclasses.entitytypetest.AccountTypeWithMetadataMap;
import org.esfinge.aom.rolemapper.core.testclasses.entitytypetest.AccountTypeWithoutAnnotations;
import org.esfinge.aom.rolemapper.core.testclasses.entitytypetest.IAccountType;
import org.esfinge.aom.rolemapper.core.testclasses.entitytypetest.MetadatasAccountType;
import org.esfinge.aom.rolemapper.core.testclasses.entitytypetest.SimpleAccountType;
import org.esfinge.aom.utils.ObjectPrinter;
import org.junit.Test;

public class AdapterEntityTypeTest {

	private static final String accountTypeClass = "org.esfinge.aom.rolemapper.core.testclasses.entitytypetest.AccountType";
	
	@Test
	public void testConstructor() throws Exception 
	{	
		AdapterEntityType entityType = new AdapterEntityType(accountTypeClass);
		Assert.assertTrue(entityType.getAssociatedObject() instanceof AccountType);
	}
	
	@Test
	public void testConstructor2() throws Exception 
	{	
		AdapterEntityType entityType = new AdapterEntityType(accountTypeClass);
		List<MethodRuleAdapter> fixedRules = entityType.getFixedRules();
		Assert.assertTrue(fixedRules.size() == 0);
	}

	@Test
	public void testConstructor3() throws Exception 
	{	
		AdapterEntityType entityType = new AdapterEntityType(accountTypeClass);
		List<MethodRuleAdapter> fixedRules = entityType.getFixedRules();
		Assert.assertTrue(fixedRules.size() == 0);
	}
	
	
	@Test
	public void testConstructor_fromDsObject() throws Exception 
	{	
		AccountType accountType = new AccountType();
		AdapterEntityType entityType = AdapterEntityType.getAdapter(accountType);
		Assert.assertEquals(accountType, entityType.getAssociatedObject());
		
		AccountType accountType2 = new AccountType();
		AdapterEntityType entityType2 = AdapterEntityType.getAdapter(accountType2);
		Assert.assertNotSame(entityType, entityType2);
		
		AdapterEntityType entityType3 = AdapterEntityType.getAdapter(accountType);
		Assert.assertEquals(entityType, entityType3);
	}	
	
	@Test
	public void testConstructor_fixedProperties() throws Exception 
	{	
		AccountTypeFixedProperties accountType = new AccountTypeFixedProperties();
		AdapterEntityType entityType = AdapterEntityType.getAdapter(accountType);
		Assert.assertNotNull(entityType.getPropertyType("owner"));
		Assert.assertNotNull(entityType.getPropertyType("balance"));
	}	
	
	@Test
	public void testNewCreateEntity_setDefaultValueToNumberProperty() throws Exception 
	{	
		AccountType accountType = new AccountType();
		AccountPropertyType accountPropertyType = new AccountPropertyType();
		accountPropertyType.setName("numberType");
		accountPropertyType.setPropertyType(float.class);
		accountType.addPropertyTypes(accountPropertyType);
		
		AdapterEntityType entityType = AdapterEntityType.getAdapter(accountType);
		HasProperties entity = entityType.createNewEntity();
		Assert.assertEquals(0, entity.getProperty("numberType").getValue());
	}
	
	@Test
	public void testNewCreateEntity_setDefaultValueToCharacterProperty() throws Exception 
	{	
		AccountType accountType = new AccountType();
		AccountPropertyType accountPropertyType = new AccountPropertyType();
		accountPropertyType.setName("charType");
		accountPropertyType.setPropertyType(char.class);
		accountType.addPropertyTypes(accountPropertyType);
		
		AdapterEntityType entityType = AdapterEntityType.getAdapter(accountType);
		HasProperties entity = entityType.createNewEntity();
		Assert.assertEquals((char)0, entity.getProperty("charType").getValue());
	}
	
	@Test
	public void testNewCreateEntity_setDefaultValueToBooleanProperty() throws Exception 
	{	
		AccountType accountType = new AccountType();
		AccountPropertyType accountPropertyType = new AccountPropertyType();
		accountPropertyType.setName("booleanType");
		accountPropertyType.setPropertyType(boolean.class);
		accountType.addPropertyTypes(accountPropertyType);
		
		AdapterEntityType entityType = AdapterEntityType.getAdapter(accountType);
		HasProperties entity = entityType.createNewEntity();
		Assert.assertFalse((Boolean)entity.getProperty("booleanType").getValue());
	}
	
	@Test
	public void testNewCreateEntity_setDefaultValueToObjectProperty() throws Exception 
	{	
		AccountType accountType = new AccountType();
		AccountPropertyType accountPropertyType = new AccountPropertyType();
		accountPropertyType.setName("objectType");
		accountPropertyType.setPropertyType(String.class);
		accountType.addPropertyTypes(accountPropertyType);
		
		AdapterEntityType entityType = AdapterEntityType.getAdapter(accountType);
		HasProperties entity = entityType.createNewEntity();
		Assert.assertEquals(null, entity.getProperty("objectType").getValue());
	}
	
	@Test
	public void testNewCreateEntity_setDefaultValueToRelationshipProperty() throws Exception 
	{	
		AccountType accountType = new AccountType();
		AccountPropertyType accountPropertyType = new AccountPropertyType();
		accountPropertyType.setName("relationshipType");
		accountPropertyType.setPropertyType(accountType);
		accountType.addPropertyTypes(accountPropertyType);
		
		AdapterEntityType entityType = AdapterEntityType.getAdapter(accountType);
		HasProperties entity = entityType.createNewEntity();
		Assert.assertEquals(null, entity.getProperty("relationshipType").getValue());
	}
	
	@Test
	public void testNewCreateEntity_noAnnotatedMethod() throws Exception 
	{	
		IAccountType accountType = new AccountTypeWithoutAnnotations();
		
		AdapterEntityType entityType = AdapterEntityType.getAdapter(accountType);		
		try
		{
			entityType.createNewEntity();
			Assert.fail();
		}
		catch (EsfingeAOMException e)
		{
			Assert.assertEquals("Could not create new entity because no creation method was found", e.getMessage());
		}
	}
	
	@Test
	public void testNewCreateEntity() throws Exception 
	{	
		AccountType accountType = new AccountType();
		
		AdapterEntityType entityType = AdapterEntityType.getAdapter(accountType);
		IEntity entity = entityType.createNewEntity();
		Assert.assertTrue(entity.getAssociatedObject() instanceof Account);
		Account account = (Account)entity.getAssociatedObject();
		Assert.assertEquals(accountType, account.getAccountType());
		Assert.assertEquals(entityType, entity.getEntityType());		
	}
	
	@Test
	public void testGetAdapter_nullDsObject() throws Exception 
	{	
		AdapterEntityType entityType = AdapterEntityType.getAdapter(null);
		Assert.assertEquals(null, entityType);
	}
	
	@Test
	public void testGetPropertyTypes() throws Exception 
	{	
		AccountType accountType = new AccountType();
		AccountPropertyType accountPropertyType = new AccountPropertyType();		
		AccountPropertyType accountPropertyType2 = new AccountPropertyType();
		accountType.addPropertyTypes(accountPropertyType);
		accountType.addPropertyTypes(accountPropertyType2);
		
		AdapterEntityType entityType = AdapterEntityType.getAdapter(accountType);
		Assert.assertEquals(2, entityType.getPropertyTypes().size());
		
		for (IPropertyType propertyType : entityType.getPropertyTypes())
		{
			if (!propertyType.getAssociatedObject().equals(accountPropertyType) && 
					!propertyType.getAssociatedObject().equals(accountPropertyType2))
			{
				Assert.fail();
			}
		}
	}
	
	@Test
	public void testGetPropertyTypes_withFixedPropertyTypes() throws Exception 
	{	
		AccountTypeFixedProperties accountType = new AccountTypeFixedProperties();
		AccountPropertyType accountPropertyType = new AccountPropertyType();		
		AccountPropertyType accountPropertyType2 = new AccountPropertyType();
		accountType.addPropertyTypes(accountPropertyType);
		accountType.addPropertyTypes(accountPropertyType2);
		
		AdapterEntityType entityType = AdapterEntityType.getAdapter(accountType);
		Assert.assertEquals(4, entityType.getPropertyTypes().size());
	}
	
	@Test
	public void testGetPropertyType() throws Exception 
	{	
		AccountType accountType = new AccountType();
		AccountPropertyType accountPropertyType = new AccountPropertyType();
		accountPropertyType.setName("propertyType");
		accountPropertyType.setPropertyType(String.class);		
		accountType.addPropertyTypes(accountPropertyType);
		
		AdapterEntityType entityType = AdapterEntityType.getAdapter(accountType);
		IPropertyType propertyType = entityType.getPropertyType("propertyType");
		Assert.assertEquals(accountPropertyType, propertyType.getAssociatedObject());
	}
	
	@Test
	public void testGetPropertyType_notFound() throws Exception 
	{	
		AccountType accountType = new AccountType();
		AccountPropertyType accountPropertyType = new AccountPropertyType();
		accountPropertyType.setName("propertyType");
		accountPropertyType.setPropertyType(String.class);		
		accountType.addPropertyTypes(accountPropertyType);
		
		AdapterEntityType entityType = AdapterEntityType.getAdapter(accountType);
		IPropertyType propertyType = entityType.getPropertyType("propertyType2");
		Assert.assertEquals(null, propertyType);
	}
	
	@Test
	public void testRemovePropertyType() throws Exception 
	{	
		AccountType accountType = new AccountType();
		AccountPropertyType accountPropertyType = new AccountPropertyType();
		accountPropertyType.setName("propertyType");
		accountPropertyType.setPropertyType(String.class);		
		accountType.addPropertyTypes(accountPropertyType);
		
		AdapterEntityType entityType = AdapterEntityType.getAdapter(accountType);
		entityType.removePropertyType("propertyType");
		Assert.assertEquals(0, accountType.getPropertyTypes().size());
	}
	
	@Test
	public void testRemovePropertyType_notFound() throws Exception 
	{	
		AccountType accountType = new AccountType();
		AccountPropertyType accountPropertyType = new AccountPropertyType();
		accountPropertyType.setName("propertyType");
		accountPropertyType.setPropertyType(String.class);		
		accountType.addPropertyTypes(accountPropertyType);
		
		AdapterEntityType entityType = AdapterEntityType.getAdapter(accountType);
		entityType.removePropertyType("propertyType2");
		Assert.assertEquals(1, accountType.getPropertyTypes().size());
	}
	
	@Test
	public void testRemovePropertyType_notAdaptedPropertyType() throws Exception 
	{	
		AccountType accountType = new AccountType();

		GenericPropertyType accountPropertyType = new GenericPropertyType("propertyType", String.class);		
		AdapterEntityType entityType = AdapterEntityType.getAdapter(accountType);
		entityType.addPropertyType(accountPropertyType);
		entityType.removePropertyType("propertyType");
		Assert.assertEquals(0, accountType.getPropertyTypes().size());
	}
	
	@Test
	public void testRemovePropertyType_noDsRemovePropertyTypeMethod() throws Exception 
	{	
		SimpleAccountType accountType = new SimpleAccountType();
		AccountPropertyType accountPropertyType = new AccountPropertyType();
		accountPropertyType.setName("propertyType");
		accountPropertyType.setPropertyType(String.class);		
		
		AdapterPropertyType propertyType = AdapterPropertyType.getAdapter(accountPropertyType);		
		AdapterEntityType entityType = AdapterEntityType.getAdapter(accountType);
		entityType.addPropertyType(propertyType);
		entityType.removePropertyType("propertyType");
		Assert.assertEquals(0, accountType.getPropertyTypes().size());
	}
	
	@Test
	public void testAddPropertyType() throws Exception 
	{	
		AccountType accountType = new AccountType();
		AccountPropertyType accountPropertyType = new AccountPropertyType();
		accountPropertyType.setName("propertyType");
		accountPropertyType.setPropertyType(String.class);		
		
		AdapterPropertyType propertyType = AdapterPropertyType.getAdapter(accountPropertyType);		
		AdapterEntityType entityType = AdapterEntityType.getAdapter(accountType);
		entityType.addPropertyType(propertyType);
		Assert.assertEquals(propertyType, entityType.getPropertyType("propertyType"));
	}
	
	@Test
	public void testAddPropertyType_notAdaptedPropertyType() throws Exception 
	{	
		AccountType accountType = new AccountType();

		GenericPropertyType accountPropertyType = new GenericPropertyType("propertyType", String.class);		
		AdapterEntityType entityType = AdapterEntityType.getAdapter(accountType);
		entityType.addPropertyType(accountPropertyType);
		Assert.assertEquals(AccountPropertyType.class, entityType.getPropertyType("propertyType").getAssociatedObject().getClass());
		Assert.assertEquals("propertyType", entityType.getPropertyType("propertyType").getName());
		Assert.assertEquals(String.class, entityType.getPropertyType("propertyType").getType());
		
	}
	
	@Test
	public void testAddPropertyType_noDsAddPropertyTypeMethod() throws Exception 
	{	
		SimpleAccountType accountType = new SimpleAccountType();
		AccountPropertyType accountPropertyType = new AccountPropertyType();
		accountPropertyType.setName("propertyType");
		accountPropertyType.setPropertyType(String.class);		
		
		AdapterPropertyType propertyType = AdapterPropertyType.getAdapter(accountPropertyType);		
		AdapterEntityType entityType = AdapterEntityType.getAdapter(accountType);
		entityType.addPropertyType(propertyType);
		Assert.assertEquals(propertyType, entityType.getPropertyType("propertyType"));
	}
	
	@Test
	public void testSetName() throws Exception 
	{	
		AccountType accountType = new AccountType();
		
		AdapterEntityType entityType = AdapterEntityType.getAdapter(accountType);
		entityType.setName("entityTypeName");
		Assert.assertEquals("entityTypeName", accountType.getName());
	}
	
	@Test
	public void testSetName_noNameMetadata() throws Exception 
	{	
		AccountTypeWithoutAnnotations accountType = new AccountTypeWithoutAnnotations();
		
		AdapterEntityType entityType = AdapterEntityType.getAdapter(accountType);
		try
		{
			entityType.setName("entityTypeName");
			Assert.fail();
		}
		catch (EsfingeAOMException e)
		{
			Assert.assertEquals("Metadata \"Name\" not found in entity type", e.getMessage());
		}
	}
	
	@Test
	public void testGetName() throws Exception 
	{	
		AccountType accountType = new AccountType();
		accountType.setName("accountTypeName");
		
		AdapterEntityType entityType = AdapterEntityType.getAdapter(accountType);
		Assert.assertEquals("accountTypeName", entityType.getName());
	}
	
	@Test
	public void testGetMetadata_getNullMetadata() throws Exception{	
		AccountType accountType = new AccountType();
		AdapterEntityType adapterType = AdapterEntityType.getAdapter(accountType);
		Assert.assertEquals(null, adapterType.getProperty(""));
	}
	
	@Test
	public void testGetMetadata_getFixedMetadata() throws Exception{
		AccountType accountType = new AccountType();
		AdapterEntityType adapterAccountType = AdapterEntityType.getAdapter(accountType);
		Assert.assertEquals(false, adapterAccountType.getProperty("persist").getValue());
		Assert.assertEquals("account_type", adapterAccountType.getProperty("description").getValue());
	}	
	
	@Test
	public void testGetMetadata_objectMetadata() throws Exception{
		AccountType accountType = new AccountType();
		
		accountType.getMetadatas().add(new MetadatasAccountType("configuration1", new Integer(10)));
		AdapterEntityType adapterAccountType = AdapterEntityType.getAdapter(accountType);
		Assert.assertEquals(10, adapterAccountType.getProperty("configuration1").getValue());

		accountType.getMetadatas().add(new MetadatasAccountType("nome", "Fusca"));
		AdapterEntityType adapterAccountType3 = AdapterEntityType.getAdapter(accountType);
		Assert.assertEquals("Fusca", adapterAccountType3.getProperty("nome").getValue());

		accountType.getMetadatas().add(new MetadatasAccountType("configuration2", new Boolean(true)));
		AdapterEntityType adapterAccountType2 = AdapterEntityType.getAdapter(accountType);		
		Assert.assertEquals(true, adapterAccountType2.getProperty("configuration2").getValue());
	}
	
	@Test
	public void testGetMetadata_MetadataMap() throws Exception{
		AccountTypeWithMetadataMap accountType = new AccountTypeWithMetadataMap();
		
		accountType.getMetadatas().put("configuration1", new Integer(10));
		AdapterPropertyType adapterAccountType = AdapterPropertyType.getAdapter(accountType);
		Assert.assertEquals(10, adapterAccountType.getProperty("configuration1").getValue());
		ObjectPrinter.printClass(adapterAccountType);
	}
	
	@Test
	public void testAddPropertyType_rule() throws Exception 
	{	
		AccountType accountType = new AccountType();
		AdapterEntityType entityType = AdapterEntityType.getAdapter(accountType);	
		IEntity iEntity = entityType.createNewEntity();
		Object executeOperation = iEntity.executeOperation("fixedRule");
		ObjectPrinter.printClass(iEntity);
		System.out.println("resultado " + executeOperation);
	}
}