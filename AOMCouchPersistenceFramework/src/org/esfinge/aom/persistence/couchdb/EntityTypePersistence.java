package org.esfinge.aom.persistence.couchdb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lightcouch.CouchDbClient;
import org.lightcouch.CouchDbProperties;
import org.lightcouch.Document;
import org.lightcouch.NoDocumentException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.sf.esfinge.aom.api.manager.visitors.IEntityTypeVisitor;
import net.sf.esfinge.aom.api.model.IEntityType;
import net.sf.esfinge.aom.api.model.IPropertyType;
import net.sf.esfinge.aom.exceptions.EsfingeAOMException;

public class EntityTypePersistence extends BasePersistence {
	private CouchDbClient db;
	
	public EntityTypePersistence(PersistenceConfig config) {
		initDatabase(config.getEntityTypePersistenceConfig());
	}
	
	public IEntityType get(String id, IEntityTypeVisitor entityTypeVisitor) throws EsfingeAOMException {
		try {
			JsonObject jsonEntityType = db.find(JsonObject.class, id);
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
	
	public List<String> listIds() throws EsfingeAOMException {
		List<String> entityTypeIds = new ArrayList<>();
		List<JsonObject> allDocs = db.view("_all_docs").query(JsonObject.class);

		for (JsonObject d : allDocs) {
			entityTypeIds.add(d.get("id").getAsString());
		}

		return entityTypeIds;
	}
	
	public void persist(IEntityType entityType, PersistenceType persistenceType) throws EsfingeAOMException {
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
					Document doc = db.find(Document.class, id.toString());
					entityTypeMap.put("_rev", doc.getRevision());
					db.update(entityTypeMap);
				} catch (NoDocumentException e) {
					db.save(entityTypeMap);
				}
			} else {
				db.save(entityTypeMap);
			}

		} catch (Exception e) {
			throw new EsfingeAOMException(e);
		}
	}
	
	public void removeById(String id) {
		Document doc = db.find(Document.class, id);
		db.remove(doc);
	}
	
	private void initDatabase(CouchDbProperties config) {
		openEntityDatabase(config);
	}
	
	private void openEntityDatabase(CouchDbProperties config) {
		db = new CouchDbClient(config);
	}
}
