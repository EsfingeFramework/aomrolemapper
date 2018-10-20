package org.esfinge.aom.model.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

	public CalculaAnos(String paramName) {
		this.paramName = paramName;
	}

	@Override
	public Object execute(IEntity obj, Object... params) {
		try {
			IProperty property = obj.getProperty(paramName);
			GenericProperty gn = (GenericProperty) property;
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
				Date dataObj = (Date) gn.getValue();
				gregorianCalendar.setTime(dataObj);
			}

			LocalDate today = LocalDate.now();
			int year = 1;
			int month = 2;
			int day = 5;
			LocalDate birthday = LocalDate.of(gregorianCalendar.get(year), gregorianCalendar.get(month)+1,
					gregorianCalendar.get(day));
			Period p = Period.between(birthday, today);

			return p.getYears() + " anos";
		} catch (EsfingeAOMException e) {
			e.printStackTrace();
		}
		return null;
	}

}
