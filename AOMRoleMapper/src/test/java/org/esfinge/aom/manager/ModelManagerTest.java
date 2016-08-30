package org.esfinge.aom.manager;

import java.util.List;

import org.esfinge.aom.api.model.HasProperties;
import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.api.model.IEntityType;
import org.esfinge.aom.api.model.IPropertyType;
import org.esfinge.aom.manager.fake.FakeModelRetriever;
import org.esfinge.aom.model.impl.GenericEntityType;
import org.esfinge.aom.model.impl.GenericPropertyType;
import org.junit.After;
import org.junit.Test;

import junit.framework.Assert;

public class ModelManagerTest {

	private ModelManager manager;
	
	private FakeModelRetriever modelRetriever;
	
	public ModelManagerTest()
	{
		manager = ModelManager.getInstance();
		modelRetriever = (FakeModelRetriever)manager.getModelRetriever();
	}
	
	@After
	public void cleanModelManagerTest()
	{
		modelRetriever.resetAttributes();
		manager.resetAttributes();
	}
	
	@Test
	public void testLoadModel() throws Exception 
	{
		GenericEntityType entityType1 = new GenericEntityType("entityType1");
		GenericEntityType entityType2 = new GenericEntityType("entityType2");
		GenericEntityType entityType3 = new GenericEntityType("entityType3");

		modelRetriever.save(entityType1);
		modelRetriever.save(entityType2);
		modelRetriever.save(entityType3);
		
		List<IEntityType> model = manager.loadModel();
		Assert.assertEquals(3, model.size());
		Assert.assertTrue(model.contains(entityType1));	
		Assert.assertTrue(model.contains(entityType2));
		Assert.assertTrue(model.contains(entityType3));
	}
	
	@Test
	public void testGetEntitiesForType() throws Exception 
	{
		IEntityType entityType1 = new GenericEntityType("entityType1");
		entityType1.addPropertyType(new GenericPropertyType("id", Object.class));
		IEntityType entityType2 = new GenericEntityType("entityType2");
		entityType2.addPropertyType(new GenericPropertyType("id", Object.class));
		IEntity entity1 = entityType1.createNewEntity();
		entity1.setProperty("id", 1);
		IEntity entity2 = entityType1.createNewEntity();
		entity2.setProperty("id", 2);
		IEntity entity3 = entityType2.createNewEntity();
		entity3.setProperty("id", 3);
		IEntity entity4 = entityType1.createNewEntity();
		entity4.setProperty("id", 4);		
		modelRetriever.save(entity1);
		modelRetriever.save(entity2);
		modelRetriever.save(entity3);
		modelRetriever.save(entity4);
		
		List<IEntity> entitiesForType = manager.getEntitiesForType(entityType1);
		Assert.assertEquals(3, entitiesForType.size());
		Assert.assertTrue(entitiesForType.contains(entity1));
		Assert.assertTrue(entitiesForType.contains(entity2));
		Assert.assertTrue(entitiesForType.contains(entity4));		
	}
		
	@Test
	public void testSave_EntityType() throws Exception 
	{
		IEntityType entityType1 = new GenericEntityType("entityType1");
		
		manager.save(entityType1);
		Assert.assertTrue(modelRetriever.isSavedEntityType());
		Assert.assertEquals(1, modelRetriever.getEntityTypeMap().size());
		Assert.assertTrue(modelRetriever.getEntityTypeMap().containsValue(entityType1));	
	}
	
	@Test
	public void testSave_Entity() throws Exception 
	{
		IEntityType entityType1 = new GenericEntityType("entityType1");
		entityType1.addPropertyType(new GenericPropertyType("id", Object.class));
		IEntity entity1 = entityType1.createNewEntity();
		entity1.setProperty("id", 1);
		
		manager.save(entity1);
		Assert.assertTrue(modelRetriever.isSavedEntity());
		Assert.assertEquals(1, modelRetriever.getEntityMap().size());
		Assert.assertTrue(modelRetriever.getEntityMap().containsValue(entity1));	
	}
	
	@Test
	public void testRemove_EntityType() throws Exception 
	{
		IEntityType entityType = new GenericEntityType("entityType1");
		IEntityType entityType2 = new GenericEntityType("entityType2");
		modelRetriever.save(entityType);
		modelRetriever.save(entityType2);
		
		IEntityType copyEntityType = new GenericEntityType(entityType.getPackageName(), entityType.getName());
		for (IPropertyType propertyType : entityType.getPropertyTypes())
		{
			IPropertyType copyPropertyType = new GenericPropertyType(propertyType.getName(), propertyType.getType());
			copyEntityType.addPropertyType(copyPropertyType);
		}
		manager.removeEntityType(copyEntityType);
		
		Assert.assertEquals(1, modelRetriever.getEntityTypeMap().size());
		Assert.assertTrue(modelRetriever.getEntityTypeMap().containsValue(entityType2));
	}
	
	@Test
	public void testRemove_EntityTypeWithAssociatedEntities() throws Exception 
	{
		IEntityType entityType = new GenericEntityType("entityType1");
		entityType.addPropertyType(new GenericPropertyType("id", Object.class));
		IEntityType entityType2 = new GenericEntityType("entityType2");
		modelRetriever.save(entityType);
		modelRetriever.save(entityType2);
		IEntity entity1 = entityType.createNewEntity();
		entity1.setProperty("id", 1);
		IEntity entity2 = entityType.createNewEntity();
		entity2.setProperty("id", 2);
		modelRetriever.save(entity1);
		modelRetriever.save(entity2);
		
		IEntityType copyEntityType = new GenericEntityType(entityType.getPackageName(), entityType.getName());
		for (IPropertyType propertyType : entityType.getPropertyTypes())
		{
			IPropertyType copyPropertyType = new GenericPropertyType(propertyType.getName(), propertyType.getType());
			copyEntityType.addPropertyType(copyPropertyType);
		}
		manager.removeEntityType(copyEntityType);
		
		Assert.assertEquals(1, modelRetriever.getEntityTypeMap().size());
		Assert.assertEquals(0, modelRetriever.getEntityMap().size());
		Assert.assertTrue(modelRetriever.getEntityTypeMap().containsValue(entityType2));
	}
	
	@Test
	public void testRemove_EntityById() throws Exception 
	{
		IEntityType entityType1 = new GenericEntityType("entityType1");
		entityType1.addPropertyType(new GenericPropertyType("id", Object.class));
		IEntity entity1 = entityType1.createNewEntity();
		entity1.setProperty("id", 1);
		modelRetriever.save(entity1);
		IEntity entity2 = entityType1.createNewEntity();
		entity1.setProperty("id", 2);
		modelRetriever.save(entity2);
		
		manager.removeEntity(1, entityType1);
		Assert.assertEquals(1, manager.getEntitiesForType(entityType1).size());
	}
	
	@Test
	public void testRemove_Entity() throws Exception 
	{
		IEntityType entityType1 = new GenericEntityType("entityType1");
		entityType1.addPropertyType(new GenericPropertyType("id", Object.class));
		IEntity entity1 = entityType1.createNewEntity();
		entity1.setProperty("id", 1);
		modelRetriever.save(entity1);
		IEntity entity2 = entityType1.createNewEntity();
		entity2.setProperty("id", 2);
		modelRetriever.save(entity2);
				
		manager.removeEntity(entity1);
		Assert.assertEquals(1, manager.getEntitiesForType(entityType1).size());
	}
	
	@Test
	public void testSave_EntityWithoutId() throws Exception 
	{
		IEntityType entityType1 = new GenericEntityType("entityType1");
		entityType1.addPropertyType(new GenericPropertyType("id", Object.class));
		IEntity entity1 = entityType1.createNewEntity();
			
		manager.save(entity1);
		Assert.assertTrue(modelRetriever.isSavedEntity());
		Assert.assertEquals(1, modelRetriever.getEntityMap().size());
		Assert.assertTrue(modelRetriever.getEntityMap().get(1000).equals(entity1));	
	}
	
	@Test
	public void testGetEntityType_EntityTypeInObjectMap() throws Exception 
	{
		IEntityType entityTypeResult = new GenericEntityType("entityType1InObjectMap");
		modelRetriever.save(entityTypeResult);
		modelRetriever.setReturnCopy(true);
		
		// First time loaded
		IEntityType entityType = manager.getEntityType("/entityType1InObjectMap");
		Assert.assertTrue(modelRetriever.isEnteredInGetEntityType());
		modelRetriever.resetEnteredInGetEntityType();
		
		// Second time - should not get from database
		IEntityType entityType2 = manager.getEntityType("/entityType1InObjectMap");
		Assert.assertFalse(modelRetriever.isEnteredInGetEntityType());
		
		Assert.assertSame(entityType, entityType2);
	}
	
	@Test
	public void testGetEntityType_EntityTypeNotInObjectMap() throws Exception 
	{
		IEntityType entityTypeResult = new GenericEntityType("entityType1NotInObjectMap");
		modelRetriever.save(entityTypeResult);
		modelRetriever.setReturnCopy(true);
		
		manager.getEntityType("/entityType1NotInObjectMap");
		Assert.assertTrue(modelRetriever.isEnteredInGetEntityType());
	}
	
	@Test
	public void testGetEntity_EntityInObjectMap() throws Exception 
	{
		IEntityType entityTypeResult = new GenericEntityType("entityType1InObjectMap");
		entityTypeResult.addPropertyType(new GenericPropertyType("id", Object.class));
		IEntity entityResult = entityTypeResult.createNewEntity();
		entityResult.setProperty("id", 100);
		modelRetriever.save(entityResult);
		modelRetriever.setReturnCopy(true);
		
		// First time loaded
		HasProperties entity = manager.getEntity(entityTypeResult, 100);
		Assert.assertTrue(modelRetriever.isEnteredInGetEntity());
		modelRetriever.resetEnteredInGetEntity();
		
		// Second time - should not get from database
		HasProperties entity2 = manager.getEntity(entityTypeResult, 100);
		Assert.assertFalse(modelRetriever.isEnteredInGetEntity());
		
		Assert.assertSame(entity, entity2);
	}
	
	@Test
	public void testGetEntity_notFound() throws Exception 
	{
		IEntityType entityType = new GenericEntityType("package", "entityType1");
		
		HasProperties entity = manager.getEntity(entityType, 1);		
		Assert.assertNull(entity);
	}
	
	@Test
	public void testGetEntityType_usingPackageAndName() throws Exception 
	{
		IEntityType entityType = new GenericEntityType("package", "entityType1");
		modelRetriever.save(entityType);
		
		IEntityType entityTypeResult = manager.getEntityType("package", "entityType1");
		Assert.assertEquals(entityType, entityTypeResult);
	}
	
	@Test
	public void testGetEntityType_notFound() throws Exception 
	{
		IEntityType entityType = new GenericEntityType("package", "entityType1");
		modelRetriever.save(entityType);
		
		IEntityType entityTypeResult = manager.getEntityType("package1", "entityType1");
		Assert.assertNull(entityTypeResult);
	}
	
	
	@Test
	public void testEquivalentEntityTypes_equals() throws Exception 
	{
		IEntityType entityType = new GenericEntityType("package", "entityType1");
		Assert.assertTrue(manager.equivalentEntityTypes(entityType, "package/entityType1"));
	}
		
	@Test
	public void testEquivalentEntityTypes_notEquals() throws Exception 
	{
		IEntityType entityType = new GenericEntityType("package", "entityType1");
		Assert.assertFalse(manager.equivalentEntityTypes(entityType, "package/EntityType1"));
	}
	
	@Test
	public void testEquivalentEntityTypes2_equals() throws Exception 
	{
		IEntityType entityType = new GenericEntityType("package", "entityType1");
		IEntityType entityType2 = new GenericEntityType("package", "entityType1");
		Assert.assertTrue(manager.equivalentEntityTypes(entityType, entityType2));
	}
	
	@Test
	public void testEquivalentEntityTypes2_notEquals() throws Exception 
	{
		IEntityType entityType = new GenericEntityType("package", "entityType1");
		IEntityType entityType2 = new GenericEntityType("package", "EntityType1");
		Assert.assertFalse(manager.equivalentEntityTypes(entityType, entityType2));
	}
	
	@Test
	public void testEquivalentEntityTypes2_notEqualsDifferentPackage() throws Exception 
	{
		IEntityType entityType = new GenericEntityType("package", "entityType1");
		IEntityType entityType2 = new GenericEntityType("package2", "entityType1");
		Assert.assertFalse(manager.equivalentEntityTypes(entityType, entityType2));
	}
	
}
