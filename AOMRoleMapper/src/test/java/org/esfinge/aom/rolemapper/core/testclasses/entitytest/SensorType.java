package org.esfinge.aom.rolemapper.core.testclasses.entitytest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.esfinge.aom.model.rolemapper.metadata.annotations.CreateEntityMethod;
import org.esfinge.aom.model.rolemapper.metadata.annotations.EntityType;
import org.esfinge.aom.model.rolemapper.metadata.annotations.PropertyType;
import org.esfinge.aom.model.rolemapper.metadata.annotations.RuleMethod;

@EntityType
public class SensorType {

	
	@PropertyType
	private Set<SensorPropertyType> propertyTypes = new HashSet<SensorPropertyType>();

	public enum SensorTypeToCreate 
	{
		Sensor,
		SensorWithFixedProperties
	};
	
	private SensorTypeToCreate typeToCreate = SensorTypeToCreate.Sensor;
	
	@CreateEntityMethod
	public ISensor createSensor()
	{
		ISensor sensor = null;
		
		switch (typeToCreate)
		{
		case Sensor:
			sensor = (ISensor) new Sensor();
			break;
			
		case SensorWithFixedProperties:
			sensor = new SensorWithFixedProperties();
			break;
		}		
		sensor.setSensorType(this);
		return sensor;
	}


	@RuleMethod
	public boolean isValid(){
		boolean ret = true;
		ISensor iSensor = createSensor();
		List<SensorProperty> properties2 = iSensor.getProperties();
		for (SensorProperty SensorProperty : properties2) {
			if(SensorProperty.getValue() == null){
				return false;
			}
		}
		return ret;
	}
	
	public Set<SensorPropertyType> getPropertyTypes() {
		return propertyTypes;
	}

	public void setPropertyTypes(Set<SensorPropertyType> propertyTypes) {
		this.propertyTypes = propertyTypes;
	}
	
	public void addPropertyTypes (SensorPropertyType propertyType)
	{
		propertyTypes.add(propertyType);
	}
	
	public void add2 (SensorPropertyType propertyType)
	{
		propertyTypes.add(propertyType);
	}
	
	public void removePropertyTypes (SensorPropertyType propertyType)
	{
		propertyTypes.remove(propertyType);
	}

	public void setTypeToCreate(SensorTypeToCreate typeToCreate) {
		this.typeToCreate = typeToCreate;
	}

}
