package org.esfinge.aom.persistence.neo4j;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;
import org.neo4j.helpers.collection.IteratorUtil;

public class Neo4jAOM implements IModelRetriever {

	private GraphDatabaseService graphdb;
	
	private static String ID_FIELD_NAME = "_id";
	private static String PROPERTY_VALUE = "propertyValue";
	private static String PROPERTY_IS_RELATIONSHIP = "isRelationship";
	private static String PROPERTY_TYPE_IS_RELATIONSHIP = "isRelationship";
	private static String PROPERTY_ENTITY_TYPE = "propertyEntityType";
	private static String PROPERTY_ENTITY_ID = "esfingePropertyEntityId";
	private static String ENTITY_CLASS = "esfingeEntityClass";
	private static String ENTITY_PROPERTY = "esfingeEntityProperty";
	private static String ENTITY_ENTITY_TYPE_ID = "esfingeEntityEntityTypeID";
	private static String ENTITY_TYPE_NAME = "esfingeEntityTypeName";
	private static String ENTITY_TYPE_PACKAGE = "esfingeEntityTypePackage";
	private static String ENTITY_TYPE_PROPERTY_TYPES = "esfingeEntityTypePropertyTypes";
	private static String ENTITY_TYPE_CLASS = "esfingeEntityTypeClass";
	private static String ENTITY_TYPE_OBJECT = "esfingeEntityTypeObject";
	private static String PROPERTY_TYPE_TYPE = "esfingePropertyTypeType";
	private static String PROPERTY_TYPE_NAME = "esfingePropertyTypeName";
	private static String PROPERTY_TYPE_CLASS = "esfingePropertyTypeClass";
	private static String PROPERTY_TYPE_OBJECT = "esfingePropertyTypeObject";
	
	private static final Label LABEL_ENTITY_TYPE_CLASS = DynamicLabel.label(ENTITY_TYPE_CLASS);
	
	private static final DynamicRelationshipType RELATIONSHIP_ENTITY_TYPE_PROPERTY_TYPE =
			DynamicRelationshipType.withName(ENTITY_TYPE_PROPERTY_TYPES);
	private static final DynamicRelationshipType RELATIONSHIP_PROPERTY_TYPE_ENTITY_TYPE =
			DynamicRelationshipType.withName(PROPERTY_TYPE_IS_RELATIONSHIP);
	private static final DynamicRelationshipType RELATIONSHIP_ENTITY_PROPERTY = 
			DynamicRelationshipType.withName(ENTITY_PROPERTY);
	private static final DynamicRelationshipType RELATIONSHIP_ENTITY_TYPE_OBJECT = 
			DynamicRelationshipType.withName(ENTITY_TYPE_OBJECT);
	private static final DynamicRelationshipType RELATIONSHIP_PROPERTY_TYPE_OBJECT = 
			DynamicRelationshipType.withName(PROPERTY_TYPE_OBJECT);
	
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
		this.graphdb = new GraphDatabaseFactory().newEmbeddedDatabase(new File(databaseName));

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
		Transaction t = beginTx();
		
		try {
			Index<Node> index = getIndex(clazz);
			String entityOrEntityTypeId = getEntityTypeId(entityType);
			Node node = index.get(entityOrEntityTypeId, id).getSingle();

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
		
		Transaction t = beginTx();
		try {
			
			Node findNode = findEntityByIDAndEntityType(id, entityType);
			
	        if(findNode != null){
				
				if(findNode.hasProperty(ENTITY_CLASS)) {
					String dsClass = (String) findNode.getProperty(ENTITY_CLASS);
					entityVisitor.initVisit(id, entityType, dsClass);
				} else {
					entityVisitor.initVisit(id, entityType);
				}
				
				for (Relationship relationship : findNode.getRelationships(RELATIONSHIP_ENTITY_PROPERTY)) {
					Node propertyGraphNode = relationship.getEndNode();
					String propertyName = propertyGraphNode.getLabels().iterator().next().name();
					Boolean isRelationshipProperty = (Boolean) propertyGraphNode.getProperty(PROPERTY_IS_RELATIONSHIP);
					if (isRelationshipProperty) {
						String entityTypeId = (String) propertyGraphNode.getProperty(PROPERTY_ENTITY_TYPE);
						String entityId = (String) propertyGraphNode.getProperty(PROPERTY_ENTITY_ID);
						entityVisitor.visitRelationship(propertyName, entityTypeId, entityId);
					} else {
						Object value = propertyGraphNode.getProperty(PROPERTY_VALUE);
						entityVisitor.visitProperty(propertyName, value);
					}
					
				}
				
				successTx(t);
				return entityVisitor.endVisit();
			} else {
				successTx(t);
				return null;
			}
			
		} catch (Exception e) {
			failureTx(t);
			throw new EsfingeAOMException(e);
		}
		
	}

	private Node findEntityByIDAndEntityType(Object id, IEntityType entityType) throws EsfingeAOMException {
		Node findNode = null;
		ResourceIterator<Node> findNodes = graphdb.findNodes(DynamicLabel.label(entityType.getName()));
		for (Node entityTypeGraphNode : IteratorUtil.asCollection(findNodes)) {
			Iterable<Relationship> typeObjectsRelationships = entityTypeGraphNode.getRelationships(RELATIONSHIP_ENTITY_TYPE_OBJECT);
			for (Relationship relationship : IteratorUtil.asCollection(typeObjectsRelationships)) {
				Node entityGraphNode = relationship.getEndNode();
				if(entityGraphNode.getProperty(ID_FIELD_NAME).equals(id)){
					findNode = entityGraphNode;
				}
			}
		}
		return findNode;
	}
	
	@Override
	public IEntityType getEntityType(String packageName, String name, IEntityTypeVisitor entityTypeVisitor) throws EsfingeAOMException {
		String id = getEntityTypeId(packageName, name);
		return getEntityType(id, entityTypeVisitor);
	}
	
	@Override
	public IEntityType getEntityType(String id, IEntityTypeVisitor entityTypeVisitor) throws EsfingeAOMException {

		Transaction t = beginTx();
		try {
			
			Node findEntityTypeNode = graphdb.findNode(LABEL_ENTITY_TYPE_CLASS, ID_FIELD_NAME, id);

			if (findEntityTypeNode != null) {
				String packageName = (String) findEntityTypeNode.getProperty(ENTITY_TYPE_PACKAGE);
				String name = (String)findEntityTypeNode.getProperty(ENTITY_TYPE_NAME);

				if (findEntityTypeNode.hasProperty(ENTITY_TYPE_CLASS)) {
					String dsClass = (String)findEntityTypeNode.getProperty(ENTITY_TYPE_CLASS);
					entityTypeVisitor.initVisit(packageName, name, dsClass);
				} else {
					entityTypeVisitor.initVisit(packageName, name);
				}
				
				Iterable<Relationship> propertyTypes = findEntityTypeNode.getRelationships(RELATIONSHIP_ENTITY_TYPE_PROPERTY_TYPE);
				for (Relationship relationship : propertyTypes) {
					Node propertyTypeNode = relationship.getEndNode();
					String propertyName = (String) propertyTypeNode.getProperty(PROPERTY_TYPE_NAME);
					Boolean isRelationship = (Boolean) propertyTypeNode.getProperty(PROPERTY_TYPE_IS_RELATIONSHIP);
					
					String dsClass = null;
					
					if (propertyTypeNode.hasProperty(PROPERTY_TYPE_CLASS)) {
						dsClass = (String) propertyTypeNode.getProperty(PROPERTY_TYPE_CLASS);						
					}
					
					if (!isRelationship) {
						String typeClass = (String) propertyTypeNode.getProperty(PROPERTY_TYPE_TYPE);
						Object type = getClass(typeClass);
						entityTypeVisitor.visitPropertyType(propertyName, type, dsClass);
					} else {
						String entityTypeID = (String) propertyTypeNode.getProperty(PROPERTY_TYPE_TYPE);
						entityTypeVisitor.visitRelationship(propertyName, entityTypeID, dsClass);
					}
					
				}
				successTx(t);
				return entityTypeVisitor.endVisit();
			} else {
				successTx(t);
				return null;
			}
		} catch (Exception e) {
			failureTx(t);
			throw new EsfingeAOMException(e);
		}
	}

	@Override
	public List<String> getAllEntityTypeIds() throws EsfingeAOMException {
		List<String> entityTypeIds = new ArrayList<String>();
		
		Transaction t = beginTx();
		try {
			ResourceIterator<Node> entityTypeNodes = graphdb.findNodes(LABEL_ENTITY_TYPE_CLASS);
			entityTypeNodes.forEachRemaining(
				(node) -> 
					entityTypeIds.add((String) node.getProperty(ID_FIELD_NAME))
				);
			
			successTx(t);
			return entityTypeIds;
		} catch (Exception e) {
			failureTx(t);
			throw new EsfingeAOMException(e);
		}
	}
	
	@Override
	public List<Object> getAllEntityIDsForType(IEntityType entityType) throws EsfingeAOMException {
		
		Transaction t = beginTx();
		
		try {
			
			List<Object> entityIDs = new ArrayList<Object>();
			ResourceIterator<Node> findNodes = graphdb.findNodes(DynamicLabel.label(entityType.getName()));
			findNodes.forEachRemaining(
				(entityTypeGraphNode) -> {
					Iterable<Relationship> typeObjectsRelationships = entityTypeGraphNode.getRelationships(RELATIONSHIP_ENTITY_TYPE_OBJECT);
					typeObjectsRelationships.forEach(
						(relationship) -> {
							Node entityGraphNode = relationship.getEndNode();
							Object entityTypeID = entityGraphNode.getProperty(ID_FIELD_NAME);
							entityIDs.add(entityTypeID);
						}
					);
				}
			);
			
			successTx(t);
			return entityIDs;
		} catch (Exception e) {
			failureTx(t);
			throw new EsfingeAOMException(e);
		}
	}

	private void persist(IEntity entity, PersistenceType persistenceType) throws EsfingeAOMException {

		Transaction t = beginTx();
		
		try {
			
			String entitySimpleName = entity.getClass().getName();
			Object id = entity.getProperty("id").getValue();

			Node entityGraphNode = this.createNewGraphNode(entitySimpleName);
			entityGraphNode = putAndRetrieveIfExistentNodeFromIndex(entitySimpleName, entityGraphNode, id.toString());
			
			entityGraphNode.setProperty(ID_FIELD_NAME, id);
			String entityTypeId = getEntityTypeId(entity.getEntityType());
			entityGraphNode.setProperty(ENTITY_ENTITY_TYPE_ID, entityTypeId);
			
			Node findEntityTypeNode = graphdb.findNode(LABEL_ENTITY_TYPE_CLASS, ID_FIELD_NAME, entityTypeId);
			findEntityTypeNode.createRelationshipTo(entityGraphNode, RELATIONSHIP_ENTITY_TYPE_OBJECT);
			
			for (IProperty property : entity.getProperties()) {
				Node entityPropertyGraphNode = createNewGraphNode(property.getName());
				IPropertyType propertyType = property.getPropertyType();
				boolean isRelationshipProperty = propertyType.isRelationshipProperty();
				entityPropertyGraphNode.setProperty(PROPERTY_IS_RELATIONSHIP, isRelationshipProperty);
				if (isRelationshipProperty) {
					IEntity relatedEntity = (IEntity) property.getValue();
					entityPropertyGraphNode.setProperty(PROPERTY_ENTITY_TYPE, getEntityTypeId(relatedEntity.getEntityType()));
					entityPropertyGraphNode.setProperty(PROPERTY_ENTITY_ID, relatedEntity.getProperty("id").getValue());
				} else {
					entityPropertyGraphNode.setProperty(PROPERTY_VALUE, property.getValue());
				}

				entityGraphNode.createRelationshipTo(entityPropertyGraphNode, RELATIONSHIP_ENTITY_PROPERTY);
				
				for (Relationship relationship : findEntityTypeNode.getRelationships(RELATIONSHIP_ENTITY_TYPE_PROPERTY_TYPE)) {
					Node endNode = relationship.getEndNode();
					if(endNode.getProperty(PROPERTY_TYPE_NAME).equals(property.getName())){
						endNode.createRelationshipTo(entityPropertyGraphNode, RELATIONSHIP_PROPERTY_TYPE_OBJECT);
					}
				}
				
			}
			
			Object associatedEntityObj = entity.getAssociatedObject();
			
			if (associatedEntityObj != null) {
				entityGraphNode.setProperty(ENTITY_CLASS, associatedEntityObj.getClass().getName());
			}
			
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
			String entityTypeName = entityType.getName();
			Node entityTypeGraphNode = this.createNewGraphNode(ENTITY_TYPE_CLASS, entityTypeName);

			String id = getEntityTypeId(entityType);

			entityTypeGraphNode = putAndRetrieveIfExistentNodeFromIndex(entityTypeName, entityTypeGraphNode, id);

			entityTypeGraphNode.setProperty(ID_FIELD_NAME, id);
			entityTypeGraphNode.setProperty(ENTITY_TYPE_NAME, entityTypeName);
			entityTypeGraphNode.setProperty(ENTITY_TYPE_PACKAGE, entityType.getPackageName());
			
			for (IPropertyType propertyType : entityType.getPropertyTypes()) {
				
				String propertyTypeName = propertyType.getName();
				Node propertyTypeGraphNode = this.createNewGraphNode(PROPERTY_ENTITY_TYPE, propertyTypeName);
				propertyTypeGraphNode.setProperty(PROPERTY_TYPE_NAME, propertyTypeName);
				
				Boolean isRelationship = propertyType.isRelationshipProperty();
				propertyTypeGraphNode.setProperty(PROPERTY_TYPE_IS_RELATIONSHIP, isRelationship);
				
				if (!isRelationship) {
					Class<?> propertyTypeTypeClass = (Class<?>) propertyType.getType();
					propertyTypeGraphNode.setProperty(PROPERTY_TYPE_TYPE, propertyTypeTypeClass.getName());					
				} else {
					IEntityType propertyTypeType = (IEntityType) propertyType.getType();
					String entityTypeId = getEntityTypeId(propertyTypeType);
					propertyTypeGraphNode.setProperty(PROPERTY_TYPE_TYPE, entityTypeId);
					createPropertyTypeEntityTypeRelationship(propertyTypeGraphNode, propertyTypeType, entityTypeId);
				}
				
				Object associatedPropertyType = propertyType.getAssociatedObject();	
				if (associatedPropertyType != null) {
					propertyTypeGraphNode.setProperty(PROPERTY_TYPE_CLASS, associatedPropertyType.getClass().getName());
				}
				
				graphdb.index().forNodes(entityTypeName).add(entityTypeGraphNode, propertyTypeName, propertyTypeGraphNode);
				entityTypeGraphNode.createRelationshipTo(propertyTypeGraphNode, RELATIONSHIP_ENTITY_TYPE_PROPERTY_TYPE);
			}
			
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

	private void createPropertyTypeEntityTypeRelationship(Node propertyTypeGraphNode, IEntityType propertyTypeType, String entityTypeId) throws EsfingeAOMException {
		String propertyTypeTypeName = propertyTypeType.getName();
		Node relationshipNode = this.createNewGraphNode(ENTITY_TYPE_CLASS, propertyTypeTypeName);
		relationshipNode = putAndRetrieveIfExistentNodeFromIndex(propertyTypeTypeName, relationshipNode, entityTypeId);
		propertyTypeGraphNode.createRelationshipTo(relationshipNode, RELATIONSHIP_PROPERTY_TYPE_ENTITY_TYPE);
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

	private Node createNewGraphNode(String... labels) {
		Node entityTypeGraphNode = graphdb.createNode();
		for (String label : labels) {
			entityTypeGraphNode.addLabel(DynamicLabel.label(label));
		}
		return entityTypeGraphNode;
	}

	private Node putAndRetrieveIfExistentNodeFromIndex(String nodeNameIndex, Node graphNode, String id) throws EsfingeAOMException {
		Node present = graphdb.index().forNodes(nodeNameIndex).putIfAbsent(graphNode, ID_FIELD_NAME, id);
		if(present != null) {
			graphNode.delete();
			graphNode = present;
		}
		return graphNode;
	}
	
	private void persistRelatedEntities(Object entity, Node newNode, Node present, PersistenceType persistenceType) throws EsfingeAOMException {
		for(Relationship relationship : present.getRelationships()){
			Object related = removeRelationshipIfExistsInIndex(entity, present, relationship);
			if(related != null)
				persistEachRelatedEntityRecursivily(newNode, relationship, related, persistenceType);
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
		return null;
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
