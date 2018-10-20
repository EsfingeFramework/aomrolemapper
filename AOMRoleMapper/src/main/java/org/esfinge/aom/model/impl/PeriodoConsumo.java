package org.esfinge.aom.model.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.api.model.IProperty;
import org.esfinge.aom.api.model.RuleObject;
import org.esfinge.aom.exceptions.EsfingeAOMException;

public class PeriodoConsumo implements RuleObject {

	String paramName;

	public PeriodoConsumo(String paramName) {
		this.paramName = paramName;
	}

	@Override
	public Object execute(IEntity obj, Object... params) {
		try {
			IProperty property = obj.getProperty(paramName);
			GregorianCalendar gregorianCalendar = new GregorianCalendar();
			if (property.getValue() instanceof String) {
				try {
					String value = (String) property.getValue();
					Date parse = new SimpleDateFormat("dd/MM/yyyy").parse(value);
					gregorianCalendar.setTime(parse);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			} else if (property.getValue() instanceof Date) {
				gregorianCalendar.setTime((Date) property.getValue());
			}

			LocalDate today = LocalDate.now();
			LocalDate birthday = LocalDate.of(gregorianCalendar.get(Calendar.YEAR),
					gregorianCalendar.get(Calendar.MONTH)+ 1, gregorianCalendar.get(Calendar.DAY_OF_MONTH));

			long daysBetween = ChronoUnit.DAYS.between(birthday, today);

			return daysBetween + " dias";
		} catch (EsfingeAOMException e) {
			e.printStackTrace();
		}
		return null;
	}

}
