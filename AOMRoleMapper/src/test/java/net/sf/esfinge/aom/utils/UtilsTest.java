package net.sf.esfinge.aom.utils;

import java.lang.reflect.Field;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;
import net.sf.esfinge.aom.utils.Utils;

import org.junit.Test;

public class UtilsTest {

	private ArrayList<String> list = new ArrayList<String>();
	private Map<String, Object> map = new HashMap<String, Object>();
	
	@Test
	public void testFirstLetterInUppercase() {
		String str = "this is a Test";
		Assert.assertEquals("This is a Test", Utils.firstLetterInUppercase(str));
	}

	@Test
	public void testFirstLetterInUppercase_StringNull() {
		String str = null;
		Assert.assertEquals(null, Utils.firstLetterInUppercase(str));
	}
	
	@Test
	public void testFirstLetterInUppercase_StringEmpty() {
		String str = "";
		Assert.assertEquals("", Utils.firstLetterInUppercase(str));
	}
	
	@Test
	public void testFirstLetterInUppercase_Length1() {
		String str = "t";
		Assert.assertEquals("T", Utils.firstLetterInUppercase(str));
	}
	
	@Test
	public void testGetActualTypeForGenerics() throws SecurityException, NoSuchFieldException {
		Field f = getClass().getDeclaredField("list");
		Assert.assertEquals(String.class, Utils.getActualTypeForGenerics(f.getGenericType(), 0));
	}
	
	@Test
	public void testGetActualTypeForGenerics_twoParameters() throws SecurityException, NoSuchFieldException {	
		Field f = getClass().getDeclaredField("map");
		Assert.assertEquals(String.class, Utils.getActualTypeForGenerics(f.getGenericType(), 0));
		Assert.assertEquals(Object.class, Utils.getActualTypeForGenerics(f.getGenericType(), 1));
	}

	@Test
	public void testGetActualTypeForGenerics_invalidParameterPosition() throws SecurityException, NoSuchFieldException {
		Field f = getClass().getDeclaredField("map");
		try
		{
			Assert.assertEquals(String.class, Utils.getActualTypeForGenerics(f.getGenericType(), 2));
			Assert.fail("InvalidParameterException should have been thrown");
		}
		catch (InvalidParameterException e)
		{
		}
		catch (Exception e)
		{
			Assert.fail("InvalidParameterException should have been thrown. Got " + e.getClass().toString());
		}
	}
	
	@Test
	public void testGetActualTypeForGenerics_notParameterizedType() {
		String str = new String();
		Assert.assertEquals(null, Utils.getActualTypeForGenerics(str.getClass(), 0));
	}
	
	@Test
	public void testCheckImplementInterface() {
		Assert.assertTrue(Utils.checkImplementInterface(ArrayList.class, List.class));
	}
	
	@Test
	public void testCheckImplementInterface_doNotImplement() {
		Assert.assertFalse(Utils.checkImplementInterface(String.class, List.class));
	}
	
	@Test
	public void testGetRawType() {		
		Assert.assertEquals(String.class, Utils.getRawType(String.class));
	}
	
	@Test
	public void testGetRawType_parameterized() throws SecurityException, NoSuchFieldException {
		Field f = getClass().getDeclaredField("list");
		Assert.assertEquals(ArrayList.class, Utils.getRawType(f.getGenericType()));
	}
	
	@Test
	public void testConvertToBoxingClass_boolean() {
		Assert.assertEquals(Boolean.class, Utils.convertToBoxingClass(boolean.class));
	}

	@Test
	public void testConvertToBoxingClass_byte() {
		Assert.assertEquals(Byte.class, Utils.convertToBoxingClass(byte.class));
	}
	
	@Test
	public void testConvertToBoxingClass_char() {
		Assert.assertEquals(Character.class, Utils.convertToBoxingClass(char.class));
	}
	
	@Test
	public void testConvertToBoxingClass_short() {
		Assert.assertEquals(Short.class, Utils.convertToBoxingClass(short.class));
	}
	
	@Test
	public void testConvertToBoxingClass_int() {
		Assert.assertEquals(Integer.class, Utils.convertToBoxingClass(int.class));
	}
	
	@Test
	public void testConvertToBoxingClass_long() {
		Assert.assertEquals(Long.class, Utils.convertToBoxingClass(long.class));
	}
	
	@Test
	public void testConvertToBoxingClass_float() {
		Assert.assertEquals(Float.class, Utils.convertToBoxingClass(float.class));
	}
	
	@Test
	public void testConvertToBoxingClass_double() {
		Assert.assertEquals(Double.class, Utils.convertToBoxingClass(double.class));
	}
	
	@Test
	public void testConvertToBoxingClass_String() {
		Assert.assertEquals(String.class, Utils.convertToBoxingClass(String.class));
	}
	
	@Test
	public void testValueIsAssignable_EqualType() {
		Assert.assertTrue(Utils.valueIsAssignable(String.class, "test"));
	}
	
	@Test
	public void testValueIsAssignable_FloatToDouble() {
		Assert.assertTrue(Utils.valueIsAssignable(Double.class, new Float(1.0)));
	}
	
	@Test
	public void testValueIsAssignable_LongToDouble() {
		Assert.assertTrue(Utils.valueIsAssignable(Double.class, new Long(1)));
	}
	
	@Test
	public void testValueIsAssignable_IntegerToDouble() {
		Assert.assertTrue(Utils.valueIsAssignable(Double.class, new Integer(1)));
	}
	
	@Test
	public void testValueIsAssignable_ShortToDouble() {
		Assert.assertTrue(Utils.valueIsAssignable(Double.class, new Short((short)1)));
	}
	
	@Test
	public void testValueIsAssignable_ByteToDouble() {
		Assert.assertTrue(Utils.valueIsAssignable(Double.class, new Byte((byte)1)));
	}
	
	@Test
	public void testValueIsAssignable_DoubleToFloat() {
		Assert.assertFalse(Utils.valueIsAssignable(Float.class, new Double(1)));
	}
	
	@Test
	public void testValueIsAssignable_LongToFloat() {
		Assert.assertTrue(Utils.valueIsAssignable(Float.class, new Long(1)));
	}
	
	@Test
	public void testValueIsAssignable_IntegerToFloat() {
		Assert.assertTrue(Utils.valueIsAssignable(Float.class, new Integer(1)));
	}
	
	@Test
	public void testValueIsAssignable_ShortToFloat() {
		Assert.assertTrue(Utils.valueIsAssignable(Float.class, new Short((short)1)));
	}
	
	@Test
	public void testValueIsAssignable_ByteToFloat() {
		Assert.assertTrue(Utils.valueIsAssignable(Float.class, new Byte((byte)1)));
	}
	
	@Test
	public void testValueIsAssignable_DoubleToLong() {
		Assert.assertFalse(Utils.valueIsAssignable(Long.class, new Double(1)));
	}
	
	@Test
	public void testValueIsAssignable_FloatToLong() {
		Assert.assertFalse(Utils.valueIsAssignable(Long.class, new Float(1)));
	}
	
	@Test
	public void testValueIsAssignable_IntegerToLong() {
		Assert.assertTrue(Utils.valueIsAssignable(Long.class, new Integer(1)));
	}
		
	@Test
	public void testValueIsAssignable_ShortToLong() {
		Assert.assertTrue(Utils.valueIsAssignable(Long.class, new Short((short)1)));
	}
	
	@Test
	public void testValueIsAssignable_ByteToLong() {
		Assert.assertTrue(Utils.valueIsAssignable(Long.class, new Byte((byte)1)));
	}
	
	@Test
	public void testValueIsAssignable_DoubleToInteger() {
		Assert.assertFalse(Utils.valueIsAssignable(Integer.class, new Double(1)));
	}
	
	@Test
	public void testValueIsAssignable_FloatToInteger() {
		Assert.assertFalse(Utils.valueIsAssignable(Integer.class, new Float(1)));
	}
	
	@Test
	public void testValueIsAssignable_LongToInteger() {
		Assert.assertFalse(Utils.valueIsAssignable(Integer.class, new Long(1)));
	}
		
	@Test
	public void testValueIsAssignable_ShortToInteger() {
		Assert.assertTrue(Utils.valueIsAssignable(Integer.class, new Short((short)1)));
	}
	
	@Test
	public void testValueIsAssignable_ByteToInteger() {
		Assert.assertTrue(Utils.valueIsAssignable(Integer.class, new Byte((byte)1)));
	}
	
	@Test
	public void testValueIsAssignable_DoubleToShort() {
		Assert.assertFalse(Utils.valueIsAssignable(Short.class, new Double(1)));
	}
	
	@Test
	public void testValueIsAssignable_FloatToShort() {
		Assert.assertFalse(Utils.valueIsAssignable(Short.class, new Float(1)));
	}
	
	@Test
	public void testValueIsAssignable_LongToShort() {
		Assert.assertFalse(Utils.valueIsAssignable(Short.class, new Long(1)));
	}
		
	@Test
	public void testValueIsAssignable_IntegerToShort() {
		Assert.assertFalse(Utils.valueIsAssignable(Short.class, new Integer(1)));
	}
	
	@Test
	public void testValueIsAssignable_ByteToShort() {
		Assert.assertTrue(Utils.valueIsAssignable(Short.class, new Byte((byte)1)));
	}
	
	@Test
	public void testValueIsAssignable_DoubleToByte() {
		Assert.assertFalse(Utils.valueIsAssignable(Byte.class, new Double(1)));
	}
	
	@Test
	public void testValueIsAssignable_FloatToByte() {
		Assert.assertFalse(Utils.valueIsAssignable(Byte.class, new Float(1)));
	}
	
	@Test
	public void testValueIsAssignable_LongToByte() {
		Assert.assertFalse(Utils.valueIsAssignable(Byte.class, new Long(1)));
	}
		
	@Test
	public void testValueIsAssignable_IntegerToByte() {
		Assert.assertFalse(Utils.valueIsAssignable(Byte.class, new Integer(1)));
	}
	
	@Test
	public void testValueIsAssignable_ShortToByte() {
		Assert.assertFalse(Utils.valueIsAssignable(Byte.class, new Short((short)1)));
	}
	
	@Test
	public void testValueIsAssignable_Interface() {
		Assert.assertTrue(Utils.valueIsAssignable(List.class, new ArrayList()));
	}
	
	@Test
	public void testValueIsAssignable_NotNumberToNumberType() {
		Assert.assertFalse(Utils.valueIsAssignable(Integer.class, new ArrayList()));
	}
	
	@Test
	public void testValueIsAssignable_Subclass() {
		Assert.assertTrue(Utils.valueIsAssignable(Object.class, new ArrayList()));
	}
	
	@Test
	public void testValueIsAssignable_NotAssignable() {
		Assert.assertFalse(Utils.valueIsAssignable(Map.class, new ArrayList()));
	}
}
