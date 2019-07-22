package org.esfinge.aom.model.dynamic.adapted;

import org.esfinge.aom.model.rolemapper.metadata.annotations.RuleClass;
import org.esfinge.aom.model.rolemapper.metadata.annotations.RuleMethod;

@RuleClass
public interface OperacaoSensor{
   
   @RuleMethod
   Object operacao(Sensor s, Object... params);
}
