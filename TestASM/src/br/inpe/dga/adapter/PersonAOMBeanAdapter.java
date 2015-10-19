package br.inpe.dga.adapter;

import org.esfinge.aom.api.model.HasProperties;
import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.model.rolemapper.metadata.annotations.Entity;
import org.esfinge.comparison.annotation.IgnoreInComparison;
import org.esfinge.comparison.annotation.Tolerance;
import org.junit.Ignore;

import javax.lang.model.*;

import br.inpe.dga.factory.AdapterFactory;

@Entity
public class PersonAOMBeanAdapter {	
	private HasProperties hasProperty;

	public PersonAOMBeanAdapter(HasProperties entityProperty) {
		this.hasProperty = entityProperty;
	}
	
	@Tolerance(value = 0.1)
	public Boolean getPersistence(){
		try {
			Boolean valueProperty = (Boolean) hasProperty.getProperty("persistence").getValue();
			return valueProperty;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}