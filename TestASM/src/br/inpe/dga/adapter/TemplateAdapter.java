package br.inpe.dga.adapter;

import org.esfinge.aom.api.model.HasProperties;
import org.esfinge.aom.api.model.IEntity;

import br.inpe.dga.factory.AdapterFactory;

public class TemplateAdapter {
	
	private HasProperties entity;

	public TemplateAdapter(HasProperties entity) {
		this.entity = entity;
	}
	
	public Object getMainContact(){
		try {
			IEntity entityProp = (IEntity) entity.getProperty("mainContact").getValue();
			return AdapterFactory.getInstance().generate(entityProp);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
