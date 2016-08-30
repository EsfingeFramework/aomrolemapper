package org.esfinge.aom.model.rolemapper.core;

import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.rolemapper.core.testclasses.propertytest.AccountWithPropertyMap;
import org.junit.After;
import org.junit.Test;

import junit.framework.Assert;

public class AdapterPropertyMapTest {
	
	@After
	public void cleanTest(){
		AdapterEntity.resetAdapters();
	}
	
	@Test
	public void testAdapterConstructor_fromDsObject() throws Exception 
	{	
		AccountWithPropertyMap account = new AccountWithPropertyMap();
		IEntity entity = AdapterEntity.getAdapter(null, account);
		Assert.assertEquals(account, entity.getAssociatedObject());
		
		AccountWithPropertyMap account2 = new AccountWithPropertyMap();
		IEntity entity2 = AdapterEntity.getAdapter(null, account2);
		Assert.assertEquals(account2, entity2.getAssociatedObject());
		
		IEntity entity3 = AdapterEntity.getAdapter(null, account);
		Assert.assertEquals(entity, entity3);
	}	
	
	@Test
	public void testGetValues() throws Exception 
	{
		AccountWithPropertyMap account = new AccountWithPropertyMap();
		String value = "conta corrente";
		IEntity entity = AdapterEntity.getAdapter(null, account);
		entity.setProperty("tipo", value);
		Assert.assertEquals(value, entity.getProperty("tipo").getValue());	
	}	
	
	@Test
	public void testSetValues() throws Exception 
	{	
		AccountWithPropertyMap account = new AccountWithPropertyMap();
		account.getPropertiesMap().put("tipo", "conta corrente");
		IEntity entity = AdapterEntity.getAdapter(null, account);
		entity.setProperty("tipo", "conta universitaria");
		Assert.assertEquals("conta universitaria", entity.getProperty("tipo").getValue());	
	}
	
	@Test
	public void testSetValues_changeValueInEntity() throws Exception 
	{	
		AccountWithPropertyMap account = new AccountWithPropertyMap();
		account.getPropertiesMap().put("tipo", "conta corrente");
		IEntity entity = AdapterEntity.getAdapter(null, account);
		entity.setProperty("tipo", "conta universitaria");
		Assert.assertEquals(account.getPropertiesMap().get("tipo"), entity.getProperty("tipo").getValue());	
	}
	
	@Test
	public void testSetValue_insertNewProperty() throws Exception 
	{	
		AccountWithPropertyMap account = new AccountWithPropertyMap();
		account.getPropertiesMap().put("tipo", "conta corrente");
		IEntity entity = AdapterEntity.getAdapter(null, account);
		entity.setProperty("valor", 500);
		Assert.assertTrue(account.getPropertiesMap().containsKey("valor"));	
		Assert.assertEquals(500, account.getPropertiesMap().get("valor"));
		Assert.assertEquals(account.getPropertiesMap().get("valor"), entity.getProperty("valor").getValue());	
	}
	
	@Test
	public void testRemoveProperty() throws Exception 
	{	
		AccountWithPropertyMap account = new AccountWithPropertyMap();
		account.getPropertiesMap().put("tipo", "conta corrente");
		IEntity entity = AdapterEntity.getAdapter(null, account);
		entity.removeProperty("tipo");
		Assert.assertEquals(null, entity.getProperty("tipo"));	
	}
}