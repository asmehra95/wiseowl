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

import java.io.IOException;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.AttributeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wiseowl.WiseOwl.query.PassageRankingComponent;

import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.util.Span;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;

/**
 * Emits the entire input as Senetences
 */
public final class SentenceTokenizer extends Tokenizer {
	private transient static Logger log = LoggerFactory.getLogger(SentenceTokenizer.class);
  private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
  private OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);
  private final PositionIncrementAttribute posIncrAtt = addAttribute(PositionIncrementAttribute.class);
  
  
  private SentenceDetector detector;
  private Span[] sentences = null;
  private char[] inputSentence;
  private int tokenOffset = 0;
  /*to create object of sentence Tokenizer*/
  public SentenceTokenizer() {
  }
  public SentenceTokenizer(AttributeFactory factory, SentenceDetector detector) {
    super(factory);
    this.detector = detector;
  }
  
  public void fillSentences() throws IOException {
	    char[] c = new char[256];
	    int sz = 0;
	    StringBuilder b = new StringBuilder();
	    
	    while ((sz = input.read(c)) >= 0) {
	      b.append(c, 0, sz);
	    }
	    String tmp = b.toString();
	    log.warn("tmp: {} ");
	    inputSentence = tmp.toCharArray();
	    sentences = detector.sentPosDetect(tmp);
	    tokenOffset = 0;
	  }
  @Override
  public final boolean incrementToken() throws IOException {
	  if (sentences == null) {
	      fillSentences();
	    }
	    
	    if (tokenOffset >= sentences.length) {
	      return false;
	    }
	    
	    Span sentenceSpan = sentences[tokenOffset];
	    clearAttributes();
	    int start = sentenceSpan.getStart();
	    int end   = sentenceSpan.getEnd();
	    termAtt.copyBuffer(inputSentence, start, end - start);
	    posIncrAtt.setPositionIncrement(1);
	    offsetAtt.setOffset(start, end);
	    tokenOffset++;
	    
	    return true;
  }
  @Override
  public void reset() throws IOException {
    super.reset();
    sentences = null;
  }
}
