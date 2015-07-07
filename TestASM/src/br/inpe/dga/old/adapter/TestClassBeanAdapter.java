package br.inpe.dga.old.adapter;

import org.esfinge.aom.api.model.HasProperties;
import org.esfinge.aom.exceptions.EsfingeAOMException;

public class TestClassBeanAdapter {

	private HasProperties entity;

	public TestClassBeanAdapter(HasProperties entity) {
		this.entity = entity;
	}
	public TestClassBeanAdapter() {
		
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

}
