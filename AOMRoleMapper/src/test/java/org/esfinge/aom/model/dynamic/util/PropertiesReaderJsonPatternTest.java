package org.esfinge.aom.model.dynamic.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.esfinge.aom.model.dynamic.utils.PropertiesReaderJsonPattern;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;

public class PropertiesReaderJsonPatternTest {
	
	private PropertiesReaderJsonPattern readerJson;
    private String fileName;		

    @Test
	@Before
	public void readJSONFile(){
	    fileName = "JsonMapTest.json";
	    try {
			readerJson = PropertiesReaderJsonPattern.getInstance(fileName);
		} catch (ParseException | IOException e) {
			e.printStackTrace();
		}
	}
		
	@Test
	public void readProperty(){		
		assertEquals(readerJson.readProperty("entity"), "javax.persistence.Entity");
		assertEquals(readerJson.readProperty("column"), "javax.persistence.Column");
		assertEquals(readerJson.readProperty("oneToOne"), "javax.persistence.OneToOne");
	}
	
	@Test
	public void readPropertyParameters(){	
		String[] values = readerJson.readPropertyParameters("column");
		assertEquals(values[0], "name");
		assertEquals(values[1], "nullable");
	}
	
	@Test
	public void readNotPropertyParameters(){	
		String[] values = readerJson.readPropertyParameters("table");
		assertNull(values);
	}
	
	@Test
	public void readPropertyTarget(){	
		assertTrue(readerJson.isPropertyTarget("column", "method"));
	}
	
	@Test
	public void readWrongPropertyTarget(){	
		assertFalse(readerJson.isPropertyTarget("column", "class"));
	}
}