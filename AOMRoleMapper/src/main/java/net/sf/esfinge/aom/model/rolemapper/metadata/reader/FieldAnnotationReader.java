package net.sf.esfinge.aom.model.rolemapper.metadata.reader;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import net.sf.esfinge.aom.exceptions.EsfingeAOMException;
import net.sf.esfinge.aom.model.rolemapper.metadata.descriptors.FieldDescriptor;

public class FieldAnnotationReader extends FieldMetadataReader {

	/**
	 * Given a class and an annotation, this method returns a list of <code>FieldDescriptor</code>
	 * that contains information related to the annotated fields of the class.
	 * 
	 * @param c <code>Class</code> object that is to be searched for annotations
	 * @param annotationClass Class of the annotation to be searched. The annotation must be applicable to fields
	 * @return A list of <code>FieldDescriptor</code> objects that contain information related to the fields annotated by <i>annotationClass</i> 
	 * @throws EsfingeAOMException TODO
	 */
	public List<FieldDescriptor> getDescriptor (Class<?> c, Class annotationClass) throws EsfingeAOMException
	{		
		ArrayList<FieldDescriptor> fieldDescriptorList = new ArrayList<FieldDescriptor>();
		
		for (Field field : c.getDeclaredFields())
		{
			Annotation annotation = field.getAnnotation(annotationClass);

			if (annotation != null)
			{
				FieldDescriptor fieldDescriptor = getDescriptor(c, field);

				fieldDescriptorList.add(fieldDescriptor);
			}
		}

		return fieldDescriptorList;
	}
	
}
