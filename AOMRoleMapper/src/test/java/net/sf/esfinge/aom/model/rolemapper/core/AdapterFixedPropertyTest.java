package net.sf.esfinge.aom.model.rolemapper.core;

import org.junit.Test;

import junit.framework.Assert;
import net.sf.esfinge.aom.exceptions.EsfingeAOMException;
import net.sf.esfinge.aom.model.impl.GenericPropertyType;
import net.sf.esfinge.aom.model.rolemapper.core.AdapterFixedProperty;
import net.sf.esfinge.aom.rolemapper.core.testclasses.entitytest.AccountWithFixedProperties;

public class AdapterFixedPropertyTest {
	
	@Test
	public void testSetValue() throws Exception 
	{	
		AccountWithFixedProperties account = new AccountWithFixedProperties();
		GenericPropertyType propertyType = new GenericPropertyType("owner", String.class);
		AdapterFixedProperty property = new AdapterFixedProperty(account, propertyType);
		property.setValue("Marcelo");
		Assert.assertEquals("Marcelo", account.getOwner());
	}	
	
	@SuppressWarnings("deprecation")
	@Test
	public void testSetValue_invalidValue() throws Exception 
	{	
		AccountWithFixedProperties account = new AccountWithFixedProperties();
		GenericPropertyType propertyType = new GenericPropertyType("owner", String.class);
		AdapterFixedProperty property = new AdapterFixedProperty(account, propertyType);
		try
		{
			property.setValue(1);
			Assert.fail();
		}
		catch (EsfingeAOMException e)
		{
			Assert.assertEquals(e.getMessage(), "The given value 1 is not valid for type owner");
		}		
	}
	
	@Test
	public void testGetValue() throws Exception 
	{	
		AccountWithFixedProperties account = new AccountWithFixedProperties();
		account.setOwner("Fabio");
		GenericPropertyType propertyType = new GenericPropertyType("owner", String.class);
		AdapterFixedProperty property = new AdapterFixedProperty(account, propertyType);
		Assert.assertEquals("Fabio", property.getValue());
		Assert.assertEquals(account, property.getAssociatedObject());
	}	
	
	@Test
	public void testSetPropertyType() throws Exception 
	{	
		AccountWithFixedProperties account = new AccountWithFixedProperties();
		GenericPropertyType propertyType = new GenericPropertyType("owner", String.class);
		GenericPropertyType propertyType1 = new GenericPropertyType("balance", double.class);
		AdapterFixedProperty property = new AdapterFixedProperty(account, propertyType);		
		try
		{
			property.setPropertyType(propertyType1);
			Assert.fail();
		}
		catch (EsfingeAOMException e)
		{
			Assert.assertEquals("Cannot change property type", e.getMessage());
		}		
	}	
		
}
