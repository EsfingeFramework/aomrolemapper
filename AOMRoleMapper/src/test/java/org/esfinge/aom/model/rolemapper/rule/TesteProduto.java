package org.esfinge.aom.model.rolemapper.rule;

import static org.junit.Assert.assertTrue;

import java.util.GregorianCalendar;

import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.api.model.IEntityType;
import org.esfinge.aom.exceptions.EsfingeAOMException;
import org.esfinge.aom.model.impl.CalculaAnos;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TesteProduto {

	private static IEntityType tipoProduto;
	private IEntity produto; 
	
	@BeforeClass
	public static void criarTipoEntidade() throws EsfingeAOMException{
		tipoProduto = FabricaTiposProduto.getTipoEntidadeCobranca();
		tipoProduto.addOperation("anosFabricacao", new CalculaAnos("dataFabricacao"));
	}
		
	@Before
	public void criarProdutoCorreto() throws EsfingeAOMException{
		produto = tipoProduto.createNewEntity();
		produto.setProperty("nome", "Notebook DELL");
		GregorianCalendar dataFabr = new GregorianCalendar();
		dataFabr.set(2010, 11, 23);
		produto.setProperty("dataFabricacao", dataFabr.getTime());	
	}

	@Test
	public void validaProdutoBase() throws Exception{
		Object resultOperation = produto.executeOperation("anosFabricacao");
		int years = (int) resultOperation;
		System.out.println(" rule object anosFabricacao retornou " + years);
		boolean result = years < 7;
		assertTrue("produto deve ser preenchido corretamente", result);
	}

	@Test
	public void validaProdutoBase2() throws Exception{
		produto.addPropertyMonitored("dataFabricacao", "anosFabricacao");
		
		GregorianCalendar dataFabr = new GregorianCalendar();
		dataFabr.set(2012, 11, 23);
		produto.setProperty("dataFabricacao", dataFabr.getTime());	
		Object resultOperation = produto.getResultOperation("anosFabricacao");
		
		System.out.println(" @@@@ rule object anosFabricacao retornou " + resultOperation);
		assertTrue(resultOperation != null);
	}
}
