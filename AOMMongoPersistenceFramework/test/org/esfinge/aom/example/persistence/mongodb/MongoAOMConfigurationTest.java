package org.esfinge.aom.example.persistence.mongodb;

import junit.framework.Assert;

import org.esfinge.aom.persistence.mongodb.MongoAOMConfiguration;
import org.junit.Test;



public class MongoAOMConfigurationTest {

	private MongoAOMConfiguration configuration = new MongoAOMConfiguration();
	
	@Test
	public void getCollectionForEntityTypeTest ()
	{
		String collectionForType = configuration.getCollectionForEntityType("savingsAccountType", "");
		Assert.assertEquals("AccountType", collectionForType);
		collectionForType = configuration.getCollectionForEntityType("checkingsAccountType", "");
		Assert.assertEquals("AccountType", collectionForType);
		collectionForType = configuration.getCollectionForEntityType("otherType", "");
		Assert.assertEquals("GenericType", collectionForType);
		collectionForType = configuration.getCollectionForEntityType("ObservationType", "");
		Assert.assertEquals("ObservationType", collectionForType);
	}
	
	@Test
	public void getEntityTypeCollectionNameTest()
	{
		Assert.assertEquals("EntityTypeCollection", configuration.getEntityTypeCollectionName());
	}
}
