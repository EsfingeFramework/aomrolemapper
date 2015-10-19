package br.inpe.dga.adapter;

import org.esfinge.aom.api.model.HasProperties;
import org.esfinge.aom.api.model.IEntity;

import br.inpe.dga.factory.AdapterFactory;
import br.inpe.dga.utils.AnnotationMapFileName;

public class TemplateAdapter {
	
	private HasProperties entity;

	public TemplateAdapter(HasProperties entity) {
		this.entity = entity;
	}
	
	public String getMainContact(){
		try {
			IEntity entityProp = (IEntity) entity.getProperty("mainContact").getValue();
			return (String) AdapterFactory.getInstance(AnnotationMapFileName.NAME.getName()).generate(entityProp);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public void setMainContact(Object entity){
		try {
			IEntity entityProp = (IEntity) entity;
			this.entity.getProperty("mainContact").
						setValue(entityProp);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}