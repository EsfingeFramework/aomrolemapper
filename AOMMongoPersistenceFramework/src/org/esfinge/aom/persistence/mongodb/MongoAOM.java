package org.esfinge.aom.persistence.mongodb;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

import net.sf.esfinge.aom.api.manager.visitors.IEntityTypeVisitor;
import net.sf.esfinge.aom.api.manager.visitors.IEntityVisitor;
import net.sf.esfinge.aom.api.model.IEntity;
import net.sf.esfinge.aom.api.model.IEntityType;
import net.sf.esfinge.aom.api.model.IProperty;
import net.sf.esfinge.aom.api.model.IPropertyType;
import net.sf.esfinge.aom.api.modelretriever.IModelRetriever;
import net.sf.esfinge.aom.exceptions.EsfingeAOMException;


public class MongoAOM implements IModelRetriever {

	private DB db;
	
	private DBCollection entityTypeCollection;
	
	private static String ID_FIELD_NAME = "_id";
	
	private static String PROPERTY_VALUE = "propertyValue";
	
	private static String PROPERTY_IS_RELATIONSHIP = "isRelationship";
	
	private static String PROPERTY_TYPE_IS_RELATIONSHIP = "isRelationship";
		
	private static String PROPERTY_ENTITY_TYPE = "propertyEntityType";
	
	private static String PROPERTY_ENTITY_ID = "esfingePropertyEntityId";
	
	private static String ENTITY_PROPERTIES = "esfingeEntityProperties";
	
	private static String ENTITY_CLASS = "esfingeEntityClass";
	
	private static String ENTITY_ENTITY_TYPE_ID = "esfingeEntityEntityTypeID";
	
	private static String ENTITY_TYPE_NAME = "esfingeEntityTypeName";
	
	private static String ENTITY_TYPE_PACKAGE = "esfingeEntityTypePackage";
	
	private static String ENTITY_TYPE_PROPERTY_TYPES = "esfingeEntityTypePropertyTypes";
	
	private static String ENTITY_TYPE_CLASS = "esfingeEntityTypeClass";
	
	private static String PROPERTY_TYPE_TYPE = "esfingePropertyTypeType";
	
	private static String PROPERTY_TYPE_CLASS = "esfingePropertyTypeClass";
	
	private static String DEFAULT_COLLECTION_FOR_ENTITIES = "esfingeEntitiesCollection";
	
	private MongoAOMConfiguration mongoAomConfig;
	
	private String databaseName;
	
	private String host;
	
	private enum PersistenceType 
	{
		Save,
		Update,
		Insert
	}
		
	public MongoAOM() throws EsfingeAOMException
	{		
		mongoAomConfig = new MongoAOMConfiguration();
		this.host = mongoAomConfig.getHost();
		this.databaseName = mongoAomConfig.getDatabase();
		initDatabase();
	}
	
	public void clearDatabase() throws EsfingeAOMException
	{
		db.dropDatabase();
		initDatabase();
	}
	
	private void initDatabase () throws EsfingeAOMException
	{
		Mongo m;
		
		try {
			m = new Mongo(host);
			db = m.getDB(databaseName);
			
			String entityTypeCollectionName = mongoAomConfig.getEntityTypeCollectionName();
			entityTypeCollection = db.getCollection(entityTypeCollectionName);
			
		} catch (Exception e) {
			throw new EsfingeAOMException(e);
		}
	}
	
	@Override
	public void save(IEntity entity) throws EsfingeAOMException {
		persist(entity, PersistenceType.Save);
	}

	@Override
	public void update(IEntity entity) throws EsfingeAOMException {
		persist(entity, PersistenceType.Update);
	}

	@Override
	public void insert(IEntity entity) throws EsfingeAOMException {
		persist(entity, PersistenceType.Insert);
	}

	@Override
	public void removeEntity(Object id, IEntityType entityType) throws EsfingeAOMException {
		DBCollection collection = getCollectionForEntityType(entityType);
		DBObject objToRemove = new BasicDBObject();
		objToRemove.put(ID_FIELD_NAME, id);
		collection.remove(objToRemove);
	}

	@Override
	public void removeEntity(IEntity entity) throws EsfingeAOMException {
		removeEntity(entity.getProperty("id").getValue(), entity.getEntityType());
	}

	@Override
	public void update(IEntityType entityType) throws EsfingeAOMException {		
		persist(entityType, PersistenceType.Update);
	}

	@Override
	public void insert(IEntityType entityType) throws EsfingeAOMException {
		persist(entityType, PersistenceType.Insert);
	}

	@Override
	public void save(IEntityType entityType) throws EsfingeAOMException {
		persist(entityType, PersistenceType.Save);
	}

	@Override
	public void removeEntityType(IEntityType entityType) throws EsfingeAOMException {
		String id = getEntityTypeId(entityType);
		
		BasicDBObject query = new BasicDBObject();
		query.put(ID_FIELD_NAME, id);
		DBObject entityTypeObj = entityTypeCollection.findOne(query);
		entityTypeCollection.remove(entityTypeObj);
		
		query = new BasicDBObject();
		query.put(ENTITY_ENTITY_TYPE_ID, id);
		DBCollection entityTypeCollection = getCollectionForEntityType(entityType);
		entityTypeCollection.findAndRemove(query);
	}

	@Override
	public IEntity getEntity(Object id, IEntityType entityType, IEntityVisitor entityVisitor) throws EsfingeAOMException {		
		
		DBCollection collection = getCollectionForEntityType(entityType);
		BasicDBObject query = new BasicDBObject();
		query.put(ID_FIELD_NAME, id);
		DBObject dbEntity = collection.findOne(query);

		if (dbEntity != null)
		{
			if (dbEntity.containsField(ENTITY_CLASS))
			{
				String dsClass = (String)dbEntity.get(ENTITY_CLASS);
				entityVisitor.initVisit(id, entityType, dsClass);
			}
			else
			{
				entityVisitor.initVisit(id, entityType);
			}
			
			DBObject properties = (DBObject) dbEntity.get(ENTITY_PROPERTIES);
			for (String propertyName : properties.keySet())
			{
				DBObject propertyFields = (DBObject)properties.get(propertyName);
				Boolean isRelationshipProperty = (Boolean)propertyFields.get(PROPERTY_IS_RELATIONSHIP);
				if (isRelationshipProperty)
				{
					String entityTypeId = (String)propertyFields.get(PROPERTY_ENTITY_TYPE);
					String entityId = (String)propertyFields.get(PROPERTY_ENTITY_ID);
					entityVisitor.visitRelationship(propertyName, entityTypeId, entityId);
				}
				else
				{
					Object value = propertyFields.get(PROPERTY_VALUE);
					entityVisitor.visitProperty(propertyName, value);
				}
			}

			return entityVisitor.endVisit();
		}
		else
		{
			return null;
		}
	}
	
	@Override
	public IEntityType getEntityType(String packageName, String name, IEntityTypeVisitor entityTypeVisitor) throws EsfingeAOMException {
		String id = getEntityTypeId(packageName, name);
		return getEntityType(id, entityTypeVisitor);
	}
	
	@Override
	public IEntityType getEntityType(String id, IEntityTypeVisitor entityTypeVisitor) throws EsfingeAOMException {

		try {			
			BasicDBObject query = new BasicDBObject();
			query.put(ID_FIELD_NAME, id);
			DBObject dbEntityType = entityTypeCollection.findOne(query);

			if (dbEntityType != null)
			{
				String packageName = (String)dbEntityType.get(ENTITY_TYPE_PACKAGE);
				String name = (String)dbEntityType.get(ENTITY_TYPE_NAME);
								
				if (dbEntityType.containsField(ENTITY_TYPE_CLASS))
				{
					String dsClass = (String)dbEntityType.get(ENTITY_TYPE_CLASS);
					entityTypeVisitor.initVisit(packageName, name, dsClass);
				}
				else
				{
					entityTypeVisitor.initVisit(packageName, name);
				}

				DBObject propertyTypes = (DBObject) dbEntityType.get(ENTITY_TYPE_PROPERTY_TYPES);
				for (String propertyName : propertyTypes.keySet())
				{
					DBObject propertyTypeFields = (DBObject)propertyTypes.get(propertyName);
					Boolean isRelationship = (Boolean) propertyTypeFields.get(PROPERTY_TYPE_IS_RELATIONSHIP);
					Object type = null; 
					String dsClass = null;
					
					if (propertyTypeFields.containsField(PROPERTY_TYPE_CLASS))
					{
						dsClass = (String)propertyTypeFields.get(PROPERTY_TYPE_CLASS);						
					}
					
					if (!isRelationship)
					{
						String typeClass = (String)propertyTypeFields.get(PROPERTY_TYPE_TYPE);
						type = getClass(typeClass);
						entityTypeVisitor.visitPropertyType(propertyName, type, dsClass);
					}
					else
					{
						String entityTypeID = (String)propertyTypeFields.get(PROPERTY_TYPE_TYPE);
						entityTypeVisitor.visitRelationship(propertyName, entityTypeID, dsClass);
					}
					
				}
				return entityTypeVisitor.endVisit();
			}
			else
			{
				return null;
			}
		} catch (Exception e) {
			throw new EsfingeAOMException(e);
		}
	}

	@Override
	public List<String> getAllEntityTypeIds() throws EsfingeAOMException {
		List<String> entityTypeIds = new ArrayList<String>();
		DBCursor cursor = entityTypeCollection.find();
		while (cursor.hasNext())
		{
			DBObject current = cursor.next();
			String id = (String)current.get(ID_FIELD_NAME);
			entityTypeIds.add(id);
		}
		return entityTypeIds;
	}
	
	@Override
	public List<Object> getAllEntityIDsForType(IEntityType entityType) throws EsfingeAOMException {
		List<Object> entityIDs = new ArrayList<Object>();
		String entityTypeId = getEntityTypeId(entityType);
		DBCollection collection = getCollectionForEntityType(entityType);
		BasicDBObject query = new BasicDBObject();
		query.put(ENTITY_ENTITY_TYPE_ID, entityTypeId);
		DBCursor cursor = collection.find(query);
		while (cursor.hasNext())
		{
			DBObject current = cursor.next();
			Object entityId = current.get(ID_FIELD_NAME);
			entityIDs.add(entityId);
		}
		return entityIDs;
	}

	private void persist(IEntity entity, PersistenceType persistenceType) throws EsfingeAOMException {
		try {			
			
			DBCollection entityCollection = getCollectionForEntityType(entity.getEntityType());
			
			Object id = entity.getProperty("id").getValue();
			
			BasicDBObject entityDBObj = new BasicDBObject();
			
			entityDBObj.put(ID_FIELD_NAME, id);			
			
			BasicDBObject entityProperties = new BasicDBObject();
			
			for (IProperty property : entity.getProperties())
			{				
				BasicDBObject propertyObj = new BasicDBObject();
				IPropertyType propertyType = property.getPropertyType();
				boolean isRelationshipProperty = propertyType.isRelationshipProperty();
				propertyObj.put(PROPERTY_IS_RELATIONSHIP, isRelationshipProperty);
				if (isRelationshipProperty)
				{
					IEntity relatedEntity = (IEntity)property.getValue();
					propertyObj.put(PROPERTY_ENTITY_TYPE, getEntityTypeId(relatedEntity.getEntityType()));
					propertyObj.put(PROPERTY_ENTITY_ID, relatedEntity.getProperty("id").getValue());
				}
				else
				{
					propertyObj.put(PROPERTY_VALUE, property.getValue());
				}
				entityProperties.put(propertyType.getName(), propertyObj);
			}
			
			entityDBObj.put(ENTITY_PROPERTIES, entityProperties);
			
			Object associatedEntityObj = entity.getAssociatedObject();
			
			if (associatedEntityObj != null)
			{
				entityDBObj.put(ENTITY_CLASS, associatedEntityObj.getClass().getName());
			}
			
			entityDBObj.put(ENTITY_ENTITY_TYPE_ID, getEntityTypeId(entity.getEntityType()));
			
			switch (persistenceType)
			{
			case Insert:
				entityCollection.insert(entityDBObj);
				break;
				
			case Update:
				BasicDBObject query = new BasicDBObject();
				query.put(ID_FIELD_NAME, id);
				
				DBObject entityObj = entityCollection.findOne(query);
				entityCollection.update(entityObj, entityDBObj);
				break;
				
			default:
				entityCollection.save(entityDBObj);	
			}		
			
		} 
		catch (Exception e)
		{
			throw new EsfingeAOMException(e);
		}
	}
	
	private void persist(IEntityType entityType, PersistenceType persistenceType) throws EsfingeAOMException {
				
		try {			
			BasicDBObject entityTypeDBObj = new BasicDBObject();
			
			String id = getEntityTypeId(entityType);
			
			entityTypeDBObj.put(ID_FIELD_NAME, id);
			
			entityTypeDBObj.put(ENTITY_TYPE_NAME, entityType.getName());
			
			entityTypeDBObj.put(ENTITY_TYPE_PACKAGE, entityType.getPackageName());
						
			BasicDBObject entityTypePropertyTypes = new BasicDBObject();
			
			for (IPropertyType propertyType : entityType.getPropertyTypes())
			{				
				BasicDBObject propertyTypeObj = new BasicDBObject();
				
				Boolean isRelationship = propertyType.isRelationshipProperty();
				
				if (!isRelationship)
				{
					Class<?> propertyTypeTypeClass = (Class<?>)propertyType.getType();
					propertyTypeObj.put(PROPERTY_TYPE_TYPE, propertyTypeTypeClass.getName());					
				}
				else
				{
					IEntityType propertyTypeType = (IEntityType)propertyType.getType();
					propertyTypeObj.put(PROPERTY_TYPE_TYPE, getEntityTypeId(propertyTypeType));
				}
				propertyTypeObj.put(PROPERTY_TYPE_IS_RELATIONSHIP, isRelationship);
				Object associatedPropertyType = propertyType.getAssociatedObject();	
				if (associatedPropertyType != null)
				{
					propertyTypeObj.put(PROPERTY_TYPE_CLASS, associatedPropertyType.getClass().getName());
				}
				entityTypePropertyTypes.put(propertyType.getName(), propertyTypeObj);
			}
			
			entityTypeDBObj.put(ENTITY_TYPE_PROPERTY_TYPES, entityTypePropertyTypes);
			
			Object associatedEntityType = entityType.getAssociatedObject();
			
			if (associatedEntityType != null)
			{
				entityTypeDBObj.put(ENTITY_TYPE_CLASS, associatedEntityType.getClass().getName());
			}
			
			switch (persistenceType)
			{
			case Insert:
				entityTypeCollection.insert(entityTypeDBObj);
				break;
				
			case Save:
				entityTypeCollection.save(entityTypeDBObj);
				break;
				
			case Update:
				BasicDBObject query = new BasicDBObject();
				query.put(ID_FIELD_NAME, id);
				
				DBObject entityTypeObj = entityTypeCollection.findOne(query);
				entityTypeCollection.update(entityTypeObj, entityTypeDBObj);
				break;
			}
		} 
		catch (Exception e)
		{
			throw new EsfingeAOMException(e);
		}
	}
	
	private String getEntityTypeId (IEntityType entityType) throws EsfingeAOMException
	{
		return getEntityTypeId(entityType.getPackageName(), entityType.getName());
	}
	
	private String getEntityTypeId (String packageName, String name)
	{
		return packageName + "/" + name;
	}
	
	private DBCollection getCollectionForEntityType (IEntityType entityType) throws EsfingeAOMException
	{
		String collectionName = mongoAomConfig.getCollectionForEntityType(entityType.getName(), entityType.getPackageName());
		if (collectionName == null)
		{
			collectionName = DEFAULT_COLLECTION_FOR_ENTITIES;
		}
		return db.getCollection(collectionName);
	}
	
	private Class<?> getClass(String className) throws ClassNotFoundException
	{
		if (Pattern.matches(".*\\..*", className))
		{
			return Class.forName(className);
		}
		if (className.equals("int"))
		{
			return int.class;
		}
		if (className.equals("long"))
		{
			return long.class;
		}
		if (className.equals("double"))
		{
			return double.class;
		}
		if (className.equals("float"))
		{
			return float.class;
		}
		if (className.equals("boolean"))
		{
			return boolean.class;
		}
		if (className.equals("char"))
		{
			return char.class;
		}
		if (className.equals("byte"))
		{
			return byte.class;
		}
		if (className.equals("short"))
		{
			return short.class;
		}
		return null;
	}

	@Override
	public Object generateEntityId() {
		return new ObjectId();
	}
}
