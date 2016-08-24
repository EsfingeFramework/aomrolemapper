package org.esfinge.aom.rolemapper.metadata.reader.testclasses;

public class SimpleClass {

	@FieldAnnotation
	@SingleFieldAnnotation
	private String strFieldWithGetSet;
	
	@FieldAnnotation
	private String strFieldWithGet;
	
	@FieldAnnotation
	private int intFieldWithSet;
	
	@FieldAnnotation
	private boolean boolField;
	
	@FieldAnnotation
	private int intFieldPrivateGet;
	
	private boolean noAnnotationField;

	public String getStrFieldWithGetSet() {
		return strFieldWithGetSet;
	}

	public void setStrFieldWithGetSet(String strFieldWithGetSet) {
		this.strFieldWithGetSet = strFieldWithGetSet;
	}

	public String getStrFieldWithGet() {
		return strFieldWithGet;
	}

	public void setIntFieldWithSet(int intFieldWithSet) {
		this.intFieldWithSet = intFieldWithSet;
	}

	public boolean isNoAnnotationField() {
		return noAnnotationField;
	}

	public void setNoAnnotationField(boolean noAnnotationField) {
		this.noAnnotationField = noAnnotationField;
	}

	private int getIntFieldPrivateGet() {
		return intFieldPrivateGet;
	}

	public void setIntFieldPrivateGet(int intFieldPrivateGet) {
		this.intFieldPrivateGet = intFieldPrivateGet;
	}

	public boolean isBoolField() {
		return boolField;
	}

	public void setBoolField(boolean boolField) {
		this.boolField = boolField;
	}

}
