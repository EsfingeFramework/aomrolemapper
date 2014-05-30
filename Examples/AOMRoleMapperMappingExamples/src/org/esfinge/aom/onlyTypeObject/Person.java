package org.esfinge.aom.onlyTypeObject;

import org.esfinge.aom.model.rolemapper.metadata.annotations.Entity;
import org.esfinge.aom.model.rolemapper.metadata.annotations.EntityProperty;
import org.esfinge.aom.model.rolemapper.metadata.annotations.EntityType;
import org.esfinge.aom.simpletypesquare.ProductType;

@Entity
public class Person {

	@EntityType
	private PersonType type;
	
	@EntityProperty
	private String name;
	
	@EntityProperty
	private String lastName;
	
	@EntityProperty
	private int age;

	public PersonType getType() {
		return type;
	}

	public void setType(PersonType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
	
}
