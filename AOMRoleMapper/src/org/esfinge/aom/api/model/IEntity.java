package org.esfinge.aom.api.model;

import java.util.List;

import org.esfinge.aom.exceptions.EsfingeAOMException;

/**
 * Interface that defines the Entity in the common AOM core structure
 */
public interface IEntity {

	/**
	 * Gets the Entity Type for this Entity
	 * @return Entity Type for this Entity
	 * @throws EsfingeAOMException
	 */
	public IEntityType getEntityType() throws EsfingeAOMException;
	
	/**
	 * Sets the Entity Type for this Entity 
	 * @param entityType Entity Type for the Entity
	 * @throws EsfingeAOMException
	 */
	public void setEntityType(IEntityType entityType) throws EsfingeAOMException;
	
	/**
	 * Gets the Properties of this Entity
	 * @return Properties of this Entity
	 * @throws EsfingeAOMException
	 */
	public List<IProperty> getProperties() throws EsfingeAOMException;
	
	/**
	 * Set a Property of the Entity
	 * @param propertyName Name of the Property to be set (this name must be the name of the corresponding
	 * Property Type)
	 * @param propertyValue Value of the Property
	 * @throws EsfingeAOMException
	 */
	public void setProperty(String propertyName, Object propertyValue) throws EsfingeAOMException;
	
	/**
	 * Remove a Property from the Entity
	 * @param propertyName Name of the Property to be removed (defined by the corresponding Property Type)
	 * @throws EsfingeAOMException 
	 */
	public void removeProperty(String propertyName) throws EsfingeAOMException;
	
	/**
	 * Get the IProperty object which corresponds to the IPropertyType with the name given as argument
	 * @param propertyName Name of the Property Type for which the corresponding Property will be returned
	 * @return IProperty object that corresponds to the IPropertyType with name equals to propertyName
	 * @throws EsfingeAOMException
	 */
	public IProperty getProperty (String propertyName) throws EsfingeAOMException;

	/**
	 * If the object is an Adapter, returns the adapted object. Otherwise, returns null.
	 * @return If the object is an Adapter, returns the adapted object. Otherwise, returns null.
	 */
	public Object getAssociatedObject ();
}
