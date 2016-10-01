package net.sf.esfinge.aom.model.impl;

import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import net.sf.esfinge.aom.api.model.IEntity;
import net.sf.esfinge.aom.api.model.IProperty;
import net.sf.esfinge.aom.api.model.RuleObject;
import net.sf.esfinge.aom.exceptions.EsfingeAOMException;

public class CalculaAnos implements RuleObject {

	String paramName;
	
	public CalculaAnos(String paramName){
		this.paramName = paramName;
	}
	
	@Override
	public Object execute(IEntity obj, Object[] params) {
		try {
			IProperty property = obj.getProperty(paramName);
			GenericProperty gn = (GenericProperty) property;
			Date dataObj = (Date) gn.getValue();
			
			GregorianCalendar gregorianCalendar = new GregorianCalendar();
			gregorianCalendar.setTime(dataObj);
			
			LocalDate today = LocalDate.now();
			LocalDate birthday = LocalDate.of(gregorianCalendar.get(Calendar.YEAR), gregorianCalendar.get(Calendar.MONTH), gregorianCalendar.get(Calendar.DAY_OF_MONTH));
			Period p = Period.between(birthday, today);

			return p.getYears();
		} catch (EsfingeAOMException e) {
			e.printStackTrace();
		}
		return null;
	}

}
