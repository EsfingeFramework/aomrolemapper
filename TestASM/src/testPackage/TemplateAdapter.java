package testPackage;

import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.exceptions.EsfingeAOMException;

public class TemplateAdapter {
	
	private IEntity entity;

	public TemplateAdapter(IEntity entity) {
		this.entity = entity;
	}
	
	public Object getMainContact(){
		try {
			IEntity entityProp = (IEntity) entity.getProperty("mainContact").getValue();
			AdapterFactory af = new AdapterFactory();
			return af.generate(entityProp);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
