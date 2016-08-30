package org.esfinge.aom.exceptions;

public class EsfingeAOMException extends Exception {

	public EsfingeAOMException()
	{		
	}
	
	public EsfingeAOMException(String msg)
	{
		super(msg);
	}
	
	public EsfingeAOMException(Throwable e)
	{
		super(e);
	}
	
	public EsfingeAOMException(String msg, Throwable e)
	{
		super(msg, e);
	}
	
}
