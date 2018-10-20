package org.esfinge.aom.model.dynamic.factory;

import java.util.HashMap;
import java.util.Map;

import javax.el.ValueExpression;

import org.apache.el.ExpressionFactoryImpl;
import org.apache.el.lang.EvaluationContext;
import org.esfinge.aom.rule.MeuELContext;
import org.junit.Test;

public class DynamicELTest {

	@Test
	public void testEL() {
		Map<String, Object> mapa = new HashMap<>();
		mapa.put("a", 23);
		mapa.put("b", 10);
		String expr = "${a+duplicar(b)}";
		EvaluationContext ec = MeuELContext.criarContexto(DynamicELTest.class, mapa);
		ValueExpression result = new ExpressionFactoryImpl().createValueExpression(ec, expr, int.class);
		System.out.println(result.getValue(ec));
	}


	@Test
	public void testELFormula() {
		Map<String, Object> mapa = new HashMap<>();
		mapa.put("a", 23);
		mapa.put("b", 10);
		String expr = "${a*b+b}";
		EvaluationContext ec = MeuELContext.criarContexto(DynamicELTest.class, mapa);
		ValueExpression result = new ExpressionFactoryImpl().createValueExpression(ec, expr, Object.class);
		System.out.println(result.getValue(ec));
	}


	public static int duplicar(int i) {
		return 2 * i;
	}
}
