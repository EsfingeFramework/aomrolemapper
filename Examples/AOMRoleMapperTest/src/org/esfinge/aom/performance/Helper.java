package org.esfinge.aom.performance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.esfinge.aom.api.model.HasProperties;
import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.api.model.IEntityType;
import org.esfinge.aom.api.model.IProperty;
import org.esfinge.aom.api.model.IPropertyType;
import org.esfinge.aom.exceptions.EsfingeAOMException;
import org.esfinge.aom.manager.ModelManager;
import org.esfinge.aom.model.factories.EntityTypeFactory;
import org.esfinge.aom.model.factories.PropertyTypeFactory;

public class Helper {

	private ModelManager manager;

	public Helper(ModelManager manager) {
		this.manager = manager;
	}

	public IEntityType createEntityType(String packageName, String typeName,
			String adaptedClass) {
		try {

			IEntityType entityType = EntityTypeFactory.createEntityType(
					packageName, typeName, adaptedClass);
			manager.save(entityType);

			return entityType;

		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);		
		} catch (EsfingeAOMException e) {
			throw new RuntimeException(e);
		}
	}

	public IPropertyType createPropertyType(IEntityType entityType,
			String name, Object type, String adptedClass) { 
		try {

			IPropertyType propertyType = PropertyTypeFactory
					.createPropertyType(name, type, adptedClass);
			entityType.addPropertyType(propertyType);
			manager.save(entityType);

			return propertyType;

		} catch (EsfingeAOMException e) {
			throw new RuntimeException(e);
		}
	}

	public IEntity createEntity(IEntityType entityType) {
		try {

			IEntity entity = entityType.createNewEntity();
			manager.save(entity);

			return entity;

		} catch (EsfingeAOMException e) {
			throw new RuntimeException(e);
		}
	}

	public void setProperty(IEntity entity, IPropertyType propertyType,
			Object value) {
		try {

			entity.setProperty(propertyType.getName(), value);
			manager.save(entity);

		} catch (EsfingeAOMException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * The hardest operation
	 */
	public Map<String, List<String>> selectStar(IEntityType entityType) {

		Map<String, List<String>> result = new HashMap<String, List<String>>();

		try {

			createListsForColumns(entityType, result);
			populateLines(entityType, result);

		} catch (EsfingeAOMException e) {
			throw new RuntimeException(e);
		}

		return result;
	}

	private void createListsForColumns(IEntityType entityType,
			Map<String, List<String>> result) throws EsfingeAOMException {

		for (IPropertyType propertyType : entityType.getPropertyTypes()) {
			result.put(propertyType.getName(), new ArrayList<String>());
		}
	}

	private void populateLines(IEntityType entityType,
			Map<String, List<String>> result) throws EsfingeAOMException {

		List<IEntity> entities = manager.getEntitiesForType(entityType);
		for (HasProperties entity : entities) {
			populateLine(result, entity);
		}
	}

	private void populateLine(Map<String, List<String>> result, HasProperties entity)
			throws EsfingeAOMException {
		
		for (IProperty property : entity.getProperties()) {

			Object value = property.getValue();
			String stringValue = (value==null) ? "NULL" : value.toString();
			
			result.get(property.getPropertyType().getName()).add(stringValue);
		}
	}
}