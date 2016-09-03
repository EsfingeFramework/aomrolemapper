package org.esfinge.aom.beanToAOM;

import java.util.Date;

import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.Entity;
import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.FixedEntityProperty;

@Entity
public class Person {

	@FixedEntityProperty
	private String name;
	
	@FixedEntityProperty
	private String lastName;
	
	@FixedEntityProperty
	private int age;
	
	@FixedEntityProperty
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
