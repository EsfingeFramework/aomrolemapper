package org.esfinge.aom.persistence.neo4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import org.esfinge.aom.api.manager.visitors.IEntityTypeVisitor;
import org.esfinge.aom.api.manager.visitors.IEntityVisitor;
import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.api.model.IEntityType;
import org.esfinge.aom.api.model.IPropertyType;
import org.esfinge.aom.api.modelretriever.IModelRetriever;
import org.esfinge.aom.exceptions.EsfingeAOMException;
import org.esfinge.querybuilder.neo4j.oomapper.MappingInfo;
import org.esfinge.querybuilder.neo4j.oomapper.parser.exceptions.ClassNotMappedException;
import org.esfinge.querybuilder.neo4j.oomapper.parser.exceptions.NullIdException;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;

public class Neo4jAOM implements IModelRetriever {

	private GraphDatabaseService graphdb;
	
//	private DBCollection entityTypeCollection;
	
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
	
	private Neo4JAOMConfiguration neo4jAomConfig;
	
	private String databaseName;
	
	private enum PersistenceType  {
		Save,
		Update,
		Insert
	}
		
	public Neo4jAOM() throws EsfingeAOMException {		
		neo4jAomConfig = new Neo4JAOMConfiguration();
		this.databaseName = neo4jAomConfig.getDatabase();
		this.graphdb = createNeo4JDatabase();
		initDatabase();
	}

	private GraphDatabaseService createNeo4JDatabase() {
		return new GraphDatabaseFactory().newEmbeddedDatabase(databaseName);
	}
	
	public void clearDatabase() throws EsfingeAOMException {
		this.clearDB();
		this.graphdb.shutdown();
		initDatabase();
	}
	
	private void clearDB() throws EsfingeAOMException {
		
		Transaction t = beginTx();
		
		try {
			for(Node node : graphdb.getAllNodes()) {
				for(Relationship rel : node.getRelationships()) {
					rel.delete();
				}
				node.delete();
			}
			successTx(t);
		}
		catch(Exception e){
			failureTx(t);
			throw new EsfingeAOMException(e);
		}
	}

	private void initDatabase() throws EsfingeAOMException {
		try {
			this.graphdb = createNeo4JDatabase();
			
			// TODO precisa fazer mapeamento da configuração das entidades?
			String entityTypeNodeName = neo4jAomConfig.getEntityTypeNodeName();
//			this.graphdb.map(IEntityType.class);
//			entityTypeCollection = db.getCollection(entityTypeNodeName);
			
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
		String clazz = entityType.getName();
		Index<Node> index = getIndex(clazz);
		Node node = index.get(getEntityTypeId(entityType), id).getSingle();
		
		Transaction t = beginTx();
		try {
			deleteNodeRelationsThenNode(node);
			successTx(t);
		} catch(Exception e) {
			failureTx(t);
			throw new EsfingeAOMException("The entity of " + clazz + " and Id " + id + " cannot be removed because it is part of a Relationship", e);
		}
		
	}
	
	/**
	 * To delete a node in Neo4J, all its relationships must me deleted first.
	 * @param node
	 */
	private void deleteNodeRelationsThenNode(Node node){
		for(Relationship relation : node.getRelationships(Direction.OUTGOING)){
			Node otherNode = relation.getOtherNode(node);
			relation.delete();
			deleteNodeRelationsThenNode(otherNode);
		}
		node.delete();
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
		
	/*	entityTypePersistence.removeById(id);
		BasicDBObject query = new BasicDBObject();
		query.put(ID_FIELD_NAME, id);
		DBObject entityTypeObj = entityTypeCollection.findOne(query);
		entityTypeCollection.remove(entityTypeObj);
	*/	
	/*	entityPersistence.removeByEntityType(entityType);
		query = new BasicDBObject();
		query.put(ENTITY_ENTITY_TYPE_ID, id);
		DBCollection entityTypeCollection = getCollectionForEntityType(entityType);
		entityTypeCollection.findAndRemove(query);
	*/
	}

	@Override
	public IEntity getEntity(Object id, IEntityType entityType, IEntityVisitor entityVisitor) throws EsfingeAOMException {		
		
		DBCollection collection = getCollectionForEntityType(entityType);
		BasicDBObject query = new BasicDBObject();
		query.put(ID_FIELD_NAME, id);
		DBObject dbEntity = collection.findOne(query);

		if (dbEntity != null) {
			if (dbEntity.containsField(ENTITY_CLASS)) {
				String dsClass = (String)dbEntity.get(ENTITY_CLASS);
				entityVisitor.initVisit(id, entityType, dsClass);
			} else {
				entityVisitor.initVisit(id, entityType);
			}
			
			DBObject properties = (DBObject) dbEntity.get(ENTITY_PROPERTIES);
			for (String propertyName : properties.keySet()) {
				DBObject propertyFields = (DBObject)properties.get(propertyName);
				Boolean isRelationshipProperty = (Boolean)propertyFields.get(PROPERTY_IS_RELATIONSHIP);
				if (isRelationshipProperty) {
					String entityTypeId = (String)propertyFields.get(PROPERTY_ENTITY_TYPE);
					String entityId = (String)propertyFields.get(PROPERTY_ENTITY_ID);
					entityVisitor.visitRelationship(propertyName, entityTypeId, entityId);
				} else {
					Object value = propertyFields.get(PROPERTY_VALUE);
					entityVisitor.visitProperty(propertyName, value);
				}
			}

			return entityVisitor.endVisit();
		} else {
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

			if (dbEntityType != null) {
				String packageName = (String)dbEntityType.get(ENTITY_TYPE_PACKAGE);
				String name = (String)dbEntityType.get(ENTITY_TYPE_NAME);
								
				if (dbEntityType.containsField(ENTITY_TYPE_CLASS)) {
					String dsClass = (String)dbEntityType.get(ENTITY_TYPE_CLASS);
					entityTypeVisitor.initVisit(packageName, name, dsClass);
				} else {
					entityTypeVisitor.initVisit(packageName, name);
				}

				DBObject propertyTypes = (DBObject) dbEntityType.get(ENTITY_TYPE_PROPERTY_TYPES);
				for (String propertyName : propertyTypes.keySet()) {
					DBObject propertyTypeFields = (DBObject)propertyTypes.get(propertyName);
					Boolean isRelationship = (Boolean) propertyTypeFields.get(PROPERTY_TYPE_IS_RELATIONSHIP);
					Object type = null; 
					String dsClass = null;
					
					if (propertyTypeFields.containsField(PROPERTY_TYPE_CLASS)) {
						dsClass = (String)propertyTypeFields.get(PROPERTY_TYPE_CLASS);						
					}
					
					if (!isRelationship) {
						String typeClass = (String)propertyTypeFields.get(PROPERTY_TYPE_TYPE);
						type = getClass(typeClass);
						entityTypeVisitor.visitPropertyType(propertyName, type, dsClass);
					} else {
						String entityTypeID = (String)propertyTypeFields.get(PROPERTY_TYPE_TYPE);
						entityTypeVisitor.visitRelationship(propertyName, entityTypeID, dsClass);
					}
					
				}
				return entityTypeVisitor.endVisit();
			} else {
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
		while (cursor.hasNext()) {
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
		while (cursor.hasNext()) {
			DBObject current = cursor.next();
			Object entityId = current.get(ID_FIELD_NAME);
			entityIDs.add(entityId);
		}
		return entityIDs;
	}

	private void persist(IEntity entity, PersistenceType persistenceType) throws EsfingeAOMException {
		
		if(!classInfoMap.containsKey(entity.getClass()))
			throw new ClassNotMappedException(entity.getClass());

		Transaction t = beginTx();
		Node newNode = null;

		try{
			MappingInfo info = classInfoMap.get(entity.getClass());
			newNode = graphdb.createNode();
			
			Label label = DynamicLabel.label(entity.getClass().getSimpleName());
			newNode.addLabel(label);
			
			String id = info.getId();
			Object idValue = parser.getPropertyValue(id, entity);
			if(idValue == null)
				throw new NullIdException("The entity " + entity + " cannot be saved with null Id");
			newNode.setProperty(id, idValue);
			
			Node present = graphdb.index().forNodes(entity.getClass().getName()).putIfAbsent(newNode, id, idValue);
			
			if(present != null)
				newNode = present;
			
			for(String property : info.getProperties()){
				Object value = parser.getPropertyValue(property, entity);
				if(value != null)
					newNode.setProperty(property, value);
				else
					newNode.setProperty(property, "null");
			}

			for(String property : info.getIndexedProperties()){
				Object value = parser.getPropertyValue(property.substring(property.lastIndexOf(".") + 1), entity);
				if(present != null)
					graphdb.index().forNodes(entity.getClass().getName()).remove(present, property);
				if(value != null){
					newNode.setProperty(property, value);
					graphdb.index().forNodes(entity.getClass().getName()).add(newNode, property, value);
				}else{
					newNode.setProperty(property, "null");
					graphdb.index().forNodes(entity.getClass().getName()).add(newNode, property, "null");
				}
			}

			for(String relatedEntity : info.getRelatedEntities()){
				
				Object related = parser.getRelatedEntity(entity, relatedEntity);
				
				if(present != null){
					for(Relationship relation : present.getRelationships(info.getRelationshipType(relatedEntity))){
						Node otherNode = relation.getOtherNode(present);
						relation.delete();
						if(!otherNode.hasRelationship()){
							graphdb.index().forNodes(info.getRelatedClass(relatedEntity).getName()).remove(otherNode);
							otherNode.delete();
						}
					}
				}
				if(Collection.class.isAssignableFrom(related.getClass())){
					Collection c = (Collection) related;
					for(Object o : c){
						Node relatedNode = persist(o);
						newNode.createRelationshipTo(relatedNode, info.getRelationshipType(relatedEntity));
					}
				}
				else{
					Node relatedNode = persist(related);
					newNode.createRelationshipTo(relatedNode, info.getRelationshipType(relatedEntity));
				}
				
			}

			successTx(t);
			
		}
		catch(Exception e){
			failureTx(t);
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
	
	private DBCollection getCollectionForEntityType (IEntityType entityType) throws EsfingeAOMException
	{
		String collectionName = neo4jAomConfig.getNodeForEntityType(entityType.getName(), entityType.getPackageName());
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
	
	private Transaction beginTx() {
		return graphdb.beginTx();
	}
	
	private void successTx(Transaction t) {
		t.success();
		t.finish();
	}
	
	private void failureTx(Transaction t) {
		t.failure();
		t.finish();
	}

	public Index<Node> getIndex(String clazz){
		return graphdb.index().forNodes(clazz);
	}
	
	/**
	 * TODO Após merge das versões de 'PersistenceFrameorks' (MongoDB, CouchDB e Neo4J), realizar pullUp para método default na interface {@link IModelRetriever}
	 */
	private String getEntityTypeId(IEntityType entityType) throws EsfingeAOMException {
		return getEntityTypeId(entityType.getPackageName(), entityType.getName());
	}
	
	private String getEntityTypeId(String packageName, String name) {
		return packageName + "/" + name;
	}
	
}
