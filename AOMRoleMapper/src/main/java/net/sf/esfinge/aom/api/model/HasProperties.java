package net.sf.esfinge.aom.api.model;

import java.util.List;

import net.sf.esfinge.aom.exceptions.EsfingeAOMException;

public interface HasProperties {

	/**
	 * Gets the Properties of this Entity
	 * @return Properties of this Entity
	 * @throws EsfingeAOMException
	 */
	public abstract List<IProperty> getProperties() throws EsfingeAOMException;

	/**
	 * Set a Property of the Entity
	 * @param propertyName Name of the Property to be set (this name must be the name of the corresponding
	 * Property Type)
	 * @param propertyValue Value of the Property
	 * @throws EsfingeAOMException
	 */
	public abstract void setProperty(String propertyName, Object propertyValue)
			throws EsfingeAOMException;

	/**
	 * Remove a Property from the Entity
	 * @param propertyName Name of the Property to be removed (defined by the corresponding Property Type)
	 * @throws EsfingeAOMException 
	 */
	public abstract void removeProperty(String propertyName)
			throws EsfingeAOMException;

	/**
	 * Get the IProperty object which corresponds to the IPropertyType with the name given as argument
	 * @param propertyName Name of the Property Type for which the corresponding Property will be returned
	 * @return IProperty object that corresponds to the IPropertyType with name equals to propertyName
	 * @throws EsfingeAOMException
	 */
	public abstract IProperty getProperty(String propertyName)
			throws EsfingeAOMException;

}