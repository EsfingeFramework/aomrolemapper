package org.esfinge.aom.rolemapper.metadata.reader.testclasses;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ClassWithCollection {

	@FieldAnnotation
	private String name;
	
	@FieldAnnotation
	private Set<SimpleClass> genericsField;
	
	@FieldAnnotation
	private List noGenericField;
	
	public ClassWithCollection()
	{
		genericsField = new LinkedHashSet<SimpleClass>();
	}

	
	public Set<SimpleClass> getGenericsField() {
		return genericsField;
	}

	public void setGenericsField(Set<SimpleClass> genericsField) {
		this.genericsField = genericsField;
	}
	
	public void addGenericsField (SimpleClass simpleClass)
	{
		genericsField.add(simpleClass);
	}
	
	public void removeGenericsField (SimpleClass simpleClass)
	{
		genericsField.remove(simpleClass);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public List getNoGenericField() {
		return noGenericField;
	}

	public void setNoGenericField(List noGenericField) {
		this.noGenericField = noGenericField;
	}

	public void addNoGenericField(Object obj)
	{
		noGenericField.add(obj);
	}

	public void removeNoGenericField(Object obj)
	{
		noGenericField.remove(obj);
	}
	
}
