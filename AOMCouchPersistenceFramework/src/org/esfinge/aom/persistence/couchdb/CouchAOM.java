package org.esfinge.aom.persistence.couchdb;

import java.util.ArrayList;
import java.util.HashMap;
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
import org.lightcouch.CouchDbClient;
import org.lightcouch.CouchDbProperties;
import org.lightcouch.DesignDocument;
import org.lightcouch.Document;
import org.lightcouch.NoDocumentException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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
		dbClientConfig = new CouchDbProperties().setCreateDbIfNotExist(true).setDbName("couchaom").setHost("localhost")
				.setPort(5984).setProtocol("http");
	}

	private void initDatabase() {
		String dbNamePrefix = dbClientConfig.getDbName();
		dbClientConfig.setDbName(dbNamePrefix + "-entity");
		dbEntity = new CouchDbClient(dbClientConfig);
		dbClientConfig.setDbName(dbNamePrefix + "-entity_type");
		dbEntityType = new CouchDbClient(dbClientConfig);
		
		DesignDocument entityDesignDoc = dbEntity.design().getFromDesk("entity");
		dbEntity.design().synchronizeWithDb(entityDesignDoc);
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
		Document doc = dbEntity.find(Document.class, id.toString());
		dbEntity.remove(doc);
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
		
		Document docEntityType = dbEntityType.find(Document.class, id);
		dbEntityType.remove(docEntityType);
		
		List<Object> entitiesOfType = getAllEntityIDsForType(entityType);
		for (Object idObject : entitiesOfType) {
			Document entityDoc = dbEntity.find(Document.class, (String)idObject);
			dbEntity.remove(entityDoc);
		}
	}

	@Override
	public List<String> getAllEntityTypeIds() throws EsfingeAOMException {
		List<String> entityTypeIds = new ArrayList<>();
		List<JsonObject> allDocs = dbEntityType.view("_all_docs").query(JsonObject.class);

		for (JsonObject d : allDocs) {
			entityTypeIds.add(d.get("id").getAsString());
		}

		return entityTypeIds;
	}

	@Override
	public List<Object> getAllEntityIDsForType(IEntityType entityType) throws EsfingeAOMException {
		List<Object> entityIDs = new ArrayList<Object>();
		String entityTypeId = getEntityTypeId(entityType);
		
		List<JsonObject> docs = dbEntity.view("entity/by_entityEntityTypeId")
				.key(entityTypeId)
				.query(JsonObject.class);
		
		for (JsonObject doc : docs) {
			entityIDs.add(doc.get("id").getAsString());
		}
		
		return entityIDs;
	}

	@Override
	public IEntity getEntity(Object id, IEntityType entityType, IEntityVisitor entityVisitor)
			throws EsfingeAOMException {
		
		try {
			JsonObject jsonEntity = dbEntity.find(JsonObject.class, (String)id);
			
			if (jsonEntity.has(ENTITY_CLASS)) {
				String dsClass = jsonEntity.get(ENTITY_CLASS).getAsString();
				entityVisitor.initVisit(id, entityType, dsClass);
			} else {
				entityVisitor.initVisit(id, entityType);
			}
			
			JsonObject properties = jsonEntity.getAsJsonObject(ENTITY_PROPERTIES);
			for (Map.Entry<String, JsonElement> entry : properties.entrySet()) {
				String propertyName = entry.getKey();
				JsonObject propertyFields = entry.getValue().getAsJsonObject();
				Boolean isRelationshipProperty = propertyFields.get(PROPERTY_IS_RELATIONSHIP).getAsBoolean();
				if (isRelationshipProperty) {
					String entityTypeId = propertyFields.get(PROPERTY_ENTITY_TYPE).getAsString();
					String entityId = propertyFields.get(PROPERTY_ENTITY_ID).getAsString();
					entityVisitor.visitRelationship(propertyName, entityTypeId, entityId);
				} else {
					Object value = (Object)propertyFields.get(PROPERTY_VALUE).getAsString();
					entityVisitor.visitProperty(propertyName, value);
				}
			}
			return entityVisitor.endVisit();
				
		} catch (NoDocumentException e) {
			return null;
		} catch (Exception e) {
			throw new EsfingeAOMException(e);
		}
	}

	@Override
	public IEntityType getEntityType(String id, IEntityTypeVisitor entityTypeVisitor) throws EsfingeAOMException {
		try {
			JsonObject jsonEntityType = dbEntityType.find(JsonObject.class, id);
			String packageName = jsonEntityType.get(ENTITY_TYPE_PACKAGE).getAsString();
			String name = jsonEntityType.get(ENTITY_TYPE_NAME).getAsString();
			
			if (jsonEntityType.has(ENTITY_TYPE_CLASS)) {
				String dsClass = jsonEntityType.get(ENTITY_TYPE_CLASS).getAsString();
				entityTypeVisitor.initVisit(packageName, name, dsClass);
			} else {
				entityTypeVisitor.initVisit(packageName, name);
			}
			
			JsonObject propertyTypes = jsonEntityType.getAsJsonObject(ENTITY_TYPE_PROPERTY_TYPES);
			for (Map.Entry<String, JsonElement> entry : propertyTypes.entrySet()) {
				String propertyName = entry.getKey();
				JsonObject propertyTypeFields = entry.getValue().getAsJsonObject();
				Boolean isRelationship = propertyTypeFields.get(PROPERTY_TYPE_IS_RELATIONSHIP).getAsBoolean();
				Object type = null; 
				String dsClass = null;
				
				if (propertyTypeFields.has(PROPERTY_TYPE_CLASS)) {
					dsClass = propertyTypeFields.get(PROPERTY_TYPE_CLASS).getAsString();
				}
				
				if (!isRelationship)
				{
					String typeClass = propertyTypeFields.get(PROPERTY_TYPE_TYPE).getAsString();
					type = getClass(typeClass);
					entityTypeVisitor.visitPropertyType(propertyName, type, dsClass);
				}
				else
				{
					String entityTypeID = propertyTypeFields.get(PROPERTY_TYPE_TYPE).getAsString();
					entityTypeVisitor.visitRelationship(propertyName, entityTypeID, dsClass);
				}
			}
			return entityTypeVisitor.endVisit();
		} catch (NoDocumentException e) {
			return null;			
		} catch (Exception e) {
			throw new EsfingeAOMException(e);
		}
	}

	@Override
	public IEntityType getEntityType(String packageName, String name, IEntityTypeVisitor entityTypeVisitor)
			throws EsfingeAOMException {
		String id = getEntityTypeId(packageName, name);
		return getEntityType(id, entityTypeVisitor);
	}

	@Override
	public Object generateEntityId() throws EsfingeAOMException {
		return UUID.randomUUID().toString();
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
}
