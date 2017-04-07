/* Copyright 2008-2011 Grant Ingersoll, Thomas Morton and Drew Farris
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
        * To purchase or learn more about Taming Text, by Grant Ingersoll, Thomas Morton and Drew Farris, visit
        * http://www.manning.com/ingersoll
        * This code has been modified and upgraded by WiseOwl Team, Avtar Singh, Sumit Kumar and Yuvraj Singh.
        * Modifications are copyright 2016-2017 WiseOwl Team, Avtar Singh, Sumit Kumar and Yuvraj Singh
        * https://www.linkedin.com/in/avtar-singh-6a481a124/
        */
package com.wiseowl.WiseOwl.solr;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.util.TokenFilterFactory;
import com.wiseowl.WiseOwl.util.NameFinderFactory;
import java.io.IOException;
import java.util.Map;

public class NameFilterFactory extends TokenFilterFactory {
  
 
	private NameFinderFactory nameFactory;
  public NameFilterFactory(Map<String,String> args) {
    super(args);
    if (!args.isEmpty()) {
      throw new IllegalArgumentException("Unknown parameters: " + args);
    }
    try {
        nameFactory = new NameFinderFactory(args);
      }
      catch (IOException e) {
        System.out.println("Can't Load Name Finder Models");
        e.printStackTrace();
      }
  }
  @Override
  public NameFilter create(TokenStream inp) {
    NameFilter nameFilter =new NameFilter(inp,nameFactory.getModelNames(), nameFactory.getNameFinders());
    return nameFilter;
  }

}