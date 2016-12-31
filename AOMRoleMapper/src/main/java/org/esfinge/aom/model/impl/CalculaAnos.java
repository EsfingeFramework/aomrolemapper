package org.esfinge.aom.model.impl;

import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.GregorianCalendar;

import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.api.model.IProperty;
import org.esfinge.aom.api.model.RuleObject;
import org.esfinge.aom.exceptions.EsfingeAOMException;

public class CalculaAnos implements RuleObject {

	String paramName;
	
	public CalculaAnos(String paramName){
		this.paramName = paramName;
	}
	
	@Override
	public Object execute(IEntity obj, Object...params) {
		try {
			IProperty property = obj.getProperty(paramName);
			GenericProperty gn = (GenericProperty) property;
			Date dataObj = (Date) gn.getValue();
			
			GregorianCalendar gregorianCalendar = new GregorianCalendar();
			gregorianCalendar.setTime(dataObj);
			
			LocalDate today = LocalDate.now();
			int year = 1;
			int month = 2;
			int day = 5;
			LocalDate birthday = LocalDate.of(gregorianCalendar.get(year), gregorianCalendar.get(month), gregorianCalendar.get(day));
			Period p = Period.between(birthday, today);

			return p.getYears();
		} catch (EsfingeAOMException e) {
			e.printStackTrace();
		}
		return null;
	}

}
