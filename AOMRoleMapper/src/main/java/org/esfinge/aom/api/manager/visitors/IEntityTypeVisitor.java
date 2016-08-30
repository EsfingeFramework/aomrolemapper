package org.esfinge.aom.api.manager.visitors;

import org.esfinge.aom.api.model.IEntityType;
import org.esfinge.aom.exceptions.EsfingeAOMException;

/**
 * This class is responsible for creating and populating an Entity Type
 */
public interface IEntityTypeVisitor {

	/**
	 * Initiates the visit creating a new Entity Type
	 * @param packageName Indicates the Entity Type's package name
	 * @param name Name of the Entity Type 
	 * @throws EsfingeAOMException 
	 */
	public void initVisit(String packageName, String name) throws EsfingeAOMException;
	
	/**
	 * Initiates the visit creating a new Entity Type
	 * @param packageName Indicates the Entity Type's package name
	 * @param name Name of the Entity Type 
	 * @param dsClass Class name for the object of the domain-specific application that will be adapted by the
	 * Entity Type to be created
	 * @throws EsfingeAOMException
	 */
	public void initVisit(String packageName, String name, String dsClass) throws EsfingeAOMException;
	
	/**
	 * Adds a Property Type to the Entity Type
	 * @param propertyName Name of the Property Type to be added
	 * @param type Type of the Property Type to be added
	 * @throws EsfingeAOMException
	 */
	public void visitPropertyType (String propertyName, Object type) throws EsfingeAOMException;
	
	/**
	 * Adds a Property Type to the Entity Type. This method must be called when the Property Type to be added
	 * is a primitive property type
	 * @param propertyName Name of the Property Type to be added
	 * @param type Type of the Property Type to be added
	 * @param dsClass Class name for the object of the domain-specific application that will be adapted by the
	 * Property Type to be created
	 * @throws EsfingeAOMException
	 */
	public void visitPropertyType (String propertyName, Object type, String dsClass) throws EsfingeAOMException;
	
	/**
	 * Adds a Property Type to the Entity Type. This method must be called when the Property Type to be added
	 * is an association property type
	 * @param propertyName Name of the Property Type to be added
	 * @param entityTypeId ID of the Entity Type which will relate with this Entity Type
	 * @param associatedClass Class name for the object of the domain-specific application that will be 
	 * adapted by the relationship Property Type to be created
	 * @throws EsfingeAOMException
	 */
	public void visitRelationship(String propertyName, String entityTypeId, String associatedClass) throws EsfingeAOMException;
	
	/**
	 * Adds a Property Type to the Entity Type. This method must be called when the Property Type to be added
	 * is an association property type
	 * @param propertyName Name of the Property Type to be added
	 * @param entityTypeId ID of the Entity Type which will relate with this Entity Type
	 * @throws EsfingeAOMException
	 */
	public void visitRelationship(String propertyName, String entityTypeId) throws EsfingeAOMException;
	
	/**
	 * Finishes the creation of the Entity Type
	 * @return Created Entity Type
	 * @throws EsfingeAOMException
	 */
	public IEntityType endVisit() throws EsfingeAOMException;
}
