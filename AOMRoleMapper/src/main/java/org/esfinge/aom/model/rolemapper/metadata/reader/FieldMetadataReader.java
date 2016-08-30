package org.esfinge.aom.model.rolemapper.metadata.reader;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

import org.esfinge.aom.model.rolemapper.metadata.descriptors.FieldDescriptor;
import org.esfinge.aom.utils.Utils;

public class FieldMetadataReader {

	/**
	 * Gets the descriptor for a field
	 * @param c Class to be analyzed
	 * @param field Field to be analyzed
	 * @return FieldDescriptor object which corresponds to the field passed as parameter
	 */
	public FieldDescriptor getDescriptor(Class<?> c, Field field) {
		boolean collectionField = checkCollectionField(field);
		FieldDescriptor fieldDescriptor = new FieldDescriptor();

		fieldDescriptor.setFieldName(field.getName());
		fieldDescriptor.setFieldClass(field.getType());

		String fieldName = Utils.firstLetterInUppercase(field.getName());

		String setFieldMethod = "set" + fieldName;
		String getFieldMethod = "get" + fieldName;

		if (field.getGenericType().equals(boolean.class))
			getFieldMethod = "is" + fieldName;

		try {
			Method getMethod = c.getMethod(getFieldMethod);
			fieldDescriptor.setGetFieldMethod(getMethod);
		} catch (Exception e) {
		}

		try {										
			Type parameterRawType = Utils.getRawType(field.getGenericType());
			if (parameterRawType instanceof Class<?>)
			{
				Method setMethod = c.getMethod(setFieldMethod, (Class<?>)parameterRawType);
				fieldDescriptor.setSetFieldMethod(setMethod);
			}
		} catch (Exception e) {
		}				

		if (collectionField)
		{
			String addElementMethod = "add" + fieldName;
			String removeElementMethod = "remove" + fieldName;
			
			Class<?> actualTypeForGeneric = (Class<?>)Utils.getActualTypeForGenerics(field.getGenericType(), 0);
			if (actualTypeForGeneric == null)
			{
				// Collection that is not parameterized
				actualTypeForGeneric = Object.class;
			}

			fieldDescriptor.setInnerFieldClass(actualTypeForGeneric);
			
			try {
				Method addMethod = c.getMethod(addElementMethod, actualTypeForGeneric);
				fieldDescriptor.setAddElementMethod(addMethod);
			} catch (Exception e) {
			}

			try {
				Method removeMethod = c.getMethod(removeElementMethod, actualTypeForGeneric);
				fieldDescriptor.setRemoveElementMethod(removeMethod);					
			} catch (Exception e) {
			}
		}
		return fieldDescriptor;
	}

	/**
	 * Checks whether a given field is a <code>Collection</code> type
	 * 
	 * @param f <code>Field</code> to be analyzed
	 * @return <i>True</i> if the field is a <code>Collection</code> type and <i>false</i> otherwise
	 */
	private boolean checkCollectionField (Field f)
	{
		Type fieldType = f.getGenericType();

		if (fieldType instanceof ParameterizedType)
		{					
			ParameterizedType type = (ParameterizedType) fieldType;

			Class<?> rawType = (Class<?>) type.getRawType();

			if (Utils.checkImplementInterface(rawType, Collection.class))
			{
				return true;
			}
		}
		else
		{
			if (fieldType instanceof Class)
			{
				Class<?> fieldClass = (Class<?>)fieldType;
				if (Utils.checkImplementInterface(fieldClass, Collection.class))
				{
					return true;
				}
			}
		}
		return false;
	}
		
}
