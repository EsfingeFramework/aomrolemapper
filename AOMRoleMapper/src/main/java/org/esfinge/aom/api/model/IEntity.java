package org.esfinge.aom.api.model;

import java.util.Map;

import org.esfinge.aom.exceptions.EsfingeAOMException;

/**
 * Interface that defines the Entity in the common AOM core structure
 */
public interface IEntity extends HasProperties {

	/**
	 * Gets the Entity Type for this Entity
	 * 
	 * @return Entity Type for this Entity
	 * @throws EsfingeAOMException
	 */
	public IEntityType getEntityType() throws EsfingeAOMException;

	/**
	 * Sets the Entity Type for this Entity
	 * 
	 * @param entityType
	 *            Entity Type for the Entity
	 * @throws EsfingeAOMException
	 */
	public void setEntityType(IEntityType entityType) throws EsfingeAOMException;

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
	 * @param params
	 * @return
	 */
	public Object executeOperation(String name, Object... params);
	
	public void addPropertyMonitored(String propertyName, String ruleName);
	
	public Object getResultOperation(String ruleName);

	public Object executeEL(String expr, Class<? extends Object> objectClass, Map<String, Object> map);
}
