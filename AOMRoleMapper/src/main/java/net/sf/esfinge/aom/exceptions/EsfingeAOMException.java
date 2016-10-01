package net.sf.esfinge.aom.exceptions;

public class EsfingeAOMException extends Exception {

	private static final long serialVersionUID = -6216485594220223308L;

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
