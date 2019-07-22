package org.esfinge.aom.model.dynamic.adapted;

import java.util.List;

public interface ISensor {

	public abstract SensorType getSensorType();

	public abstract void setSensorType(SensorType SensorType);

	public abstract List<SensorProperty> getProperties();

	public abstract void setProperties(List<SensorProperty> properties);

}