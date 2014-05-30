package org.esfinge.aom.model.rolemapper.core;

import junit.framework.Assert;

import org.esfinge.aom.api.model.IPropertyType;
import org.esfinge.aom.exceptions.EsfingeAOMException;
import org.esfinge.aom.rolemapper.core.testclasses.propertytest.Account;
import org.esfinge.aom.rolemapper.core.testclasses.propertytest.AccountProperty;
import org.esfinge.aom.rolemapper.core.testclasses.propertytest.AccountPropertyType;
import org.esfinge.aom.rolemapper.core.testclasses.propertytest.AccountType;
import org.junit.Test;

public class AdapterPropertyTest {

	private static final String accountPropertyClass = "org.esfinge.aom.rolemapper.core.testclasses.propertytest.AccountProperty";

	@Test
	public void testConstructor() throws Exception 
	{	
		AdapterProperty property = new AdapterProperty(accountPropertyClass);
		Assert.assertTrue(property.getAssociatedObject() instanceof AccountProperty);
	}	
	
	@Test
	public void testConstructor_fromDsObject() throws Exception 
	{	
		AccountProperty accountProperty = new AccountProperty();
		AdapterProperty property = AdapterProperty.getAdapter(accountProperty);
		Assert.assertEquals(accountProperty, property.getAssociatedObject());
		
		AccountProperty accountProperty2 = new AccountProperty();
		AdapterProperty property2 = AdapterProperty.getAdapter(accountProperty2);
		Assert.assertNotSame(property, property2);
		
		AdapterProperty property3 = AdapterProperty.getAdapter(accountProperty);
		Assert.assertEquals(property, property3);
	}	
		
	@Test
	public void testGetValue() throws Exception 
	{	
		AccountProperty accountProperty = new AccountProperty();
		accountProperty.setValue(10.0);
		AdapterProperty property = AdapterProperty.getAdapter(accountProperty);
		Assert.assertEquals(10.0, property.getValue());
	}
	
	@Test
	public void testGetValue_relationshipValue() throws Exception 
	{	
		AccountProperty accountProperty = new AccountProperty();
		AccountPropertyType accountPropertyType = new AccountPropertyType();
		AccountType accountType = new AccountType();
		accountPropertyType.setPropertyType(accountType);		
		accountProperty.setPropertyType(accountPropertyType);
		
		AdapterProperty property = AdapterProperty.getAdapter(accountProperty);
		
		Account account = new Account();
		AdapterEntityType entityType = AdapterEntityType.getAdapter(accountType);
		AdapterEntity relationshipEntity = AdapterEntity.getAdapter(entityType, account);
		property.setValue(relationshipEntity);
		
		Assert.assertEquals(relationshipEntity, property.getValue());
	}
	
	@Test
	public void testSetValue_valueIsValid() throws Exception 
	{	
		AccountProperty accountProperty = new AccountProperty();
		AccountPropertyType accountPropertyType = new AccountPropertyType();
		accountPropertyType.setPropertyType(double.class);
		accountProperty.setPropertyType(accountPropertyType);
		AdapterProperty property = AdapterProperty.getAdapter(accountProperty);
		property.setValue(25.3);
		Assert.assertEquals(25.3, accountProperty.getValue());
	}
	
	@Test
	public void testSetValue_valueIsNotValid() throws Exception 
	{	
		AccountProperty accountProperty = new AccountProperty();
		AccountPropertyType accountPropertyType = new AccountPropertyType();
		accountPropertyType.setPropertyType(int.class);
		accountProperty.setPropertyType(accountPropertyType);
		AdapterProperty property = AdapterProperty.getAdapter(accountProperty);
		
		try
		{
			property.setValue(25.3);
			Assert.fail("Should not set value");
		} 
		catch (EsfingeAOMException e)
		{
		}
		catch (Exception e2)
		{
			Assert.fail("Should throw an EsfingeAOMException");
		}
	}
	
	@Test
	public void testSetValue_relationshipValue() throws Exception 
	{	
		AccountProperty accountProperty = new AccountProperty();
		AccountPropertyType accountPropertyType = new AccountPropertyType();
		AccountType accountType = new AccountType();
		accountPropertyType.setPropertyType(accountType);		
		accountProperty.setPropertyType(accountPropertyType);
		
		AdapterProperty property = AdapterProperty.getAdapter(accountProperty);
		
		Account account = new Account();
		AdapterEntityType entityType = AdapterEntityType.getAdapter(accountType);
		AdapterEntity relationshipEntity = AdapterEntity.getAdapter(entityType, account);
		property.setValue(relationshipEntity);
		
		Assert.assertEquals(account, accountProperty.getValue());
	}
	
	@Test
	public void testGetType() throws Exception 
	{	
		AccountProperty accountProperty = new AccountProperty();	
		AccountPropertyType propertyType = new AccountPropertyType();
		accountProperty.setPropertyType(propertyType);
		AdapterProperty property = AdapterProperty.getAdapter(accountProperty);
		IPropertyType genericPropertyType = property.getPropertyType();
		Assert.assertEquals(propertyType, ((AdapterPropertyType)genericPropertyType).getAssociatedObject());
	}
	
	@Test
	public void testGetType_nullType() throws Exception 
	{	
		AccountProperty accountProperty = new AccountProperty();
		AdapterProperty property = AdapterProperty.getAdapter(accountProperty);
		IPropertyType genericPropertyType = property.getPropertyType();
		Assert.assertEquals(null, genericPropertyType);
	}
	
	@Test
	public void testSetType() throws Exception 
	{	
		AccountProperty accountProperty = new AccountProperty();
		AccountPropertyType accountPropertyType = new AccountPropertyType();
		AdapterProperty property = AdapterProperty.getAdapter(accountProperty);
		AdapterPropertyType propertyType = AdapterPropertyType.getAdapter(accountPropertyType);
		property.setPropertyType(propertyType);
		Assert.assertEquals(propertyType, property.getPropertyType());
		Assert.assertEquals(accountPropertyType, accountProperty.getPropertyType());
	}
		
	@Test
	public void testSetType_changeType() throws Exception 
	{	
		AccountProperty accountProperty = new AccountProperty();
		AccountPropertyType accountPropertyType = new AccountPropertyType();
		AccountPropertyType accountPropertyType2 = new AccountPropertyType();
		AdapterProperty property = AdapterProperty.getAdapter(accountProperty);
		AdapterPropertyType propertyType = AdapterPropertyType.getAdapter(accountPropertyType);
		AdapterPropertyType propertyType2 = AdapterPropertyType.getAdapter(accountPropertyType2);
		property.setPropertyType(propertyType);
		Assert.assertEquals(accountPropertyType, accountProperty.getPropertyType());
		Assert.assertEquals(propertyType, property.getPropertyType());
		
		property.setPropertyType(propertyType2);
		Assert.assertEquals(accountPropertyType2, accountProperty.getPropertyType());
		Assert.assertEquals(propertyType2, property.getPropertyType());		
	}
	
	@Test
	public void testGetAdapter_nullDsObject() throws Exception 
	{	
		AdapterProperty property = AdapterProperty.getAdapter(null);
		Assert.assertEquals(null, property);
	}
}
