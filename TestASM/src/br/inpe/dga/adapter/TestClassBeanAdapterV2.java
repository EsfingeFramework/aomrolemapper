package br.inpe.dga.adapter;

import org.esfinge.aom.api.model.HasProperties;
import org.esfinge.aom.exceptions.EsfingeAOMException;

public class TestClassBeanAdapterV2 {

	private HasProperties entity;

	public TestClassBeanAdapterV2(HasProperties entity) {
		this.entity = entity;
	}
	public TestClassBeanAdapterV2() {
		
	}
	
	public Object getExtraProperty() {
		try {
			return entity.getProperty("extraProperty").getValue();
		} catch (EsfingeAOMException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void setExtraProperty(Object a) {
		try {
			entity.setProperty("extraProperty", a);
		} catch (EsfingeAOMException e) {
			throw new RuntimeException(e);
		}
	}
	
	public Integer getIntegerProperty() {
		try {
			return (Integer)entity.getProperty("integerProperty").getValue();
		} catch (EsfingeAOMException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void setIntegerProperty(Integer a) {
		try {
			entity.setProperty("integerProperty", a);
		} catch (EsfingeAOMException e) {
			throw new RuntimeException(e);
		}
	}

}


