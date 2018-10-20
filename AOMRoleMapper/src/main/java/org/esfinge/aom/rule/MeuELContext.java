package org.esfinge.aom.rule;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;

import javax.el.ArrayELResolver;
import javax.el.BeanELResolver;
import javax.el.CompositeELResolver;
import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.FunctionMapper;
import javax.el.ListELResolver;
import javax.el.MapELResolver;
import javax.el.ValueExpression;
import javax.el.VariableMapper;

import org.apache.el.ExpressionFactoryImpl;
import org.apache.el.ValueExpressionLiteral;
import org.apache.el.lang.EvaluationContext;
import org.apache.el.lang.FunctionMapperImpl;
import org.apache.el.lang.VariableMapperImpl;

public class MeuELContext extends ELContext {
	private FunctionMapper functionMapper;
	private VariableMapper variableMapper;
	private CompositeELResolver elResolver;

	public MeuELContext(FunctionMapper functionMapper, VariableMapper variableMapper, ELResolver... resolvers) {
		this.functionMapper = functionMapper;
		this.variableMapper = variableMapper;
		elResolver = new CompositeELResolver();
		for (ELResolver resolver : resolvers) {
			elResolver.add(resolver);
		}
	}

	public ELResolver getELResolver() {
		return elResolver;
	}

	public FunctionMapper getFunctionMapper() {
		return functionMapper;
	}

	public VariableMapper getVariableMapper() {
		return variableMapper;
	}

	public static VariableMapper mapearVariaveis(Map<String, Object> vars) {
		VariableMapper mapper = new VariableMapperImpl();
		for (String attributeName : vars.keySet()) {
			if (vars.get(attributeName) != null) {
				Class<?> clazz = vars.get(attributeName).getClass();
				ValueExpressionLiteral expression = new ValueExpressionLiteral(vars.get(attributeName), clazz);
				mapper.setVariable(attributeName, expression);
			}
		}
		return mapper;
	}

	public static FunctionMapper mapearFuncoes(Class<?> clazz) {
		FunctionMapperImpl mapper = new FunctionMapperImpl();
		for (Method m : clazz.getMethods()) {
			if (Modifier.isStatic(m.getModifiers()))
				mapper.addFunction("", m.getName(), m);
		}
		return mapper;
	}

	public static EvaluationContext criarContexto(Class<?> functionClass, Map<String, Object> attributeMap) {
		VariableMapper vMapper = mapearVariaveis(attributeMap);
		FunctionMapper fMapper = mapearFuncoes(functionClass);
		MeuELContext context = new MeuELContext(fMapper, vMapper, new ArrayELResolver(), new ListELResolver(),
				new MapELResolver(), new BeanELResolver());
		return new EvaluationContext(context, fMapper, vMapper);
	}

	public static Object execute(String expr, Class<? extends Object> objectClass, Map<String, Object> map) {
		EvaluationContext ec = MeuELContext.criarContexto(objectClass, map);
		ValueExpression result = new ExpressionFactoryImpl().createValueExpression(ec, expr, Object.class);
		return result.getValue(ec);
	}

}