package org.esfinge.aom.model.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import org.esfinge.aom.api.model.HasProperties;
import org.esfinge.aom.api.model.IEntityType;
import org.esfinge.aom.api.model.IProperty;
import org.esfinge.aom.api.model.IPropertyType;
import org.esfinge.aom.exceptions.EsfingeAOMException;

public abstract class ThingWithProperties implements HasProperties {

	protected Map<IPropertyType, IProperty> properties = new WeakHashMap<IPropertyType, IProperty>();

	public ThingWithProperties() {
		super();
	}

	public abstract IEntityType getEntityType(); // esse cara pode ser nulo

	@Override
	public List<IProperty> getProperties() throws EsfingeAOMException {

		ArrayList<IProperty> result = new ArrayList<IProperty>();

		if (getEntityType() != null) {
			List<IPropertyType> validPropertyTypes = getEntityType().getPropertyTypes();
			List<IPropertyType> invalidPropertyTypes = new ArrayList<IPropertyType>();
			for (IProperty property : properties.values()) {
				IPropertyType propertyType = property.getPropertyType();
				if (validPropertyTypes.contains(propertyType)) {
					result.add(property);
				} else {
					invalidPropertyTypes.add(propertyType);
				}
			}

			for (IPropertyType propertyType : invalidPropertyTypes) {
				properties.remove(propertyType);
			}
		} else {
			for (IProperty property : properties.values()) {
				result.add(property);
			}
		}

		return result;
	}

	@Override
	public void setProperty(String propertyName, Object propertyValue) throws EsfingeAOMException {

		if (propertyName.contains(".")) {
			String parts[] = propertyName.split("\\.");

			IPropertyType propertyType = findPropertyType(parts[0]);
			propertyValue = convertNumberType(propertyValue, propertyType);
			boolean isMapped = isIn(propertyType);

			if (isMapped) {
				Map<String, Object> atributosValor = getAttrMap(propertyType.getName());
				atributosValor.put(parts[1], propertyValue);
				properties.clear();
				properties.put(propertyType, new GenericProperty(propertyType, atributosValor));
			} else {
				Map<String, Object> atributosValor = new HashMap<String, Object>();
				atributosValor.put(parts[1], propertyValue);

				propertyValue = convertNumberType(propertyValue, propertyType);
				properties.put(propertyType, new GenericProperty(propertyType, atributosValor));
			}
		} else {
			IPropertyType propertyType = findPropertyType(propertyName);
			if (properties.containsKey(propertyType)) {
				properties.get(propertyName).setValue(propertyValue);
			} else {
				propertyValue = convertNumberType(propertyValue, propertyType);
				properties.put(propertyType, new GenericProperty(propertyType, propertyValue));
			}
		}
	}

	private Map<String, Object> getAttrMap(String name) throws EsfingeAOMException {
		Collection<IProperty> values = properties.values();
		for (IProperty iProperty : values) {
			if (iProperty.getName().equals(name)) {
				@SuppressWarnings("unchecked")
				Map<String, Object> map = (Map<String, Object>) iProperty.getValue();
				return map;
			}
		}
		return new HashMap<>();
	}

	private boolean isIn(IPropertyType propertyType) throws EsfingeAOMException {
		Set<IPropertyType> keySet = properties.keySet();
		for (IPropertyType iPropertyType : keySet) {
			if (iPropertyType.getName().equals(propertyType.getName())) {
				return true;
			}
		}
		return false;
	}

	private Object convertNumberType(Object propertyValue, IPropertyType propertyType) throws EsfingeAOMException {
		if (this instanceof GenericPropertyType) {
			GenericPropertyType gn = (org.esfinge.aom.model.impl.GenericPropertyType) this;
			if (isNumericType(gn.type)) {
				boolean equalType = gn.type.equals(propertyValue.getClass());

				propertyType.setType(gn.type);
				if (propertyValue instanceof Long) {
					propertyValue = ((Long) propertyValue).longValue();
				} else if (propertyValue instanceof Double) {
					propertyValue = ((Double) propertyValue).doubleValue();
				} else if (propertyValue instanceof Integer) {
					if (gn.type.toString().contains("Double")) {
						propertyValue = ((Integer) propertyValue).longValue();
					} else {
						propertyValue = ((Integer) propertyValue).longValue();
					}
					if (equalType) {
						propertyType.setType(propertyValue.getClass());
					}
				} else if (propertyValue instanceof Short) {
					propertyValue = ((Short) propertyValue).longValue();
					if (equalType) {
						propertyType.setType(propertyValue.getClass());
					}
				} else if (propertyValue instanceof Float) {
					propertyValue = ((Float) propertyValue).longValue();
					if (equalType) {
						propertyType.setType(propertyValue.getClass());
					}
				} else if (propertyValue instanceof Byte) {
					propertyValue = ((Byte) propertyValue).longValue();
					if (equalType) {
						propertyType.setType(propertyValue.getClass());
					}
				} else if (propertyValue instanceof HashMap) {
					@SuppressWarnings("unchecked")
					Map<String, ?> map = ((HashMap<String, ?>) propertyValue);
					Map<String, Long> mapNew = new HashMap<>();
					Set<String> keys = map.keySet();
					Long value = 0L;
					for (String key : keys) {
						Object obj = map.get(key);
						if (obj instanceof Integer) {
							value = ((Integer) obj).longValue();
						}
						mapNew.put(key, value);
					}
					propertyValue = mapNew;
				}
			} else {
				propertyType.setType(propertyValue.getClass());
			}
		}else{
			propertyType.setType(propertyValue.getClass());
		}
		return propertyValue;
	}

	private boolean isNumericType(Object type) {
		String name1 = type.toString();
		String[] numerics = { "Double", "Float", "Long", "Integer", "Short", "Byte" };
		for (int i = 0; i < numerics.length; i++) {
			if (name1.contains(numerics[i])) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void removeProperty(String propertyName) throws EsfingeAOMException {
		IPropertyType propertyType = findPropertyType(propertyName);
		properties.remove(propertyType);
	}

	@Override
	public IProperty getProperty(String propertyName) throws EsfingeAOMException {
		IPropertyType propertyType = findPropertyType(propertyName);

		if (propertyType != null) {
			return properties.get(propertyType);
		}

		return null;
	}

	private IPropertyType findPropertyType(String propertyName) throws EsfingeAOMException {

		IPropertyType propertyType = getPropertyTypeFromEntityType(propertyName);

		if (propertyType == null) {
			propertyType = new GenericPropertyType(propertyName, null);
		}

		return propertyType;
	}

	private IPropertyType getPropertyTypeFromEntityType(String propertyName) throws EsfingeAOMException {
		if (getEntityType() != null) {
			return getEntityType().getPropertyType(propertyName);
		}

		return null;
	}
}