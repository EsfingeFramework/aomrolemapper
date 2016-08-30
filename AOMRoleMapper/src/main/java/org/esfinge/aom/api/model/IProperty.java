package org.esfinge.aom.api.model;

import org.esfinge.aom.exceptions.EsfingeAOMException;

/**
 * Interface that defines the Property in the common AOM core structure
 */
public interface IProperty extends HasProperties{

	/**
	 * Gets the Property Type of this Property
	 * @return Property Type of this Property
	 * @throws EsfingeAOMException
	 */
	public IPropertyType getPropertyType() throws EsfingeAOMException;
	
	/**
	 * Sets the Property Type for this Property
	 * @param propertyType Property Type to be set
	 * @throws EsfingeAOMException
	 */
	public void setPropertyType(IPropertyType propertyType) throws EsfingeAOMException;
	
	/**
	 * Gets the value of this Property
	 * @return Value of this Property
	 * @throws EsfingeAOMException
	 */
	public Object getValue() throws EsfingeAOMException;
	
	/**
	 * Sets the value of this Property
	 * @param value Value to be set
	 * @throws EsfingeAOMException
	 */
	public void setValue(Object value) throws EsfingeAOMException;
	
	/**
	 * If the object is an Adapter, returns the adapted object. Otherwise, returns null.
	 * @return If the object is an Adapter, returns the adapted object. Otherwise, returns null.
	 */
	public Object getAssociatedObject ();
	
	/**
	 * Gets the name of this Property
	 * @return Name of this Property
	 * @throws EsfingeAOMException
	 */
	public String getName() throws EsfingeAOMException;
	
	/**
	 * Sets the name of this Property, valid if it do not have an associated PropertyType 
	 * @param value Name to be set
	 * @throws EsfingeAOMException
	 */
	public void setName(String value) throws EsfingeAOMException;
}
