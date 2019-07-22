package org.esfinge.aom.model.rolemapper.rule;

import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.GregorianCalendar;

import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.api.model.IEntityType;
import org.esfinge.aom.exceptions.EsfingeAOMException;
import org.esfinge.aom.model.impl.CalculaAnos;
import org.esfinge.aom.model.impl.GenericEntityType;
import org.esfinge.aom.model.impl.GenericPropertyType;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TesteProduto {

	private static IEntityType tipoProduto;
	private IEntity produto;

	@BeforeClass
	public static void criarTipoEntidade() throws EsfingeAOMException {
		tipoProduto = FabricaTiposProduto.getTipoEntidadeCobranca();
		tipoProduto.addOperation("anosFabricacao", new CalculaAnos("dataFabricacao"));
	}

	@Before
	public void criarProdutoCorreto() throws EsfingeAOMException {
		IEntityType tipoProduto = new GenericEntityType("Produto");

		// adicionando RuleObject ao tipo da entidade
		tipoProduto.addOperation("anosFabricacao", new CalculaAnos("dataFabricacao"));

		// criando os tipos dataFabricao e nome na propriedade
		GenericPropertyType dataNascPropertyType = new GenericPropertyType("dataFabricacao", Date.class);
		dataNascPropertyType.setProperty("notempty", true);
		GenericPropertyType nomePropertyType = new GenericPropertyType("nome", String.class);
		nomePropertyType.setProperty("notempty", true);

		// adicionando property types no tipo de entidade
		tipoProduto.addPropertyType(dataNascPropertyType);
		tipoProduto.addPropertyType(nomePropertyType);

		// criando a entidade AOM
		produto = tipoProduto.createNewEntity();
		produto.setProperty("nome", "Notebook DELL");
		GregorianCalendar dataFabr = new GregorianCalendar();
		dataFabr.set(2010, 11, 23);
		produto.setProperty("dataFabricacao", dataFabr.getTime());

		// executando a regra anosFabricacao
		Object resultOperation = produto.executeOperation("anosFabricacao");
		String years = (String) resultOperation;
		System.out.println(" rule object anosFabricacao retornou " + years);
		boolean result = years != null;
		assertTrue("produto deve ser preenchido corretamente", result);
	}

	@Test
	public void testaRegraAnosFabricacao() throws EsfingeAOMException {
		IEntityType tipoProduto = new GenericEntityType("Produto");

		// adicionando RuleObject ao tipo da entidade
		tipoProduto.addOperation("anosFabricacao", new CalculaAnos("dataFabricacao"));

		// criando os tipos dataFabricao e nome na propriedade
		GenericPropertyType dataNascPropertyType = new GenericPropertyType("dataFabricacao", Date.class);
		dataNascPropertyType.setProperty("notempty", true);
		GenericPropertyType nomePropertyType = new GenericPropertyType("nome", String.class);
		nomePropertyType.setProperty("notempty", true);

		// adicionando property types no tipo de entidade
		tipoProduto.addPropertyType(dataNascPropertyType);
		tipoProduto.addPropertyType(nomePropertyType);

		// criando a entidade AOM
		produto = tipoProduto.createNewEntity();
		produto.setProperty("nome", "Notebook DELL");
		GregorianCalendar dataFabr = new GregorianCalendar();
		dataFabr.set(2010, 11, 23);
		produto.setProperty("dataFabricacao", dataFabr.getTime());

		// executando a regra anosFabricacao
		Object resultOperation = produto.executeOperation("anosFabricacao");
		String years = (String) resultOperation;
		boolean result = years != null;
		assertTrue("produto deve ser preenchido corretamente", result);
	}

	@Test
	public void validaProdutoBase() throws Exception {
		Object resultOperation = produto.executeOperation("anosFabricacao");
		String years = (String) resultOperation;
		System.out.println(" rule object anosFabricacao retornou " + years);
		boolean result = years != null;
		assertTrue("produto deve ser preenchido corretamente", result);
	}

	@Test
	public void validaProdutoBase2() throws Exception {
		produto.addPropertyMonitored("dataFabricacao", "anosFabricacao");

		GregorianCalendar dataFabr = new GregorianCalendar();
		dataFabr.set(2012, 11, 23);
		produto.setProperty("dataFabricacao", dataFabr.getTime());
		Object resultOperation = produto.getResultOperation("anosFabricacao");

		System.out.println(" @@@@ rule object anosFabricacao retornou " + resultOperation);
		assertTrue(resultOperation != null);
	}

	@Test
	public void validaProdutoBase3() throws Exception {
		IEntityType tipoProduto = new GenericEntityType("Produto");

		// adicionando RuleObject ao tipo da entidade
		tipoProduto.addOperation("anosFabricacao", new CalculaAnos("dataFabricacao"));

		// criando os tipos dataFabricao e nome na propriedade
		GenericPropertyType dataNascPropertyType = new GenericPropertyType("dataFabricacao", Date.class);
		dataNascPropertyType.setProperty("notempty", true);
		GenericPropertyType nomePropertyType = new GenericPropertyType("nome", String.class);
		nomePropertyType.setProperty("notempty", true);

		// adicionando property types no tipo de entidade
		tipoProduto.addPropertyType(dataNascPropertyType);
		tipoProduto.addPropertyType(nomePropertyType);

		produto = tipoProduto.createNewEntity();
		produto.setProperty("nome", "Notebook DELL");
		GregorianCalendar dataFabr = new GregorianCalendar();
		dataFabr.set(2010, 11, 23);
		produto.setProperty("dataFabricacao", dataFabr.getTime());

		// executando a regra anosFabricacao
		Object resultOperation = produto.executeOperation("anosFabricacao");
		String years = (String) resultOperation;
		boolean result = years != null;
		assertTrue("produto deve ser preenchido corretamente", result);
		produto.addPropertyMonitored("dataFabricacao", "anosFabricacao");

		// GregorianCalendar dataFabr = new GregorianCalendar();
		dataFabr.set(2012, 11, 23);
		produto.setProperty("dataFabricacao", dataFabr.getTime());
		resultOperation = produto.getResultOperation("anosFabricacao");

		assertTrue(resultOperation != null);
	}

	@Test
	public void exemploTypeSquare() throws Exception {
		// criando o tipo de entidade
		IEntityType tipoSensor = new GenericEntityType("Magnetometro");

		// criando os tipos de propriedades
		GenericPropertyType direcaoPropertyType = new GenericPropertyType("direcao", String.class);
		GenericPropertyType intensidadePropertyType = new GenericPropertyType("intensidade", Integer.class);
		GenericPropertyType sentidoPropertyType = new GenericPropertyType("sentido", Integer.class);

		// adicionando property types no tipo de entidade
		tipoSensor.addPropertyType(direcaoPropertyType);
		tipoSensor.addPropertyType(intensidadePropertyType);
		tipoSensor.addPropertyType(sentidoPropertyType);

		// Criando a entidade Magnetometro
		IEntity magnetometro = tipoSensor.createNewEntity();

		// setando valores nas propriedades
		magnetometro.setProperty("direcao", "NORTE");
		magnetometro.setProperty("sentido", 350);
		magnetometro.setProperty("intensidade", 50);
	}
}
