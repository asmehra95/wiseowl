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
