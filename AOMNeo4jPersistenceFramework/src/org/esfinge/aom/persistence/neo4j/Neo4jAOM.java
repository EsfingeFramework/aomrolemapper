package org.esfinge.aom.persistence.neo4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import org.esfinge.aom.api.manager.visitors.IEntityTypeVisitor;
import org.esfinge.aom.api.manager.visitors.IEntityVisitor;
import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.api.model.IEntityType;
import org.esfinge.aom.api.model.IProperty;
import org.esfinge.aom.api.model.IPropertyType;
import org.esfinge.aom.api.modelretriever.IModelRetriever;
import org.esfinge.aom.exceptions.EsfingeAOMException;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;

public class Neo4jAOM implements IModelRetriever {

	private GraphDatabaseService graphdb;
	
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
	
	private static final DynamicRelationshipType PROPERTY_TYPE_RELATIONSHIP =
			DynamicRelationshipType.withName(ENTITY_TYPE_PROPERTY_TYPES);

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
		initDatabase();
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
		} catch(Exception e){
			failureTx(t);
			throw new EsfingeAOMException(e);
		}
	}

	private void initDatabase() throws EsfingeAOMException {
		this.graphdb = new GraphDatabaseFactory().newEmbeddedDatabase(databaseName);

		try {
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
		String entityOrEntityTypeId = getEntityTypeId(entityType);
		Node node = index.get(entityOrEntityTypeId, id).getSingle();
		
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
		
		String clazz = entityType.getName();
		Index<Node> index = getIndex(clazz);
		String entityOrEntityTypeId = getEntityTypeId(entityType);
		
		/*	TODO removeAllRelationshipsFromThisEntityTypeToAllItsEntities
		 *  TODO removeAllEntitiesOfThisGivenEntityType
		 *  TODO removeThisEntityType
		 */
		
		/*	
		entityPersistence.removeByEntityType(entityType);
		query = new BasicDBObject();
		query.put(ENTITY_ENTITY_TYPE_ID, id);
		DBCollection entityTypeCollection = getCollectionForEntityType(entityType);
		entityTypeCollection.findAndRemove(query);
	 */
		
	/*	
	 * entityTypePersistence.removeById(id);
		BasicDBObject query = new BasicDBObject();
		query.put(ID_FIELD_NAME, id);
		DBObject entityTypeObj = entityTypeCollection.findOne(query);
		entityTypeCollection.remove(entityTypeObj);
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

		Transaction t = beginTx();
		
		try {
			
			String entitySimpleName = entity.getClass().getName();
			Node entityGraphNode = this.createNewGraphNode(entitySimpleName);

			Object id = entity.getProperty("id").getValue();
			entityGraphNode.setProperty(ID_FIELD_NAME, id);
			
			entityGraphNode = putAndRetrieveIfExistentNodeFromIndex(entitySimpleName, entityGraphNode, id.toString());
			
//			BasicDBObject entityProperties = new BasicDBObject();
			
			for (IProperty property : entity.getProperties()) {
				Node entityPropertiesGraphNode = createNewGraphNode(property.getName());
				IPropertyType propertyType = property.getPropertyType();
				boolean isRelationshipProperty = propertyType.isRelationshipProperty();
				entityPropertiesGraphNode.setProperty(PROPERTY_IS_RELATIONSHIP, isRelationshipProperty);
				if (isRelationshipProperty) {
					IEntity relatedEntity = (IEntity) property.getValue();
					entityPropertiesGraphNode.setProperty(PROPERTY_ENTITY_TYPE, getEntityTypeId(relatedEntity.getEntityType()));
					entityPropertiesGraphNode.setProperty(PROPERTY_ENTITY_ID, relatedEntity.getProperty("id").getValue());
				} else {
					entityPropertiesGraphNode.setProperty(PROPERTY_VALUE, property.getValue());
				}
				
				// TODO confirmar esses parâmetroas
//				entityProperties.put(propertyType.getName(), entityPropertiesGraphNode);
				graphdb.index().forNodes(entitySimpleName).add(entityGraphNode, propertyType.getName(), entityPropertiesGraphNode);
				
				// FIXME descobrir nome para essa DynamicRelationshipType
				entityGraphNode.createRelationshipTo(entityPropertiesGraphNode, DynamicRelationshipType.withName("hasProperty"));
			}
			
//			entityGraphNode.setProperty(ENTITY_PROPERTIES, entityProperties);
			
			Object associatedEntityObj = entity.getAssociatedObject();
			
			if (associatedEntityObj != null) {
				entityGraphNode.setProperty(ENTITY_CLASS, associatedEntityObj.getClass().getName());
			}
			
			entityGraphNode.setProperty(ENTITY_ENTITY_TYPE_ID, getEntityTypeId(entity.getEntityType()));
			
			insertSaveOrUpdate(persistenceType, entityGraphNode, id.toString());
			
			successTx(t);
			
		} catch (Exception e) {
			failureTx(t);
			throw new EsfingeAOMException(e);
		}
	}
	

	private Node persist(IEntityType entityType, PersistenceType persistenceType) throws EsfingeAOMException {
		
		Transaction t = beginTx();

		try {
			Node entityTypeGraphNode = this.createNewGraphNode(entityType.getName());

			String id = getEntityTypeId(entityType);

			entityTypeGraphNode.setProperty(ID_FIELD_NAME, id);

			entityTypeGraphNode = putAndRetrieveIfExistentNodeFromIndex(entityType.getName(), entityTypeGraphNode, id);

			entityTypeGraphNode.setProperty(ENTITY_TYPE_NAME, entityType.getName());
			entityTypeGraphNode.setProperty(ENTITY_TYPE_PACKAGE, entityType.getPackageName());
			
			// TODO verificar necessidade novo Node
//			BasicDBObject entityTypePropertyTypes = new BasicDBObject();
						
			for (IPropertyType propertyType : entityType.getPropertyTypes()) {
				
				Node propertyTypeGraphNode = this.createNewGraphNode(propertyType.getName());
				
				Boolean isRelationship = propertyType.isRelationshipProperty();
				
				if (!isRelationship) {
					Class<?> propertyTypeTypeClass = (Class<?>) propertyType.getType();
					propertyTypeGraphNode.setProperty(PROPERTY_TYPE_TYPE, propertyTypeTypeClass.getName());					
				} else {
					IEntityType propertyTypeType = (IEntityType) propertyType.getType();
					propertyTypeGraphNode.setProperty(PROPERTY_TYPE_TYPE, getEntityTypeId(propertyTypeType));
				}
				propertyTypeGraphNode.setProperty(PROPERTY_TYPE_IS_RELATIONSHIP, isRelationship);
				Object associatedPropertyType = propertyType.getAssociatedObject();	
				if (associatedPropertyType != null) {
					propertyTypeGraphNode.setProperty(PROPERTY_TYPE_CLASS, associatedPropertyType.getClass().getName());
				}
//				entityTypePropertyTypes.put(propertyType.getName(), propertyTypeGraphNode);
				graphdb.index().forNodes(entityType.getName()).add(entityTypeGraphNode, propertyType.getName(), propertyTypeGraphNode);

				entityTypeGraphNode.createRelationshipTo(propertyTypeGraphNode, PROPERTY_TYPE_RELATIONSHIP);
			}
			
//			entityTypeGraphNode.put(ENTITY_TYPE_PROPERTY_TYPES, entityTypePropertyTypes);
			
			Object associatedEntityType = entityType.getAssociatedObject();
			
			if (associatedEntityType != null) {
				entityTypeGraphNode.setProperty(ENTITY_TYPE_CLASS, associatedEntityType.getClass().getName());
			}
			
			// TODO Test persistRelatedEntities
			persistRelatedEntities(entityType, entityTypeGraphNode, entityTypeGraphNode, persistenceType);

			// TODO Neo4J create/merge automático, portanto verificar necessidade, excluir PersistenceType caso não precise
			insertSaveOrUpdate(persistenceType, entityTypeGraphNode, id);
			
			successTx(t);
			return entityTypeGraphNode;
			
		} catch (Exception e) {
			failureTx(t);
			throw new EsfingeAOMException(e);
		}
	}
	
	/**
	 * TODO retirar caso desnecessário
	 */
	private void insertSaveOrUpdate(PersistenceType persistenceType, Node entityTypeGraphNode, String id) {
//		switch (persistenceType) {
//		case Insert:
//			entityTypeCollection.insert(entityTypeGraphNode);
//			break;
//			
//		case Save:
//			entityTypeCollection.save(entityTypeGraphNode);
//			break;
//			
//		case Update:
//			BasicDBObject query = new BasicDBObject();
//			query.put(ID_FIELD_NAME, id);
//			
//			DBObject entityTypeObj = entityTypeCollection.findOne(query);
//			entityTypeCollection.update(entityTypeObj, entityTypeGraphNode);
//			break;
//		}
	}

	private Node createNewGraphNode(String label) {
		Node entityTypeGraphNode = graphdb.createNode();
		entityTypeGraphNode.addLabel(DynamicLabel.label(label));
		return entityTypeGraphNode;
	}

	private Node putAndRetrieveIfExistentNodeFromIndex(String nodeNameIndex, Node graphNode, String id) throws EsfingeAOMException {
		Node present = graphdb.index().forNodes(nodeNameIndex).putIfAbsent(graphNode, ID_FIELD_NAME, id);
		if(present != null)
			graphNode = present;
		return graphNode;
	}
	
	private void persistRelatedEntities(Object entity, Node newNode, Node present, PersistenceType persistenceType) throws EsfingeAOMException {
		for(Relationship relatedEntity : present.getRelationships()){
			Object related = removeRelationshipIfExistsInIndex(entity, present, relatedEntity);
			persistEachRelatedEntityRecursivily(newNode, relatedEntity, related, persistenceType);
		}
	}

	private void persistEachRelatedEntityRecursivily(Node newNode, Relationship relatedEntity, Object related, PersistenceType persistenceType) throws EsfingeAOMException {
		if(Collection.class.isAssignableFrom(related.getClass())){
			Collection<?> c = (Collection<?>) related;
			for(Object o : c){
				Node relatedNode = persist((IEntityType) o, persistenceType);
				newNode.createRelationshipTo(relatedNode, relatedEntity.getType());
			}
		} else {
			Node relatedNode = persist((IEntityType) related, persistenceType);
			newNode.createRelationshipTo(relatedNode, relatedEntity.getType());
		}
	}

	private Object removeRelationshipIfExistsInIndex(Object entity, Node present, Relationship relatedEntity) {
//		Object related = parser.getRelatedEntity(entity, relatedEntity);
//		
//		if(present != null){
//			for(Relationship relation : present.getRelationships(info.getRelationshipType(relatedEntity))){
//				Node otherNode = relation.getOtherNode(present);
//				relation.delete();
//				if(!otherNode.hasRelationship()){
//					graphdb.index().forNodes(info.getRelatedClass(relatedEntity).getName()).remove(otherNode);
//					otherNode.delete();
//				}
//			}
//		}
//		return related;
		return new Object();
	}
	
	private DBCollection getCollectionForEntityType (IEntityType entityType) throws EsfingeAOMException {
		String collectionName = neo4jAomConfig.getNodeForEntityType(entityType.getName(), entityType.getPackageName());
		if (collectionName == null)
		{
			collectionName = DEFAULT_COLLECTION_FOR_ENTITIES;
		}
		return db.getCollection(collectionName);
	}
	
	private Class<?> getClass(String className) throws ClassNotFoundException {
		if (Pattern.matches(".*\\..*", className)) {
			return Class.forName(className);
		}
		if (className.equals("int")) {
			return int.class;
		}
		if (className.equals("long")) {
			return long.class;
		}
		if (className.equals("double")) {
			return double.class;
		}
		if (className.equals("float")) {
			return float.class;
		}
		if (className.equals("boolean")) {
			return boolean.class;
		}
		if (className.equals("char")) {
			return char.class;
		}
		if (className.equals("byte")) {
			return byte.class;
		}
		if (className.equals("short")) {
			return short.class;
		}
		return null;
	}

	@Override
	public Object generateEntityId() {
		return UUID.randomUUID().toString();
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
	 * TODO Realizar pullUp para método default na interface {@link IModelRetriever}
	 * após merge das versões de 'PersistenceFrameorks' (MongoDB, CouchDB e Neo4J)
	 */
	private String getEntityTypeId(IEntityType entityType) throws EsfingeAOMException {
		return getEntityTypeId(entityType.getPackageName(), entityType.getName());
	}
	
	private String getEntityTypeId(String packageName, String name) {
		return packageName + "/" + name;
	}
	
}
