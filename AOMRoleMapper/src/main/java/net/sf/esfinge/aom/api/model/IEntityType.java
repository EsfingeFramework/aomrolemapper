package net.sf.esfinge.aom.api.model;

import java.util.List;

import net.sf.esfinge.aom.exceptions.EsfingeAOMException;

/**
 * Interface that defines the Entity Type in the common AOM core structure
 */
public interface IEntityType extends HasProperties {

	/**
	 * Gets the package name for the Entity Type
	 * 
	 * @return Entity Type's package name
	 */
	public String getPackageName();

	/**
	 * Sets the package name for the Entity Type
	 * 
	 * @param packageName
	 *            Package name for the Entity Type
	 */
	public void setPackageName(String packageName);

	/**
	 * Gets the name of the Entity Type
	 * 
	 * @return Name of the Entity Type
	 * @throws EsfingeAOMException
	 */
	public String getName() throws EsfingeAOMException;

	/**
	 * Sets the name for the Entity Type
	 * 
	 * @param name
	 *            Name for the Entity Type
	 * @throws EsfingeAOMException
	 */
	public void setName(String name) throws EsfingeAOMException;

	/**
	 * Creates a new IEntity object whose type is this Entity Type
	 * 
	 * @return Newly created IEntity object
	 * @throws EsfingeAOMException
	 */
	public IEntity createNewEntity() throws EsfingeAOMException;

	/**
	 * Get the Property Types for this Entity Type
	 * 
	 * @return Property Types for this Entity Type
	 * @throws EsfingeAOMException
	 */
	public List<IPropertyType> getPropertyTypes() throws EsfingeAOMException;

	/**
	 * Adds a Property Type for this Entity Type
	 * 
	 * @param propertyType
	 *            Property Type to be added
	 * @throws EsfingeAOMException
	 */
	public void addPropertyType(IPropertyType propertyType) throws EsfingeAOMException;

	/**
	 * Removes a Property Type from this Entity Type
	 * 
	 * @param propertyType
	 *            Property Type to be removed
	 * @throws EsfingeAOMException
	 */
	public void removePropertyType(IPropertyType propertyType) throws EsfingeAOMException;

	/**
	 * Removes a Property Type from this Entity Type
	 * 
	 * @param propertyName
	 *            Name of the Property Type to be removed
	 * @throws EsfingeAOMException
	 */
	public void removePropertyType(String propertyName) throws EsfingeAOMException;

	/**
	 * Gets the Property Type with the name given as argument
	 * 
	 * @param name
	 *            Name of the Property Type to be returned
	 * @return Property Type with the name given as argument
	 * @throws EsfingeAOMException
	 */
	public IPropertyType getPropertyType(String name) throws EsfingeAOMException;

	/**
	 * If the object is an Adapter, returns the adapted object. Otherwise,
	 * returns null.
	 * 
	 * @return If the object is an Adapter, returns the adapted object.
	 *         Otherwise, returns null.
	 */
	public Object getAssociatedObject();

	/**
	 * 
	 * @param name
	 * @param rule
	 */
	public void addOperation(String name, RuleObject rule);

	/**
	 * 
	 * @param name
	 * @return
	 */
	public RuleObject getOperation(String name);

}
