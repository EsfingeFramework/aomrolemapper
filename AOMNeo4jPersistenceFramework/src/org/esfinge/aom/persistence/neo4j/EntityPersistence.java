package org.esfinge.aom.persistence.neo4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.esfinge.aom.api.manager.visitors.IEntityVisitor;
import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.api.model.IEntityType;
import org.esfinge.aom.api.model.IProperty;
import org.esfinge.aom.api.model.IPropertyType;
import org.esfinge.aom.exceptions.EsfingeAOMException;
import org.lightcouch.CouchDbClient;
import org.lightcouch.CouchDbProperties;
import org.lightcouch.DesignDocument;
import org.lightcouch.Document;
import org.lightcouch.NoDocumentException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class EntityPersistence extends BasePersistence {
	private CouchDbClient db;
	
	public EntityPersistence(PersistenceConfig config) {
		initDatabase(config.getEntityPersistenceConfig());
	}
	
	public IEntity get(Object id, IEntityType entityType, IEntityVisitor entityVisitor)
			throws EsfingeAOMException {
		
		try {
			JsonObject jsonEntity = db.find(JsonObject.class, (String)id);
			
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
	
	public List<Object> listIdsByEntityType(IEntityType entityType) throws EsfingeAOMException {
		List<Object> entityIDs = new ArrayList<Object>();
		String entityTypeId = getEntityTypeId(entityType);
		
		List<JsonObject> docs = db.view("entity/by_entityEntityTypeId")
				.key(entityTypeId)
				.query(JsonObject.class);
		
		for (JsonObject doc : docs) {
			entityIDs.add(doc.get("id").getAsString());
		}
		
		return entityIDs;
	}
	
	public void persist(IEntity entity, PersistenceType persistenceType) throws EsfingeAOMException {
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
					Document doc = db.find(Document.class, id.toString());
					entityMap.put("_rev", doc.getRevision());
					db.update(entityMap);
				} catch (NoDocumentException e) {
					db.save(entityMap);
				}
			} else {
				db.save(entityMap);
			}

		} catch (Exception e) {
			throw new EsfingeAOMException(e);
		}
	}
	
	public void removeByEntityType(IEntityType entityType) throws EsfingeAOMException {
		List<Object> entitiesOfType = listIdsByEntityType(entityType);
		for (Object idObject : entitiesOfType) {
			Document doc = db.find(Document.class, idObject.toString());
			db.remove(doc);
		}
	}
	
	public void removeById(String id) {
		Document doc = db.find(Document.class, id);
		db.remove(doc);
	}
	
	private void initDatabase(CouchDbProperties config) {
		openEntityDatabase(config);
		syncEntityDesignDocs();
	}
	
	private void openEntityDatabase(CouchDbProperties config) {
		db = new CouchDbClient(config);
	}
	
	private void syncEntityDesignDocs() {
		DesignDocument entityDesignDoc = db.design().getFromDesk("entity");
		db.design().synchronizeWithDb(entityDesignDoc);
	}
}
