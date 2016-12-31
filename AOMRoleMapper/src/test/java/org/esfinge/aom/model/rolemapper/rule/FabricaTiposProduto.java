package org.esfinge.aom.model.rolemapper.rule;

import java.util.Date;

import org.esfinge.aom.api.model.IEntityType;
import org.esfinge.aom.exceptions.EsfingeAOMException;
import org.esfinge.aom.model.impl.GenericEntityType;
import org.esfinge.aom.model.impl.GenericPropertyType;

public class FabricaTiposProduto {

	public static IEntityType getTipoEntidadeCobranca() throws EsfingeAOMException{
		IEntityType produto = new GenericEntityType("Produto");
	
		//criando property types
		GenericPropertyType dataNascPropertyType = new GenericPropertyType("dataFabricacao", Date.class);
		dataNascPropertyType.setProperty("notempty", true);

		GenericPropertyType nomePropertyType = new GenericPropertyType("nome", String.class);
		nomePropertyType.setProperty("notempty", true);
					
		//adicionando property types no tipo de entidade
		produto.addPropertyType(dataNascPropertyType);
		produto.addPropertyType(nomePropertyType);
		
		return produto;
	}
	
}
