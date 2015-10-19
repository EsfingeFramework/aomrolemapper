
package br.inpe.dga.adapter;

import org.esfinge.aom.api.model.HasProperties;
import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.model.rolemapper.metadata.annotations.Entity;

import javax.lang.model.*;
import javax.xml.ws.Action;

import br.inpe.dga.factory.AdapterFactory;
import br.inpe.dga.utils.AnnotationMapFileName;

public class TemplatePropertyTypeAdapter {
	
	private HasProperties entity;

	public TemplatePropertyTypeAdapter(HasProperties entity) {
		this.entity = entity;
	}
	
	@Action
	public Object getMainContact(){
		try {
			IEntity entityProp = (IEntity) entity.getProperty("mainContact").getValue();
			return AdapterFactory.getInstance(AnnotationMapFileName.NAME.getName()).generate(entityProp);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}