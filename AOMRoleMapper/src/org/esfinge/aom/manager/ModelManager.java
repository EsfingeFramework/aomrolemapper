package org.esfinge.aom.manager;

import org.esfinge.aom.api.manager.visitors.IEntityTypeVisitor;
import org.esfinge.aom.api.manager.visitors.IEntityVisitor;
import org.esfinge.aom.api.model.HasProperties;
import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.api.model.IEntityType;
import org.esfinge.aom.api.model.IProperty;
import org.esfinge.aom.api.model.IPropertyType;
import org.esfinge.aom.api.modelretriever.IModelRetriever;
import org.esfinge.aom.exceptions.EsfingeAOMException;
import org.esfinge.aom.manager.visitors.CreateEntityTypeVisitor;
import org.esfinge.aom.manager.visitors.CreateEntityVisitor;
import org.esfinge.aom.model.factories.PropertyTypeFactory;
import org.esfinge.aom.modelretriever.factories.ModelRetrieverFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Class that manages the instances created by the framework
 */
public class ModelManager {

	private IModelRetriever modelRetriever;
	
	private Map<Object, IEntity> loadedEntitiesMap = new WeakHashMap<Object,IEntity>();
	
	private Map<String, IEntityType> loadedEntityTypesMap = new WeakHashMap<String, IEntityType>();
	
	private static ModelManager instance;
	
	private static String ENTITY_ID_PROPERTY_NAME = "id";
	
	public static ModelManager getInstance()
	{
		if (instance == null)
		{
			instance = new ModelManager();
		}
		return instance;
	}
	
	private ModelManager()
	{
		modelRetriever = ModelRetrieverFactory.getInstance().getEntityManager();
	}
	
	/**
	 * Loads the model from persistent storage to memory
	 * @return Loaded Entity Types that correspond to the model
	 * @throws EsfingeAOMException
	 */
	public synchronized List<IEntityType> loadModel () throws EsfingeAOMException
	{
		List<String> entityTypesIds = modelRetriever.getAllEntityTypeIds();
		List<IEntityType> entityTypes = new ArrayList<IEntityType>();
		
		//testando
		IEntityType entityType2 = getEntityType("2");
		for (String entityTypeId : entityTypesIds)
		{
			IEntityType entityType = getEntityType(entityTypeId);
			entityTypes.add(entityType);
		}
		
		return entityTypes;
	}
	
	/**
	 * Gets all the Entities for a given Entity Type
	 * @param type Entity Type to be queried
	 * @return All the Entities for the given Entity Type
	 * @throws EsfingeAOMException
	 */
	public List<IEntity> getEntitiesForType (IEntityType type) throws EsfingeAOMException
	{
		List<Object> entityIDs = modelRetriever.getAllEntityIDsForType(type);
		List<IEntity> entities = new ArrayList<IEntity>();
		for (Object entityID : entityIDs)
		{
			entities.add(getEntity(type, entityID));
		}
		return entities;
	}

	/**
	 * Saves an Entity Type to the persistent storage
	 * @param entityType Entity Type to be saved
	 * @throws EsfingeAOMException
	 */
	public synchronized void save (IEntityType entityType) throws EsfingeAOMException
	{
		insertIDPropertyType(entityType);
		modelRetriever.save(entityType);
		loadedEntityTypesMap.put(getEntityTypeId(entityType), entityType);
	}
	
	/**
	 * Saves an Entity to the persistent storage
	 * @param entity Entity to be saved
	 * @throws EsfingeAOMException
	 */
	public synchronized void save (IEntity entity) throws EsfingeAOMException
	{
		IProperty property = entity.getProperty(ENTITY_ID_PROPERTY_NAME);

		if (property == null || property.getValue() == null)
		{
			Object id = modelRetriever.generateEntityId();
			entity.setProperty(ENTITY_ID_PROPERTY_NAME, id);
		}
		modelRetriever.save(entity);
		loadedEntitiesMap.put(entity.getProperty(ENTITY_ID_PROPERTY_NAME).getValue(), entity);
	}
	
	/**
	 * Loads an Entity Type from the persistent storage
	 * @param packageName Package name of the Entity Type
	 * @param name Name of the Entity Type
	 * @return Loaded Entity Type
	 * @throws EsfingeAOMException
	 */
	public synchronized IEntityType getEntityType(String packageName, String name) throws EsfingeAOMException
	{
		String entityTypeId = getEntityTypeId(packageName, name);
		return getEntityType(entityTypeId);
	}
	
	/**
	 * Loads an Entity Type from the persistent storage
	 * @param entityTypeId ID of the Entity Type
	 * @return Loaded Entity Type
	 * @throws EsfingeAOMException
	 */
	public synchronized IEntityType getEntityType(String entityTypeId) throws EsfingeAOMException	{
		
		if (!loadedEntityTypesMap.containsKey(entityTypeId))
		{
			IEntityTypeVisitor visitor = new CreateEntityTypeVisitor(); 
			IEntityType entityType = modelRetriever.getEntityType(entityTypeId, visitor);
			if (entityType != null)
			{
				loadedEntityTypesMap.put(entityTypeId, entityType);
				return entityType;
			}
			else
			{
				return null;
			}
		}
		return loadedEntityTypesMap.get(entityTypeId);
	}
	
	/**
     * Reloads the entity passed as parameter and returns it
     * @param entity
     * @return The fresh Entity
     */
    public synchronized HasProperties getFreshEntity(IEntity entity) throws EsfingeAOMException {
        Object id = entity.getProperty(ENTITY_ID_PROPERTY_NAME).getValue();
        loadedEntitiesMap.remove(id);
        return getEntity(entity.getEntityType(), id);
    }

    /**
	 * Loads an Entity from the persistent storage
	 * @param entityType Entity Type of the Entity to be loaded
	 * @param id ID of the Entity to be loaded
	 * @return Loaded Entity
	 * @throws EsfingeAOMException
	 */
	public synchronized IEntity getEntity (IEntityType entityType, Object id) throws EsfingeAOMException
	{		
		if (!loadedEntitiesMap.containsKey(id))
		{	
			IEntityVisitor visitor = new CreateEntityVisitor();
			IEntity entity = modelRetriever.getEntity(id, entityType, visitor);
			if (entity != null)
			{
				loadedEntitiesMap.put(id, entity);
				return entity;
			}
			else
			{
				return null;
			}
		}
		return loadedEntitiesMap.get(id);
	}
	
	/**
	 * Removes an Entity from the persistent storage
	 * @param entity Entity to be removed
	 * @throws EsfingeAOMException
	 */
	public synchronized void removeEntity (IEntity entity) throws EsfingeAOMException
	{	
		loadedEntitiesMap.remove(entity.getProperty(ENTITY_ID_PROPERTY_NAME).getValue());
		modelRetriever.removeEntity(entity);
	}
	
	/**
	 * Removes an Entity from the persistent storage
	 * @param id ID of the Entity to be removed
	 * @param entityType Entity Type of the Entity to be removed
	 * @throws EsfingeAOMException
	 */
	public synchronized void removeEntity (Object id, IEntityType entityType) throws EsfingeAOMException
	{	
		loadedEntitiesMap.remove(id);
		modelRetriever.removeEntity(id, entityType);
	}
	
	/**
	 * Removes an Entity Type from the persistent storage
	 * @param entityType Entity Type to be removed
	 * @throws EsfingeAOMException
	 */
	public synchronized void removeEntityType (IEntityType entityType) throws EsfingeAOMException
	{
		String entityTypeId = getEntityTypeId(entityType.getPackageName(), entityType.getName());
		loadedEntityTypesMap.remove(entityTypeId);
		for (IEntity entity : getEntitiesForType(entityType))
		{
			removeEntity(entity);
		}		
		modelRetriever.removeEntityType(entityType);
	}
	
	/**
	 * Checks whether two Entity Types are equivalent
	 * @param entityType Entity Type 1
	 * @param entityType2 Entity Type 2
	 * @return True if the Entity Types are equivalent and false otherwise.
	 * @throws EsfingeAOMException
	 */
	public boolean equivalentEntityTypes (IEntityType entityType, IEntityType entityType2) throws EsfingeAOMException
	{
		return equivalentEntityTypes(entityType, getEntityTypeId(entityType2));
	}
	
	/**
	 * Checks whether two Entity Types are equivalent
	 * @param entityType Entity Type 1
	 * @param entityType2ID ID of the Entity Type to be compared
	 * @return True if the Entity Types are equivalent and false otherwise.
	 * @throws EsfingeAOMException
	 */
	public boolean equivalentEntityTypes (IEntityType entityType, String entityType2ID) throws EsfingeAOMException
	{
		return (getEntityTypeId(entityType).equals(entityType2ID));
	}
	
	/**
	 * Returns the Model Retriever object
	 * @return Model Retriever
	 */
	public IModelRetriever getModelRetriever ()
	{
		return modelRetriever;
	}
	
	public void resetAttributes() {
		loadedEntitiesMap = new WeakHashMap<Object,IEntity>();
		loadedEntityTypesMap = new WeakHashMap<String, IEntityType>();	
	}
	
	private void insertIDPropertyType(IEntityType entityType) throws EsfingeAOMException {
		IPropertyType id = entityType.getPropertyType(ENTITY_ID_PROPERTY_NAME);
		if (id == null)
		{
			id = PropertyTypeFactory.createPropertyType(ENTITY_ID_PROPERTY_NAME, Object.class);
			entityType.addPropertyType(id);
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
}
