package org.esfinge.aom.rolemapper.metadata.reader;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.esfinge.aom.model.rolemapper.metadata.descriptors.FieldDescriptor;
import org.esfinge.aom.model.rolemapper.metadata.reader.FieldAnnotationReader;
import org.esfinge.aom.rolemapper.metadata.reader.testclasses.ClassWithCollection;
import org.esfinge.aom.rolemapper.metadata.reader.testclasses.FieldAnnotation;
import org.esfinge.aom.rolemapper.metadata.reader.testclasses.SimpleClass;
import org.esfinge.aom.rolemapper.metadata.reader.testclasses.SingleFieldAnnotation;
import org.junit.Test;

public class FieldAnnotationReaderTest {

	private FieldAnnotationReader fieldAnnotationReader = new FieldAnnotationReader();
	
	@Test
	public void createFieldDescriptor_oneField() throws Exception {

		List<FieldDescriptor> fieldDescriptors = fieldAnnotationReader.getDescriptor(SimpleClass.class, SingleFieldAnnotation.class);

		Assert.assertEquals(1, fieldDescriptors.size());

		FieldDescriptor fieldDescriptor = fieldDescriptors.get(0);

		Assert.assertEquals("strFieldWithGetSet", fieldDescriptor.getFieldName());
		Assert.assertEquals(String.class, fieldDescriptor.getFieldClass());

		Class<?> simpleClass = SimpleClass.class;

		Method getMethod = simpleClass.getMethod("getStrFieldWithGetSet");			
		Method setMethod = simpleClass.getMethod("setStrFieldWithGetSet", String.class);

		Assert.assertEquals(getMethod, fieldDescriptor.getGetFieldMethod());
		Assert.assertEquals(setMethod, fieldDescriptor.getSetFieldMethod());
	}
	
	@Test
	public void createFieldDescriptor_simpleFields() throws Exception {
		
		List<FieldDescriptor> fieldDescriptors = fieldAnnotationReader.getDescriptor(SimpleClass.class, FieldAnnotation.class);

		int numOfAnnotatedFields = 5;
		
		Assert.assertEquals(numOfAnnotatedFields, fieldDescriptors.size());
		
		int correctNames = 0;

		ArrayList<String> foundFieldNames = new ArrayList<String>();
		
		for (FieldDescriptor fieldDescriptor : fieldDescriptors)
		{
			String fieldName = fieldDescriptor.getFieldName();
			
			if (fieldName.equals("strFieldWithGetSet") ||
				fieldName.equals("strFieldWithGet") ||
				fieldName.equals("intFieldWithSet") ||
				fieldName.equals("boolField") ||
				fieldName.equals("intFieldPrivateGet"))
			{
				correctNames++;
				
				Assert.assertFalse(foundFieldNames.contains(fieldName));
				foundFieldNames.add(fieldName);				
			}
		}		
		Assert.assertEquals(numOfAnnotatedFields, correctNames);
	}

	@Test
	public void createFieldDescriptor_noGetOrNoSet() throws Exception
	{
		List<FieldDescriptor> fieldDescriptors = fieldAnnotationReader.getDescriptor(SimpleClass.class, FieldAnnotation.class);

		Class<?> simpleClass = SimpleClass.class;
		
		for (FieldDescriptor fieldDescriptor : fieldDescriptors)
		{
			String fieldName = fieldDescriptor.getFieldName();
			
			if (fieldName.equals("strFieldWithGet"))
			{
				Method getMethod = simpleClass.getMethod("getStrFieldWithGet");			
				Assert.assertEquals(getMethod, fieldDescriptor.getGetFieldMethod());
				Assert.assertNull(fieldDescriptor.getSetFieldMethod());
			}
			else if (fieldName.equals("intFieldWithSet"))
			{			
				Method setMethod = simpleClass.getMethod("setIntFieldWithSet", int.class);
				Assert.assertEquals(setMethod, fieldDescriptor.getSetFieldMethod());
				Assert.assertNull(fieldDescriptor.getGetFieldMethod());
			}
		}			
	}
	
	@Test
	public void createFieldDescriptor_getBoolean() throws Exception
	{
		List<FieldDescriptor> fieldDescriptors = fieldAnnotationReader.getDescriptor(SimpleClass.class, FieldAnnotation.class);

		Class<?> simpleClass = SimpleClass.class;
		
		for (FieldDescriptor fieldDescriptor : fieldDescriptors)
		{
			String fieldName = fieldDescriptor.getFieldName();
			
			if (fieldName.equals("protectedBoolField"))
			{
				Method getMethod = simpleClass.getMethod("isBoolField");			
				Method setMethod = simpleClass.getMethod("setBoolField", boolean.class);

				Assert.assertEquals(getMethod, fieldDescriptor.getGetFieldMethod());
				Assert.assertEquals(setMethod, fieldDescriptor.getSetFieldMethod());
			}
		}				
	}
	
	@Test
	public void createFieldDescriptor_collectionGetSimpleField() throws Exception
	{
		List<FieldDescriptor> fieldDescriptors = fieldAnnotationReader.getDescriptor(ClassWithCollection.class, FieldAnnotation.class);

		Class<?> classWithCollection = ClassWithCollection.class;
		
		for (FieldDescriptor fieldDescriptor : fieldDescriptors)
		{
			String fieldName = fieldDescriptor.getFieldName();
			
			if (fieldName.equals("name"))
			{
				Method getMethod = classWithCollection.getMethod("getName");			
				Method setMethod = classWithCollection.getMethod("setName", String.class);

				Assert.assertEquals(getMethod, fieldDescriptor.getGetFieldMethod());
				Assert.assertEquals(setMethod, fieldDescriptor.getSetFieldMethod());
			}
		}				
	}
	
	@Test
	public void createFieldDescriptor_collectionGenerics() throws Exception
	{
		List<FieldDescriptor> fieldDescriptors = fieldAnnotationReader.getDescriptor(ClassWithCollection.class, FieldAnnotation.class);

		Class<?> classWithCollection = ClassWithCollection.class;
		
		for (FieldDescriptor fieldDescriptor : fieldDescriptors)
		{
			String fieldName = fieldDescriptor.getFieldName();
			
			if (fieldName.equals("genericsField"))
			{
				Method getMethod = classWithCollection.getMethod("getGenericsField");			
				Method setMethod = classWithCollection.getMethod("setGenericsField", Set.class);
				Method addMethod = classWithCollection.getMethod("addGenericsField", SimpleClass.class);			
				Method removeMethod = classWithCollection.getMethod("removeGenericsField", SimpleClass.class);
				
				Assert.assertEquals(getMethod, fieldDescriptor.getGetFieldMethod());
				Assert.assertEquals(setMethod, fieldDescriptor.getSetFieldMethod());
				Assert.assertEquals(addMethod, fieldDescriptor.getAddElementMethod());
				Assert.assertEquals(removeMethod, fieldDescriptor.getRemoveElementMethod());
			}
		}				
	}
	
	@Test
	public void createFieldDescriptor_collectionNoGenerics() throws Exception
	{
		List<FieldDescriptor> fieldDescriptors = fieldAnnotationReader.getDescriptor(ClassWithCollection.class, FieldAnnotation.class);

		Class<?> classWithCollection = ClassWithCollection.class;
		
		for (FieldDescriptor fieldDescriptor : fieldDescriptors)
		{
			String fieldName = fieldDescriptor.getFieldName();
			
			if (fieldName.equals("noGenericField"))
			{
				Method getMethod = classWithCollection.getMethod("getNoGenericField");			
				Method setMethod = classWithCollection.getMethod("setNoGenericField", List.class);
				Method addMethod = classWithCollection.getMethod("addNoGenericField", Object.class);			
				Method removeMethod = classWithCollection.getMethod("removeNoGenericField", Object.class);
				
				Assert.assertEquals(getMethod, fieldDescriptor.getGetFieldMethod());
				Assert.assertEquals(setMethod, fieldDescriptor.getSetFieldMethod());
				Assert.assertEquals(addMethod, fieldDescriptor.getAddElementMethod());
				Assert.assertEquals(removeMethod, fieldDescriptor.getRemoveElementMethod());
			}
		}				
	}
}
