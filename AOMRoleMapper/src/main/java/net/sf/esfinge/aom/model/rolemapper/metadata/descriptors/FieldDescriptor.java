package net.sf.esfinge.aom.model.rolemapper.metadata.descriptors;

import java.lang.reflect.Method;

public class FieldDescriptor {

	private String fieldName;
		
	private Class<?> fieldClass;
	
	private Method getFieldMethod;
	
	private Method setFieldMethod;
	
	private Method addElementMethod;
	
	private Method removeElementMethod;
	
	private Class<?> innerFieldClass;

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public Method getGetFieldMethod() {
		return getFieldMethod;
	}

	public void setGetFieldMethod(Method getFieldMethod) {
		this.getFieldMethod = getFieldMethod;
	}

	public Method getSetFieldMethod() {
		return setFieldMethod;
	}

	public void setSetFieldMethod(Method setFieldMethod) {
		this.setFieldMethod = setFieldMethod;
	}

	public Class<?> getFieldClass() {
		return fieldClass;
	}

	public void setFieldClass(Class<?> fieldClass) {
		this.fieldClass = fieldClass;
	}
	
	public Method getAddElementMethod() {
		return addElementMethod;
	}

	public void setAddElementMethod(Method addElementMethod) {
		this.addElementMethod = addElementMethod;
	}

	public Method getRemoveElementMethod() {
		return removeElementMethod;
	}

	public void setRemoveElementMethod(Method removeElementMethod) {
		this.removeElementMethod = removeElementMethod;
	}

	public Class<?> getInnerFieldClass() {
		return innerFieldClass;
	}

	public void setInnerFieldClass(Class<?> innerFieldClass) {
		this.innerFieldClass = innerFieldClass;
	}
}
