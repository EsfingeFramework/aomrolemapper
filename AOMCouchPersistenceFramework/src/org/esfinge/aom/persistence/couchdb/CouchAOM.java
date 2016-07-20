package org.esfinge.aom.persistence.couchdb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.esfinge.aom.api.manager.visitors.IEntityTypeVisitor;
import org.esfinge.aom.api.manager.visitors.IEntityVisitor;
import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.api.model.IEntityType;
import org.esfinge.aom.api.model.IProperty;
import org.esfinge.aom.api.model.IPropertyType;
import org.esfinge.aom.api.modelretriever.IModelRetriever;
import org.esfinge.aom.exceptions.EsfingeAOMException;
import org.lightcouch.CouchDbClient;
import org.lightcouch.CouchDbProperties;
import org.lightcouch.Document;
import org.lightcouch.NoDocumentException;

public class CouchAOM implements IModelRetriever {

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

	private CouchDbProperties dbClientConfig;
	private CouchDbClient dbEntity;
	private CouchDbClient dbEntityType;

	private enum PersistenceType {
		Save, Update, Insert
	}

	public CouchAOM() {
		loadDatabaseConfig();
		initDatabase();
	}

	private void loadDatabaseConfig() {
		dbClientConfig = new CouchDbProperties().setCreateDbIfNotExist(true).setDbName("couchaom")
				.setHost("localhost").setPort(5984).setProtocol("http");
	}

	private void initDatabase() {
		String dbNamePrefix = dbClientConfig.getDbName();
		dbClientConfig.setDbName(dbNamePrefix + "-entity");
		dbEntity = new CouchDbClient(dbClientConfig);
		dbClientConfig.setDbName(dbNamePrefix + "-entity_type");
		dbEntityType = new CouchDbClient(dbClientConfig);
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
		// TODO Auto-generated method stub

	}

	@Override
	public void removeEntity(IEntity entity) throws EsfingeAOMException {
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

	}

	@Override
	public List<String> getAllEntityTypeIds() throws EsfingeAOMException {
		List<String> allDocs = new ArrayList<>();

		return allDocs;
	}

	@Override
	public List<Object> getAllEntityIDsForType(IEntityType entityType) throws EsfingeAOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IEntity getEntity(Object id, IEntityType entityType, IEntityVisitor entityVisitor)
			throws EsfingeAOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IEntityType getEntityType(String id, IEntityTypeVisitor entityTypeVisitor) throws EsfingeAOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IEntityType getEntityType(String packageName, String name, IEntityTypeVisitor entityTypeVisitor)
			throws EsfingeAOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object generateEntityId() throws EsfingeAOMException {
		// TODO Auto-generated method stub
		return null;
	}

	private void persist(IEntity entity, PersistenceType persistenceType) throws EsfingeAOMException {
		try {
			Object id = entity.getProperty("id").getValue();
			Map<String, Object> entityMap = new HashMap<>();
			entityMap.put(ID_FIELD_NAME, id);

			Map<String, Object> propertiesMap = new HashMap<>();
			for (IProperty property : entity.getProperties()) {
				Map<String, Object> propertyMap = new HashMap<>();
				IPropertyType propertyType = property.getPropertyType();
				boolean isRelationshipProperty = propertyType.isRelationshipProperty();
				propertyMap.put(PROPERTY_IS_RELATIONSHIP, isRelationshipProperty);
				if (isRelationshipProperty) {
					IEntity relatedEntity = (IEntity) property.getValue();
					propertyMap.put(PROPERTY_ENTITY_TYPE, getEntityTypeId(relatedEntity.getEntityType()));
					propertyMap.put(PROPERTY_ENTITY_ID, relatedEntity.getProperty("id").getValue());
				} else {
					propertyMap.put(PROPERTY_VALUE, property.getValue());
				}
				propertiesMap.put(propertyType.getName(), propertyMap);
			}
			entityMap.put(ENTITY_PROPERTIES, propertiesMap);

			Object associatedEntityObj = entity.getAssociatedObject();
			if (associatedEntityObj != null) {
				entityMap.put(ENTITY_CLASS, associatedEntityObj.getClass().getName());
			}

			entityMap.put(ENTITY_ENTITY_TYPE_ID, getEntityTypeId(entity.getEntityType()));

			if (persistenceType != PersistenceType.Insert) {
				try {
					Document doc = dbEntity.find(Document.class, id.toString());
					entityMap.put("_rev", doc.getRevision());
					dbEntity.update(entityMap);
				} catch (NoDocumentException e) {
					dbEntity.save(entityMap);
				}
			} else {
				dbEntity.save(entityMap);
			}
			
		} catch (Exception e) {
			throw new EsfingeAOMException(e);
		}
	}

	private void persist(IEntityType entityType, PersistenceType persistenceType) throws EsfingeAOMException {

		try {
			Map<String, Object> entityTypeMap = new HashMap<>();
			String id = getEntityTypeId(entityType);
			System.out.println("persist: " + id + " / " + persistenceType.toString());
			entityTypeMap.put(ID_FIELD_NAME, id);
			entityTypeMap.put(ENTITY_TYPE_NAME, entityType.getName());
			entityTypeMap.put(ENTITY_TYPE_PACKAGE, entityType.getPackageName());

			Map<String, Object> entityTypePropertyTypes = new HashMap<>();

			for (IPropertyType propertyType : entityType.getPropertyTypes()) {
				Map<String, Object> propertyTypeObj = new HashMap<>();

				Boolean isRelationship = propertyType.isRelationshipProperty();

				if (!isRelationship) {
					Class<?> propertyTypeTypeClass = (Class<?>) propertyType.getType();
					propertyTypeObj.put(PROPERTY_TYPE_TYPE, propertyTypeTypeClass.getName());
				} else {
					IEntityType propertyTypeType = (IEntityType) propertyType.getType();
					propertyTypeObj.put(PROPERTY_TYPE_TYPE, getEntityTypeId(propertyTypeType));
				}
				propertyTypeObj.put(PROPERTY_TYPE_IS_RELATIONSHIP, isRelationship);
				Object associatedPropertyType = propertyType.getAssociatedObject();
				if (associatedPropertyType != null) {
					propertyTypeObj.put(PROPERTY_TYPE_CLASS, associatedPropertyType.getClass().getName());
				}
				entityTypePropertyTypes.put(propertyType.getName(), propertyTypeObj);
			}

			entityTypeMap.put(ENTITY_TYPE_PROPERTY_TYPES, entityTypePropertyTypes);

			Object associatedEntityType = entityType.getAssociatedObject();

			if (associatedEntityType != null) {
				entityTypeMap.put(ENTITY_TYPE_CLASS, associatedEntityType.getClass().getName());
			}
			
			if (persistenceType != PersistenceType.Insert) {
				try {
					Document doc = dbEntityType.find(Document.class, id.toString());
					entityTypeMap.put("_rev", doc.getRevision());
					dbEntityType.update(entityTypeMap);
				} catch (NoDocumentException e) {
					dbEntityType.save(entityTypeMap);
				}
			} else {
				dbEntityType.save(entityTypeMap);
			}

		} catch (Exception e) {
			throw new EsfingeAOMException(e);
		}
	}

	private String getEntityTypeId(IEntityType entityType) throws EsfingeAOMException {
		return getEntityTypeId(entityType.getPackageName(), entityType.getName());
	}

	private String getEntityTypeId(String packageName, String name) {
		return packageName + "/" + name;
	}

}
