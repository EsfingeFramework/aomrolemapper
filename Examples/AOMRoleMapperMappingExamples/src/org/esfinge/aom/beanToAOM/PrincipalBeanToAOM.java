package org.esfinge.aom.beanToAOM;

import java.util.Date;

import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.api.model.IPropertyType;
import org.esfinge.aom.exceptions.EsfingeAOMException;
import org.esfinge.aom.model.rolemapper.core.AdapterEntity;

public class PrincipalBeanToAOM {
	
	public static void main(String[] args) throws EsfingeAOMException{
		Person p = new Person();
		p.setName("Eduardo");
		p.setLastName("Guerra");
		p.setAge(33);
		p.setBithday(new Date());
		
		IEntity entity = AdapterEntity.getAdapter(null, p);
		for(IPropertyType pt : entity.getEntityType().getPropertyTypes()){
			System.out.println(entity.getProperty(pt.getName()).getValue());
		}
		
	}

}
