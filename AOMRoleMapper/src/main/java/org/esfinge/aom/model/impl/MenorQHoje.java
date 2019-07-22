package org.esfinge.aom.model.impl;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.api.model.IProperty;
import org.esfinge.aom.exceptions.EsfingeAOMException;

public class MenorQHoje extends BasicRuleObject {

	String paramName;

	public MenorQHoje(String paramName) {
		this.paramName = paramName;
	}

	@Override
	public Object execute(IEntity obj, Object... params) throws EsfingeAOMException {
		try {
			IProperty property = obj.getProperty(paramName);
			GregorianCalendar gregorianCalendar = new GregorianCalendar();

			if (property.getValue() instanceof Date) {
				GenericProperty gn = (GenericProperty) property;
				Date dataObj = (Date) gn.getValue();
				gregorianCalendar.setTime(dataObj);
			}

			LocalDate today = LocalDate.now();
			LocalDate birthday = LocalDate.of(gregorianCalendar.get(Calendar.YEAR),
					gregorianCalendar.get(Calendar.MONTH)+ 1, gregorianCalendar.get(Calendar.DAY_OF_MONTH));

			long daysBetween = ChronoUnit.DAYS.between(birthday, today);
			
			if(daysBetween >= 0){
				return daysBetween;
			}else{
				throw new EsfingeAOMException("data maior do que hoje");
			}
		} catch (EsfingeAOMException e) {
			throw e;
		}
	}
}
