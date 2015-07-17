package org.esfinge.aom.example.medical;

import org.esfinge.aom.model.rolemapper.metadata.annotations.EntityProperty;
import org.esfinge.aom.model.rolemapper.metadata.annotations.PropertyType;
import org.esfinge.aom.model.rolemapper.metadata.annotations.PropertyValue;

@EntityProperty
public class Observation {

	@PropertyType
	private ObservationType observationType;
	
	@PropertyValue
	private Object observation;

	public ObservationType getObservationType() {
		return observationType;
	}

	public void setObservationType(ObservationType observationType) {
		this.observationType = observationType;
	}

	public Object getObservation() {
		return observation;
	}

	public void setObservation(Object observation) {
		this.observation = observation;
	}

}
