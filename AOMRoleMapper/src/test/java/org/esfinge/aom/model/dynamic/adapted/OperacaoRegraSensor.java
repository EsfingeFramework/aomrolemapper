package org.esfinge.aom.model.dynamic.adapted;

public class OperacaoRegraSensor implements OperacaoSensor {

	@Override
	public Object operacao(Sensor s, Object... params) {
		return 10;
	}

//	public Object execute(IEntity obj, Object... params) throws EsfingeAOMException {
//		return operacao( obj.getAssociatedObject() != null ? (Sensor) obj.getAssociatedObject() : null, params);
//	}

}
