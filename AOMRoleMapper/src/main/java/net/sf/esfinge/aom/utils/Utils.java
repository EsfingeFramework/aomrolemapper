package net.sf.esfinge.aom.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.security.InvalidParameterException;
import java.util.HashMap;

public class Utils {

	/**
	 * Makes the first letter of the String a capital letter
	 * 
	 * @param str Base string
	 * @return <code>str</code> with the first letter in upper case. If <code>str</code> is null or empty, returns <code>str</code>.
	 */
	public static String firstLetterInUppercase (String str)
	{
		if (str != null && !str.isEmpty())
		{
			return str.substring(0, 1).toUpperCase() + str.substring(1);
		}
		
		return str;
	}
	
	/**
	 * Gets the parameter for the parameterized type passed as parameter. 
	 * 
	 * @param t Type to be checked
	 * @param typePosition Position of the parameter to be returned. For instance, if T<A,B> is passed as <code>t</code>, and 1 as 
	 * <code>typePosition</code>, B is returned 
	 * @return the Type of the parameter in position <code>typePosition</code> in the parameterized type or null if <code>t</code> is not a 
	 * parameterized type
	 */
	public static Type getActualTypeForGenerics (Type t, int typePosition)
	{
		if (t instanceof ParameterizedType)
		{
			ParameterizedType paramType = (ParameterizedType)t;
			Type[] actualTypes = paramType.getActualTypeArguments();
			if (typePosition < actualTypes.length)
				return actualTypes[typePosition];
			else 
				throw new InvalidParameterException("Invalid parameter typePosition: " + typePosition);
		}
		
		return null;
	}
	
	/**
	 * Checks whether a type implements an interface
	 * 
	 * @param type Type to be checked. 
	 * @param superInterfaceClass Interface to be checked
	 * @return <i>True</i> if <code>type</code> or its superclasses implement <code>superInterfaceClass</code> 
	 */
	public static boolean checkImplementInterface(Class<?> type, Class<?> superInterfaceClass)
	{
		for (Class<?> intf : type.getInterfaces())
		{
			if (intf.equals(superInterfaceClass))
				return true;										
		}				
		return false;
	}
	
	/**
	 * Returns the raw class for the type passed as parameter. 
	 * 
	 * @param t Type for which the raw type will be got
	 * @return If this <code>t</code> is a parameterized type, returns the <code>Type</code> corresponding to this type without the parameters. 
	 * Otherwise, returns <code>t</code> 
	 */
	public static Type getRawType (Type t)
	{
		if (t instanceof ParameterizedType)
		{
			ParameterizedType paramType = (ParameterizedType)t;
			return paramType.getRawType();
		}
		else
		{
			return t;
		}
	}
	
	/**
	 * Convert primitive types to their corresponding boxing types
	 * @param classToConvert Primitive class to be converted
	 * @return The corresponding boxing type if the parameter is a primitive type. Otherwise,
	 * returns the parameter sent to the method.
	 */
	public static Class<?> convertToBoxingClass (Class<?> classToConvert)
	{
		if (classToConvert.isPrimitive())
		{
			if (classToConvert.equals(boolean.class))
			{
				return Boolean.class;
			}
			if (classToConvert.equals(byte.class))
			{
				return Byte.class;
			}
			if (classToConvert.equals(short.class))
			{
				return Short.class;
			}
			if (classToConvert.equals(char.class))
			{
				return Character.class;
			}
			if (classToConvert.equals(int.class))
			{
				return Integer.class;
			}
			if (classToConvert.equals(long.class))
			{
				return Long.class;
			}
			if (classToConvert.equals(float.class))
			{
				return Float.class;
			}
			if (classToConvert.equals(double.class))
			{
				return Double.class;
			}
		}
		return classToConvert;
	}
	
	/**
	 * Check whether the given value can be assigned
	 * @param classToCheck Class to be checked
	 * @param value Value to be checked
	 * @return True if the value can be assigned to the class
	 */
	public static boolean valueIsAssignable (Class<?> classToBeChecked, Object value)
	{
		if (classToBeChecked.equals(value.getClass()))
		{
			return true;
		}
		
		if (Number.class.isAssignableFrom(classToBeChecked))
		{
			// FIXME Tratar Autobox - quando classToBeChecked é do tipo primiário (ex.: int.class)
			/* AOMRoleMapperTest/bankingExample/savingsAccount/CreateEntity
			Caused by: net.sf.esfinge.aom.exceptions.EsfingeAOMException: The given value 0 is not valid for type accountNumber
				at net.sf.esfinge.aom.model.rolemapper.core.AdapterFixedProperty.setValue(AdapterFixedProperty.java:59)
				at net.sf.esfinge.aom.model.rolemapper.core.AdapterEntity.setProperty(AdapterEntity.java:231)
			 */
			if (Number.class.isAssignableFrom(value.getClass()))
			{
				if (classToBeChecked.equals(Double.class))
				{
					return true;
				}
				if (classToBeChecked.equals(Float.class) && !value.getClass().equals(Double.class))
				{
					return true;
				}
				if (classToBeChecked.equals(Long.class) && !value.getClass().equals(Double.class)
						&& !value.getClass().equals(Float.class))
				{
					return true;
				}
				if (classToBeChecked.equals(Integer.class) && !value.getClass().equals(Double.class)
						&& !value.getClass().equals(Float.class) && !value.getClass().equals(Long.class))
				{
					return true;
				}
				if (classToBeChecked.equals(Short.class) && value.getClass().equals(Byte.class))
				{
					return true;
				}
				return false;
			}else if (value.getClass().equals(HashMap.class)){
				return true;
			}
			else
			{
				return false;
			}
		}
				
		return classToBeChecked.isAssignableFrom(value.getClass());
	}
			
}
