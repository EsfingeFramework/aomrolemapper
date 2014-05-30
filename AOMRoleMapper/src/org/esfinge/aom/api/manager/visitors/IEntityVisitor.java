package org.esfinge.aom.api.manager.visitors;

import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.api.model.IEntityType;
import org.esfinge.aom.exceptions.EsfingeAOMException;

/**
 * This class is responsible for creating and populating an Entity 
 */
public interface IEntityVisitor {

	/**
	 * Initiates the visit creating a new Entity 
	 * @param id ID for the new Entity 
	 * @param entityType Entity Type of the Entity to be created
	 * @throws EsfingeAOMException
	 */
	public void initVisit(Object id, IEntityType entityType) throws EsfingeAOMException;
	
	/**
	 * Initiates the visit creating a new Entity 
	 * @param id ID for the new Entity 
	 * @param entityType Entity Type of the Entity to be created
	 * @param dsClass dsClass Class name for the object of the domain-specific application that will be adapted by the
	 * Entity to be created
	 * @throws EsfingeAOMException
	 */
	public void initVisit(Object id, IEntityType entityType, String dsClass) throws EsfingeAOMException;
	
	/**
	 * Sets a Property to the Entity being created
	 * @param propertyName Name that identifies the Property Type of the Property to be set
	 * @param propertyValue Value of the Property
	 * @throws EsfingeAOMException
	 */
	public void visitProperty (String propertyName, Object propertyValue) throws EsfingeAOMException;
	
	/**
	 * Sets a relationship Property to the Entity
	 * @param propertyName Name that identifies the Property Type of the Property to be set
	 * @param entityTypeId ID of the EntityType of the Entity that will relate to this Entity
	 * @param entityId ID of the Entity that will relate to this Entity
	 * @throws EsfingeAOMException
	 */
	public void visitRelationship (String propertyName, String entityTypeId, String entityId) throws EsfingeAOMException;
	
	/**
	 * Finishes the creation of the Entity 
	 * @return Created Entity 
	 * @throws EsfingeAOMException
	 */
	public IEntity endVisit() throws EsfingeAOMException;
}
