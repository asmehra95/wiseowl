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