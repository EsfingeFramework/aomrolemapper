package org.esfinge.aom.example.persistence.neo4j;

import org.esfinge.aom.persistence.neo4j.Neo4JAOMConfiguration;
import org.junit.Assert;
import org.junit.Test;

public class Neo4JAOMConfigurationTest {

	private Neo4JAOMConfiguration configuration = new Neo4JAOMConfiguration();
	
	@Test
	public void getNodeForEntityTypeTest () {
		String nodeForType = configuration.getNodeForEntityType("savingsAccountType", "");
		Assert.assertEquals("AccountType", nodeForType);
		nodeForType = configuration.getNodeForEntityType("checkingsAccountType", "");
		Assert.assertEquals("AccountType", nodeForType);
		nodeForType = configuration.getNodeForEntityType("otherType", "");
		Assert.assertEquals("GenericType", nodeForType);
		nodeForType = configuration.getNodeForEntityType("ObservationType", "");
		Assert.assertEquals("ObservationType", nodeForType);
	}
	
	@Test
	public void getEntityTypeNodeNameTest() {
		Assert.assertEquals("EntityTypeNode", configuration.getEntityTypeNodeName());
	}
}
