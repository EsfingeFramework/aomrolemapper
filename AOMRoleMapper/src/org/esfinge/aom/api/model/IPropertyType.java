package org.esfinge.aom.api.model;

import org.esfinge.aom.exceptions.EsfingeAOMException;

/**
 * Interface that defines the Property Type in the common AOM core structure
 */
public interface IPropertyType {
	
	/**
	 * Gets the name of this Property Type
	 * @return Name of this Property Type
	 * @throws EsfingeAOMException
	 */
	public String getName() throws EsfingeAOMException;
	
	/**
	 * Sets the name of this Property Type
	 * @param name Name to be set
	 * @throws EsfingeAOMException
	 */
	public void setName(String name) throws EsfingeAOMException;
	
	/**
	 * Gets the type of this Property Type. This type can be an IEntityType object or a Class object
	 * that indicates the valid values for a primitive Property.
	 * @return Type of this Property Type
	 * @throws EsfingeAOMException
	 */
	public Object getType() throws EsfingeAOMException;
	
	/**
	 * Sets the type of this Property Type. This type can be an IEntityType object or a Class object
	 * that indicates the valid values for a primitive Property.
	 * @param type Type to be set
	 * @throws EsfingeAOMException
	 */
	public void setType (Object type) throws EsfingeAOMException;
	
	/**
	 * If the object is an Adapter, returns the adapted object. Otherwise, returns null.
	 * @return If the object is an Adapter, returns the adapted object. Otherwise, returns null.
	 */
	public Object getAssociatedObject ();
	
	/**
	 * Returns the type of this Property Type as a String
	 * @return Type of this Property Type as a String
	 * @throws EsfingeAOMException
	 */
	public String getTypeAsString() throws EsfingeAOMException;
	
	/**
	 * If this Property Type corresponds to an association, returns true. If this is a primitive Property Type
	 * returns false.
	 * @return True if this Property Type is an association. False if this Property Type is a primitive type.
	 * @throws EsfingeAOMException
	 */
	public boolean isRelationshipProperty() throws EsfingeAOMException;
	
	/**
	 * Given a value to be set to a Property, validates it against the type of this Property Type.
	 * @param value Value to be validated
	 * @return True if the value can be set to a Property whose Property Type is equal to this Property Type. 
	 * False otherwise
	 * @throws EsfingeAOMException
	 */
	public boolean isValidValue(Object value) throws EsfingeAOMException;
}