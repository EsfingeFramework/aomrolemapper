package org.esfinge.aom.beanToAOM;

import java.util.Date;

import org.esfinge.aom.model.rolemapper.metadata.annotations.Entity;
import org.esfinge.aom.model.rolemapper.metadata.annotations.EntityProperty;

@Entity
public class Person {

	@EntityProperty
	private String name;
	
	@EntityProperty
	private String lastName;
	
	@EntityProperty
	private int age;
	
	@EntityProperty
	private Date bithday;
	
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
	public Date getBithday() {
		return bithday;
	}
	public void setBithday(Date bithday) {
		this.bithday = bithday;
	}
	
}
