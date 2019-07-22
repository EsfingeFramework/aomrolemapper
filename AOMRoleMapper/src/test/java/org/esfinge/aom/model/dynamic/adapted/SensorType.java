package org.esfinge.aom.model.dynamic.adapted;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.esfinge.aom.model.rolemapper.metadata.annotations.CreateEntityMethod;
import org.esfinge.aom.model.rolemapper.metadata.annotations.EntityType;
import org.esfinge.aom.model.rolemapper.metadata.annotations.PropertyType;
import org.esfinge.aom.model.rolemapper.metadata.annotations.RuleMap;
import org.esfinge.aom.model.rolemapper.metadata.annotations.RuleMethod;

@EntityType
public class SensorType {

	@RuleMap
	private Map<String, OperacaoSensor> operations = new HashMap<>();

	@PropertyType
	private Set<SensorPropertyType> propertyTypes = new HashSet<SensorPropertyType>();

	public void addPropertyTypes(SensorPropertyType propertyType) {
		propertyTypes.add(propertyType);
	}

	@RuleMethod
	public int converterUnidade(String propriedade, String unidade) {
		if (propriedade != null) {
			for (SensorPropertyType sensorPropertyType : propertyTypes) {
				System.out.println(sensorPropertyType.getName());
			}
		}
		return 1;
	}

	@CreateEntityMethod
	public ISensor createSensor() {
		System.out.println("createSensor");
		ISensor sensor = null;
		sensor = (ISensor) new Sensor();
		sensor.setSensorType(this);
		if (operations == null) {
			operations = new HashMap<>();
		}
		return sensor;
	}

	public Map<String, OperacaoSensor> getOperations() {
		return operations;
	}

	public Set<SensorPropertyType> getPropertyTypes() {
		return propertyTypes;
	}

	public void removePropertyTypes(SensorPropertyType propertyType) {
		propertyTypes.remove(propertyType);
	}

	public void setOperations(Map<String, OperacaoSensor> operations) {
		this.operations = operations;
	}

	public void setPropertyTypes(Set<SensorPropertyType> propertyTypes) {
		this.propertyTypes = propertyTypes;
	}

	public void addOperation(String name, OperacaoRegraSensor rule) {
		operations.put(name, rule);
	}

//	public void addOperacao(String name, OperacaoRegraSensor rule) {
//		operations.put(name, rule);
//	}
}
