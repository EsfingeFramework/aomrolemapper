package org.esfinge.aom.onlyTypeObject;

import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.api.model.IEntityType;
import org.esfinge.aom.exceptions.EsfingeAOMException;
import org.esfinge.aom.model.rolemapper.core.AdapterEntity;
import org.esfinge.aom.model.rolemapper.core.AdapterEntityType;


public class PrincipalTypeObjectOnly {

	public static void main(String[] args) throws EsfingeAOMException {
		PersonType pt = new PersonType();
		pt.setTypeName("Child");
		IEntityType et = AdapterEntityType.getAdapter(pt);
		
		Person p = new Person();
		p.setType(pt);
		p.setName("Maria");
		p.setLastName("Guerra");
		p.setAge(8);
		
		IEntity e = AdapterEntity.getAdapter(et, p);
		System.out.println(e.getEntityType().getName());
		System.out.println(e.getEntityType().getPropertyTypes().size());		

	}

}
