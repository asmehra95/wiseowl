package com.wiseowl.WiseOwl.solr;

public class TimeData {
	String time=null;
	int start=-1,end=-1;
	public String getTime()
	{
		return time;
	}
	public int getStart()
	{
		return start;
	}
	public int getEnd()
	{
		return end;
	}
	public void setTime(String text)
	{
		time=text;
	}
	public void setStart(int text)
	{
		start=text;
	}
	public void setEnd(int text)
	{
			end=text;
	}

}
