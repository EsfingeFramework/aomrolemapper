package org.esfinge.aom.model.dynamic.adapted;

import java.util.ArrayList;
import java.util.List;

import org.esfinge.aom.model.rolemapper.metadata.annotations.Entity;
import org.esfinge.aom.model.rolemapper.metadata.annotations.EntityProperty;
import org.esfinge.aom.model.rolemapper.metadata.annotations.EntityType;
import org.esfinge.aom.model.rolemapper.metadata.annotations.FixedEntityProperty;
import org.esfinge.aom.model.rolemapper.metadata.annotations.RuleMethod;

@Entity
public class Sensor {

	@EntityType
	private SensorType SensorType;
	
	@FixedEntityProperty
	private String owner;

	@FixedEntityProperty
	private String propriedade;
	
	@EntityProperty
	private List<SensorProperty> properties = new ArrayList<SensorProperty>();

	public void addProperties (SensorProperty property)
	{
		this.properties.add(property);
	}

	public String getOwner() {
		return owner;
	}

	public List<SensorProperty> getProperties() {
		return properties;
	}
	
	public String getPropriedade() {
		return propriedade;
	}

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

	public void removeProperties (SensorProperty property)
	{
		this.properties.remove(property);
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public void setProperties(List<SensorProperty> properties) {
		this.properties = properties;
	}
	
	public void setPropriedade(String propriedade) {
		this.propriedade = propriedade;
	}
	
	public void setSensorType(SensorType SensorType) {
		this.SensorType = SensorType;
	}
}
