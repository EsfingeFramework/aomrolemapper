package org.esfinge.aom.onlyTypeObject;

import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.Entity;
import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.EntityType;
import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.FixedEntityProperty;

@Entity
public class Person {

	@EntityType
	private PersonType type;
	
	@FixedEntityProperty
	private String name;
	
	@FixedEntityProperty
	private String lastName;
	
	@FixedEntityProperty
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
