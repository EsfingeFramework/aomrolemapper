package org.esfinge.aom.api.modelretriever;

import java.util.List;

import org.esfinge.aom.api.manager.visitors.IEntityTypeVisitor;
import org.esfinge.aom.api.manager.visitors.IEntityVisitor;
import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.api.model.IEntityType;
import org.esfinge.aom.exceptions.EsfingeAOMException;

/**
 * Interface that should be implemented by persistence frameworks 
 */
public interface IModelRetriever {

	/**
	 * Saves an Entity object in a persistent storage
	 * @param entity Entity to be saved
	 * @throws EsfingeAOMException
	 */
	public void save (IEntity entity) throws EsfingeAOMException;
	
	/**
	 * Updates an Entity object in the persistent storage
	 * @param entity Entity to be updated
	 * @throws EsfingeAOMException
	 */
	public void update (IEntity entity) throws EsfingeAOMException;
	
	/**
	 * Inserts an Entity object in the persistent storage
	 * @param entity Entity to be inserted
	 * @throws EsfingeAOMException
	 */
	public void insert (IEntity entity) throws EsfingeAOMException;
	
	/**
	 * Removes an Entity from the persistent storage
	 * @param id ID of the Entity to be removed
	 * @param entityType Entity Type of the Entity to be removed
	 * @throws EsfingeAOMException
	 */
	public void removeEntity (Object id, IEntityType entityType) throws EsfingeAOMException;
	
	/**
	 * Removes an Entity from the persistent storage
	 * @param entity Entity to be removed
	 * @throws EsfingeAOMException
	 */
	public void removeEntity (IEntity entity) throws EsfingeAOMException;
	
	/**
	 * Updates an Entity Type object in the persistent storage
	 * @param entity Entity Type to be updated
	 * @throws EsfingeAOMException
	 */
	public void update (IEntityType entityType) throws EsfingeAOMException;

	/**
	 * Inserts an Entity Type object in the persistent storage
	 * @param entity Entity Type to be inserted
	 * @throws EsfingeAOMException
	 */
	public void insert (IEntityType entityType) throws EsfingeAOMException;
	
	/**
	 * Saves an Entity Type object in a persistent storage
	 * @param entity Entity Type to be saved
	 * @throws EsfingeAOMException
	 */
	public void save (IEntityType entityType) throws EsfingeAOMException;
	
	/**
	 * Removes an Entity Type from the persistent storage
	 * @param entity Entity Type to be removed
	 * @throws EsfingeAOMException
	 */
	public void removeEntityType (IEntityType entityType) throws EsfingeAOMException;
	
	/**
	 * Gets the IDs of all Entity Types
	 * @return IDs of all Entity Types
	 * @throws EsfingeAOMException
	 */
	public List<String> getAllEntityTypeIds() throws EsfingeAOMException;
	
	/**
	 * Gets the IDs for all the Entities of a given Entity Type
	 * @param entityType Entity Type to be queried
	 * @return IDs of all the Entities of the given Entity Type
	 * @throws EsfingeAOMException
	 */
	public List<Object> getAllEntityIDsForType(IEntityType entityType) throws EsfingeAOMException;
	
	/**
	 * Gets an Entity from the persistent storage
	 * @param id ID of the Entity to be returned
	 * @param entityType Entity Type of the Entity to be returned
	 * @param entityVisitor Entity Visitor to be used to create the Entity in the memory
	 * @return Loaded Entity
	 * @throws EsfingeAOMException
	 */
	public IEntity getEntity (Object id, IEntityType entityType, IEntityVisitor entityVisitor) throws EsfingeAOMException;
	
	/**
	 * Gets an Entity Type from the persistent storage
	 * @param id ID of the Entity Type
	 * @param entityTypeVisitor Entity Type Visitor to be used to create the Entity Type in the memory
	 * @return Loaded Entity Type
	 * @throws EsfingeAOMException
	 */
	public IEntityType getEntityType (String id, IEntityTypeVisitor entityTypeVisitor) throws EsfingeAOMException;
	
	/**
	 * Gets an Entity Type from the persistent storage
	 * @param packageName Name of the package of the Entity Type to be loaded
	 * @param name Name of the Entity Type to be loaded
	 * @param entityTypeVisitor Entity Type Visitor to be used to create the Entity Type in the memory
	 * @return Loaded Entity Type
	 * @throws EsfingeAOMException
	 */
	public IEntityType getEntityType (String packageName, String name, IEntityTypeVisitor entityTypeVisitor) throws EsfingeAOMException;
	
	/**
	 * Generates a unique ID to be set to an Entity
	 * @return Generated ID
	 * @throws EsfingeAOMException
	 */
	public Object generateEntityId () throws EsfingeAOMException;
	
}
