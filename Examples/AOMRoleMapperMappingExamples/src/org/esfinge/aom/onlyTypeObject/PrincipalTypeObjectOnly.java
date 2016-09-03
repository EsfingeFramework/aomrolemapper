package org.esfinge.aom.onlyTypeObject;

import net.sf.esfinge.aom.api.model.IEntity;
import net.sf.esfinge.aom.api.model.IEntityType;
import net.sf.esfinge.aom.exceptions.EsfingeAOMException;
import net.sf.esfinge.aom.model.rolemapper.core.AdapterEntity;
import net.sf.esfinge.aom.model.rolemapper.core.AdapterEntityType;


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
