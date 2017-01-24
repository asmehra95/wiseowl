package com.wiseowl.WiseOwl.solr;

import org.apache.lucene.analysis.util.TokenizerFactory;
import org.apache.lucene.util.AttributeFactory;

import com.wiseowl.WiseOwl.util.SentenceDetectorFactory;

import java.io.IOException;
import java.util.Map;

/** This class is used to create an Instance of SentenceTokenizer class which is used by solr for tokenizing*/ 
public class SentenceTokenizerFactory extends TokenizerFactory {
  
	SentenceDetectorFactory sentenceDetectorFactory;
	/** Creates a new SentenceTokenizerFactory */
	public SentenceTokenizerFactory(Map<String,String> args) {
    super(args);
    try {
        sentenceDetectorFactory = new SentenceDetectorFactory(args);
      }
      catch (IOException e) {
    
        e.printStackTrace();
      }
  }
  
  @Override
  public SentenceTokenizer create(AttributeFactory factory) {
    return new SentenceTokenizer(factory, sentenceDetectorFactory.getSentenceDetector());
  }
}