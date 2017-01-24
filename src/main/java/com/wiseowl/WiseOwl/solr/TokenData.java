package com.wiseowl.WiseOwl.solr;

public class TokenData {
	private String TokenText=null;
	private String ner=null;
	private String pos=null;
	public String getToken()
	{
		return TokenText;
	}
	public String getNER()
	{
		return ner;
	}
	public String getPOS()
	{
		return pos;
	}
	public void setToken(String text)
	{
		TokenText=text;
	}
	public void setNER(String text)
	{
		ner=text;
	}
	public void setPOS(String text)
	{
			pos=text;
	}

}
