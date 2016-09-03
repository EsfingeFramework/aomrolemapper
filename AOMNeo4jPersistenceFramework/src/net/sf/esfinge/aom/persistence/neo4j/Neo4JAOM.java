package net.sf.esfinge.aom.persistence.neo4j;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;
import org.neo4j.helpers.collection.Iterables;

import net.sf.esfinge.aom.api.manager.visitors.IEntityTypeVisitor;
import net.sf.esfinge.aom.api.manager.visitors.IEntityVisitor;
import net.sf.esfinge.aom.api.model.IEntity;
import net.sf.esfinge.aom.api.model.IEntityType;
import net.sf.esfinge.aom.api.model.IProperty;
import net.sf.esfinge.aom.api.model.IPropertyType;
import net.sf.esfinge.aom.api.modelretriever.IModelRetriever;
import net.sf.esfinge.aom.exceptions.EsfingeAOMException;

public class Neo4JAOM implements IModelRetriever {

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
	
	private static final Label LABEL_ENTITY_TYPE_CLASS = Label.label(ENTITY_TYPE_CLASS);
	private static final Label LABEL_PROPERTY_ENTITY_TYPE = Label.label(PROPERTY_ENTITY_TYPE);
	
	private static final RelationshipType RELATIONSHIP_ENTITY_TYPE_PROPERTY_TYPE =
			RelationshipType.withName(ENTITY_TYPE_PROPERTY_TYPES);
	private static final RelationshipType RELATIONSHIP_PROPERTY_TYPE_ENTITY_TYPE =
			RelationshipType.withName(PROPERTY_TYPE_IS_RELATIONSHIP);
	private static final RelationshipType RELATIONSHIP_ENTITY_PROPERTY = 
			RelationshipType.withName(ENTITY_PROPERTY);
	private static final RelationshipType RELATIONSHIP_ENTITY_TYPE_OBJECT = 
			RelationshipType.withName(ENTITY_TYPE_OBJECT);
	private static final RelationshipType RELATIONSHIP_PROPERTY_TYPE_OBJECT = 
			RelationshipType.withName(PROPERTY_TYPE_OBJECT);
	
	private Neo4JAOMConfiguration neo4jAomConfig;
	
	private String databaseName;
	
	private enum PersistenceType  {
		Save,
		Update,
		Insert
	}
	
	public Neo4JAOM() throws EsfingeAOMException {		
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
		
		Runtime.getRuntime().addShutdownHook(new Thread(
			() -> {
				graphdb.shutdown();
				System.out.println("Neo4J Database has been safely shutdown.");
			}
		));
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
		Transaction t = beginTx();
		
		try {
			Node findNode = findEntityByIDAndEntityType(id, entityType);
			if(findNode != null) {
				String entityLabel = findNode.getLabels().iterator().next().name();
				deleteNodeRelationsThenNode(findNode, entityLabel);
			}
			successTx(t);
		} catch(Exception e) {
			failureTx(t);
			throw new EsfingeAOMException("The entity of " + entityType.getName() + " and Id " + id + " cannot be removed because it is part of a Relationship", e);
		}
	}

	/**
	 * To delete a node in Neo4J, all of its relationships must me deleted first.
	 * @param node
	 * @param entityLabel 
	 */
	private void deleteNodeRelationsThenNode(Node node, String entityLabel){
		for(Relationship relation : node.getRelationships(Direction.OUTGOING)){
			Node otherNode = relation.getOtherNode(node);
			relation.delete();
			deleteNodeRelationsThenNode(otherNode, entityLabel);
		}
		for(Relationship relation : node.getRelationships(Direction.INCOMING)){
			relation.delete();
		}
		removeNodeFromIndex(node, entityLabel);
		node.delete();
	}

	private void removeNodeFromIndex(Node node, String entityLabel) {
		List<Label> labels = Iterables.asList(node.getLabels());
		if(labels.contains(LABEL_ENTITY_TYPE_CLASS)) {
			graphdb.index().forNodes(ENTITY_TYPE_CLASS).remove(node);
		}
		if(labels.contains(LABEL_PROPERTY_ENTITY_TYPE)) {
			graphdb.index().forNodes(PROPERTY_ENTITY_TYPE).remove(node);
		}
		if(labels.contains(Label.label(entityLabel))) {
			graphdb.index().forNodes(entityLabel).remove(node);
		}
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

		Transaction t = beginTx();
		
		try {
			Node findNode = graphdb.findNode(LABEL_ENTITY_TYPE_CLASS, ID_FIELD_NAME, id);
			if(findNode != null)
				deleteNodeRelationsThenNode(findNode, clazz);
			
			successTx(t);
		} catch(Exception e) {
			failureTx(t);
			throw new EsfingeAOMException("The entity of " + clazz + " and Id " + id + " cannot be removed because it is part of a Relationship", e);
		}

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
		ResourceIterator<Node> findNodes = graphdb.findNodes(Label.label(entityType.getName()));
		for (Node entityTypeGraphNode : findNodes.stream().collect(Collectors.toList())) {
			Iterable<Relationship> typeObjectsRelationships = entityTypeGraphNode.getRelationships(RELATIONSHIP_ENTITY_TYPE_OBJECT);
			for (Relationship relationship : Iterables.asList(typeObjectsRelationships)) {
				Node entityGraphNode = relationship.getEndNode();
				if(entityGraphNode.getProperty(ID_FIELD_NAME).equals(id)){
					return entityGraphNode;
				}
			}
		}
		return null;
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
			entityTypeNodes.stream().forEach(
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
			ResourceIterator<Node> findNodes = graphdb.findNodes(Label.label(entityType.getName()));
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
			
			String entitySimpleName = getEntitySimpleName(entity);
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

	private String getEntitySimpleName(IEntity entity) {
		Object associatedObject = entity.getAssociatedObject();
		if(associatedObject != null){
			return associatedObject.getClass().getSimpleName();
		} else {
			return entity.getClass().getSimpleName();
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
				if(existsRelationshipForPropertyType(entityTypeGraphNode, propertyTypeName))
					continue;
				
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

	private boolean existsRelationshipForPropertyType(Node entityTypeGraphNode, String propertyTypeName) {
		List<Relationship> relationships = Iterables.asList(entityTypeGraphNode.getRelationships());
		for (Relationship relationship : relationships) {
			Node propertyTypeGraphNode = relationship.getEndNode();
			if(propertyTypeGraphNode.hasProperty(PROPERTY_TYPE_NAME) &&
			   propertyTypeGraphNode.getProperty(PROPERTY_TYPE_NAME).equals(propertyTypeName)) {
				return true;
			}
		}
		return false;
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
		for (String caption : labels) {
			entityTypeGraphNode.addLabel(Label.label(caption));
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
		t.close();
	}
	
	private void failureTx(Transaction t) {
		t.failure();
		t.close();
	}

	public Index<Node> getIndex(String indexNameSpace){
		return graphdb.index().forNodes(indexNameSpace);
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
