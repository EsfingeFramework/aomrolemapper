package org.esfinge.aom.rolemapper.core.testclasses.entitytest;

import java.util.ArrayList;
import java.util.List;

import org.esfinge.aom.model.rolemapper.metadata.annotations.Entity;
import org.esfinge.aom.model.rolemapper.metadata.annotations.EntityProperty;
import org.esfinge.aom.model.rolemapper.metadata.annotations.EntityType;
import org.esfinge.aom.model.rolemapper.metadata.annotations.FixedEntityProperty;
import org.esfinge.aom.model.rolemapper.metadata.annotations.RuleMethod;

@Entity
public class Sensor implements ISensor {

	@EntityType
	private SensorType SensorType;
	
	@FixedEntityProperty
	private String owner;
	
	@EntityProperty
	private List<SensorProperty> properties = new ArrayList<SensorProperty>();

	public SensorType getSensorType() {
		return SensorType;
	}
	
	@RuleMethod
	public boolean isValid(){
		boolean ret = true;
		ISensor iSensor = SensorType.createSensor();
		List<SensorProperty> properties2 = iSensor.getProperties();
		for (SensorProperty SensorProperty : properties2) {
			if(SensorProperty.getValue() == null){
				return false;
			}
		}
		return ret;
	}

	public void setSensorType(SensorType SensorType) {
		this.SensorType = SensorType;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public List<SensorProperty> getProperties() {
		return properties;
	}

	public void setProperties(List<SensorProperty> properties) {
		this.properties = properties;
	}
	
	public void addProperties (SensorProperty property)
	{
		this.properties.add(property);
	}
	
	public void removeProperties (SensorProperty property)
	{
		this.properties.remove(property);
	}
}
