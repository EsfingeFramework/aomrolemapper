package br.inpe.dga.beans;

import org.esfinge.aom.model.rolemapper.metadata.annotations.Entity;
import org.esfinge.aom.model.rolemapper.metadata.annotations.EntityType;
import org.esfinge.aom.rolemapper.core.testclasses.propertytypetest.AccountType;

@Entity
public class Car {
	@EntityType
	private CarType carType;
	
	private String color;
	private Integer year;
	
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public CarType getCarType() {
		return carType;
	}
	public void setCarType(CarType carType) {
		this.carType = carType;
	}	
}
