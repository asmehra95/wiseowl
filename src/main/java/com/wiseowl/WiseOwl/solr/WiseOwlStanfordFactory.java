package com.wiseowl.WiseOwl.solr;

import java.util.Map;
import java.util.Properties;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.util.TokenFilterFactory;
import org.apache.lucene.analysis.util.TokenizerFactory;
import org.apache.lucene.util.AttributeFactory;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.time.TimeAnnotator;

public class WiseOwlStanfordFactory extends TokenFilterFactory {
	StanfordCoreNLP pipeline=null;
	public WiseOwlStanfordFactory(Map<String,String> args) {
    super(args);
    Properties props = new Properties();
	props.setProperty("annotators", "tokenize, cleanxml, ssplit,pos,lemma,ner");
	this.pipeline = new StanfordCoreNLP(props);
	pipeline.addAnnotator(new TimeAnnotator("sutime", props));
  }


	@Override
	public TokenStream create(TokenStream ts) {
		return new WiseOwlStanfordFilter(ts,pipeline);
	}

}
  