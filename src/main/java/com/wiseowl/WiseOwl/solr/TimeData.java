/* Copyright 2016-2017 WiseOwl Team, Avtar Singh, Sumit Kumar and Yuvraj Singh
        *
        *    Licensed under the Apache License, Version 2.0 (the "License");
        *    you may not use this file except in compliance with the License.
        *    You may obtain a copy of the License at
        *
        *        http://www.apache.org/licenses/LICENSE-2.0
        *
        *    Unless required by applicable law or agreed to in writing, software
        *    distributed under the License is distributed on an "AS IS" BASIS,
        *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
        *    See the License for the specific language governing permissions and
        *    limitations under the License.
        * -------------------
        * Modifications are copyright 2016-2017 WiseOwl Team, Avtar Singh, Sumit Kumar and Yuvraj Singh
        * https://www.linkedin.com/in/avtar-singh-6a481a124/
        */
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
