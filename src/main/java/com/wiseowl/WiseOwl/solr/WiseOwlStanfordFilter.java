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

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Queue;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.KeywordAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.util.AttributeFactory;
import org.apache.lucene.util.AttributeSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.*;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.time.TimeAnnotations;
import edu.stanford.nlp.time.TimeAnnotator;
import edu.stanford.nlp.time.TimeExpression;

public class WiseOwlStanfordFilter extends TokenFilter {
	
	private transient static Logger log = LoggerFactory.getLogger(WiseOwlStanfordFilter.class);
	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
	private OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);
	private TypeAttribute typeAtt = addAttribute(TypeAttribute.class);
	
	private final PositionIncrementAttribute posIncrAtt = addAttribute(PositionIncrementAttribute.class);
	private StanfordCoreNLP pipeline;
	int tokenOffset,start,end;
	public static final String NE_PREFIX = "NE_";
	private final KeywordAttribute keywordAtt = addAttribute(KeywordAttribute.class);
	private Queue<TokenData> tokenQueue =
			    new LinkedList<TokenData>();
	 private final Queue<AttributeSource.State> stateQueue =
			    new LinkedList<AttributeSource.State>();
	 private Queue<TimeData> timeQueue =
			    new LinkedList<TimeData>();
	private Iterator<TokenData> itr=null;
	private Iterator<TokenData> itr_cpy=null;
	public WiseOwlStanfordFilter(TokenStream ts,StanfordCoreNLP pipeline)
	{
		super(ts);
		this.pipeline=pipeline;
	}

	public static void main(String args[])
	{
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, cleanxml, ssplit,pos,lemma,ner");
		
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		pipeline.addAnnotator(new TimeAnnotator("sutime", props));
		String text = "<mydata> refeer</mydata>today is 12 jan 2016. what is tommorow? Who is Avtar? Does he work at Apple or Google? Sumit was born on 13 feb,2011.";

		Annotation document = new Annotation(text);
		pipeline.annotate(document);
	    System.out.println(document.get(CoreAnnotations.TextAnnotation.class));
	    List<CoreMap> timexAnnsAll = document.get(TimeAnnotations.TimexAnnotations.class);
	    for (CoreMap cm : timexAnnsAll) {
	    List<CoreLabel> tokens = cm.get(CoreAnnotations.TokensAnnotation.class);
	    TimeData td=new TimeData();
	    td.setTime(cm.get(TimeExpression.Annotation.class).getTemporal().toISOString());
	    td.setStart(tokens.get(0).get(CoreAnnotations.CharacterOffsetBeginAnnotation.class));
	    td.setEnd(tokens.get(tokens.size() - 1).get(CoreAnnotations.CharacterOffsetEndAnnotation.class));
	    }
	 
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		for(CoreMap sentence: sentences) {
		  // traversing the words in the current sentence
		  // a CoreLabel is a CoreMap with additional token-specific methods
			System.out.println("in sent");
		  for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
		    // this is the text of the token
			  System.out.println("in token");
		    String word = token.get(TextAnnotation.class);
		    // this is the POS tag of the token
		    String pos = token.get(PartOfSpeechAnnotation.class);
		    // this is the NER label of the token
		    String ne = token.get(NamedEntityTagAnnotation.class);
		    System.out.println("word : "+word+" pos: "+pos+" ner: "+ne);
		    
		  }

		}

	}
	public Iterator findTokens() throws IOException
	{
		/*char[] c = new char[256];
	    int sz = 0;
	    StringBuilder b = new StringBuilder();
	    
	    while ((sz = input.read(c)) >= 0) {
	      b.append(c, 0, sz);
	    }*/
	    //String text = b.toString();
		if (!input.incrementToken()) return null;
	    String text;
	    text = input.getAttribute(CharTermAttribute.class).toString();
		// read some text in the text variable
		//System.out.println("before annotation");
		Annotation document = new Annotation(text);
		// these are all the sentences in this document
		// a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
		pipeline.annotate(document);
		List<CoreMap> timexAnnsAll = document.get(TimeAnnotations.TimexAnnotations.class);
	    for (CoreMap cm : timexAnnsAll) {
	    List<CoreLabel> tokens = cm.get(CoreAnnotations.TokensAnnotation.class);
	    TimeData td=new TimeData();
	    td.setTime(cm.get(TimeExpression.Annotation.class).getTemporal().toString());
	    td.setStart(tokens.get(0).get(CoreAnnotations.CharacterOffsetBeginAnnotation.class));
	    td.setEnd(tokens.get(tokens.size() - 1).get(CoreAnnotations.CharacterOffsetEndAnnotation.class));
	    timeQueue.add(td);
	    }
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		
		//System.out.println("after annotation and sentence getting"+sentences.size());
		for(CoreMap sentence: sentences) {
		  // traversing the words in the current sentence
		  // a CoreLabel is a CoreMap with additional token-specific methods
		  for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
		    // this is the text of the token
			  System.out.println("in token");
		    String word = token.get(TextAnnotation.class);
		    // this is the POS tag of the token
		    String pos = token.get(PartOfSpeechAnnotation.class);
		    // this is the NER label of the token
		    String ne = token.get(NamedEntityTagAnnotation.class);
		   // System.out.println("word : "+word+" pos: "+pos+" ner: "+ne);
		    TokenData tok=new TokenData();
		    tok.setNER(ne);
		    tok.setToken(word);
		    tok.setPOS(pos);
		    tokenQueue.add(tok);
		  }

		}
		Iterator<TokenData> it=tokenQueue.iterator();
		itr_cpy=tokenQueue.iterator();
		tokenOffset=0;
		start=0;
		end=0;
		return it;
	}

	@Override
	public boolean incrementToken() throws IOException {
		if(stateQueue.peek()==null)
		{
			
		if(itr==null)
		   {
			   itr= findTokens();
		   }
		   TokenData tok;
		   if(itr.hasNext()==false)
		   {
			   return false;
		   }else
		   {
			   tok=(TokenData) itr.next();
			   TokenData tok2=null;
			   if(itr_cpy.hasNext())
				   tok2 = (TokenData) itr_cpy.next();
			   String word=tok.getToken();
			   this.start=this.end;
			   clearAttributes();
			   end=start+word.length();
			   offsetAtt.setOffset(start, end);
			   typeAtt.setType("<ALPHANUM>");
			   keywordAtt.setKeyword(true);
			   posIncrAtt.setPositionIncrement(0);
		       String pos=tok.getPOS();
		       if(!pos.equals("."))
			   {
		    	   termAtt.setEmpty().append(pos);
		    	   stateQueue.add(captureState());
			   }
			   
			   if(!tok.getNER().equals("O"))
			    {
			          posIncrAtt.setPositionIncrement(0);
			          String ne=tok.getNER();
				      termAtt.setEmpty().append(NE_PREFIX + ne);
				      stateQueue.add(captureState());
				      if(ne.equals("DATE") )
				      {
				    	  int st=start,en=end;
				    	  
				    	  if(tok==tok2)
				    	  {
				    		  while(itr_cpy.hasNext())
				    		  {
				    			  tok2=(TokenData) itr_cpy.next();
				    			  if(!tok2.getNER().equals("DATE"))
				    			  {
				    				  break;
				    			  }
				    			  st=en;
				    			  en=st+tok2.getToken().length();
				    		  }
				    		  TimeData tm=timeQueue.poll();
				    		  posIncrAtt.setPositionIncrement(0);
							  keywordAtt.setKeyword(true);
						      String timeData=tm.getTime();
							  termAtt.setEmpty().append(timeData);
							  offsetAtt.setOffset(start, en);
							  typeAtt.setType("<ALPHANUM>");
							  stateQueue.add(captureState());
				    	  }
				      }
			    }
			   keywordAtt.setKeyword(false);
			   offsetAtt.setOffset(start, end);
			   typeAtt.setType("<ALPHANUM>");
			   termAtt.setEmpty().append(word);
			   posIncrAtt.setPositionIncrement(1);
			   tokenOffset++;
			   return true;
		   }
		}
		clearAttributes();
		State state = stateQueue.poll();
	    restoreState(state);
	    return true;
		   
	}
	@Override
	 public void reset() throws IOException {
		    super.reset();
		    tokenQueue.clear();
		    timeQueue.clear();
		    stateQueue.clear();
		  }
	 @Override
	  public void end() throws IOException {
	    super.end();
	    posIncrAtt.setPositionIncrement(posIncrAtt.getPositionIncrement() + 1);
	  }
}
