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
package com.wiseowl.WiseOwl.query;

import java.io.IOException;

import org.apache.lucene.index.PostingsEnum;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.search.spans.Spans;
import org.apache.lucene.util.BytesRef;

import com.wiseowl.WiseOwl.solr.NameFilter;

//Not thread-safe, but should be lightweight to build

/**
 * The PassageRankingTVM is a Lucene TermVectorMapper that builds a five different windows around a matching term.
 * This Window can then be used to rank the passages
 */
class WindowBuildingTVM {
  //spanStart and spanEnd are the start and positions of where the match occurred in the document
  //from these values, we can calculate the windows
  int spanStart, spanEnd;
  Passage passage;
  private int primaryWS, adjWS, secWS;


  public WindowBuildingTVM(int primaryWindowSize, int adjacentWindowSize, int secondaryWindowSize) {
    this.primaryWS = primaryWindowSize;
    this.adjWS = adjacentWindowSize;
    this.secWS = secondaryWindowSize;
    passage = new Passage();//reuse the passage, since it will be cloned if it makes it onto the priority queue
  }

  public void map(Terms terms,Spans spans) throws IOException {
  	int primStart = spanStart - primaryWS;
      int primEnd = spanEnd + primaryWS;
      // stores the start and end of the adjacent previous and following
      int adjLBStart = primStart - adjWS;
      int adjLBEnd = primStart - 1;//don't overlap
      int adjUBStart = primEnd + 1;//don't overlap
      int adjUBEnd = primEnd + adjWS;
      //stores the start and end of the secondary previous and the secondary following
      int secLBStart = adjLBStart - secWS;
      int secLBEnd = adjLBStart - 1; //don't overlap the adjacent window
      int secUBStart = adjUBEnd + 1;
      int secUBEnd = adjUBEnd + secWS;
      WindowTerm lastWT = null;
      if(terms!=null)
      {}
  	TermsEnum termsEnum = terms.iterator();
      BytesRef termref = null;
      String term=null;
      
      while ((termref = termsEnum.next()) != null) {
    	term=termsEnum.term().utf8ToString();
    	PostingsEnum postings = termsEnum.postings(null, PostingsEnum.PAYLOADS | PostingsEnum.OFFSETS);
    	postings.nextDoc();
    if (term.startsWith(NameFilter.NE_PREFIX) == false && term.startsWith(PassageRankingComponent.NE_PREFIX_LOWER) == false) {//filter out the types, as we don't need them here
      //construct the windows, which means we need a bunch of 
  	//bracketing variables to know what window we are in
      //start and end of the primary window
        //unfortunately, we still have to loop over the positions
        //we'll make this inclusive of the boundaries, do an upfront check here so
        //we can skip over anything that is outside of all windows
      	//int position=spans.nextStartPosition();
      	int position=postings.nextPosition();
        if (position >= secLBStart && position <= secUBEnd) {
          //fill in the windows
          WindowTerm wt;
          //offsets aren't required, but they are nice to have
          
          if (postings != null){
          //log.warn("terms if postings!=null {}",term);
          wt = new WindowTerm(term, position, postings.startOffset(), postings.endOffset());
          } else {
            wt = new WindowTerm(term, position);
            //log.warn("terms if postings==null {}",term);
          }
          
          if (position >= primStart && position <= primEnd) {//are we in the primary window
            passage.terms.add(wt);
            //we are only going to keep bigrams for the primary window.  You could do it for the other windows, too
            if (lastWT != null) {
              WindowTerm bigramWT = new WindowTerm(lastWT.term + "," + term, lastWT.position);//we don't care about offsets for bigrams
              passage.bigrams.add(bigramWT);
            }
            lastWT = wt;
          } else if (position >= secLBStart && position <= secLBEnd) {
          	//are we in the secondary previous window?
            passage.secPrevTerms.add(wt);
          } else if (position >= secUBStart && position <= secUBEnd) {//are we in the secondary following window?
            passage.secFollowTerms.add(wt);
          } else if (position >= adjLBStart && position <= adjLBEnd) {//are we in the adjacent previous window?
            passage.prevTerms.add(wt);
          } else if (position >= adjUBStart && position <= adjUBEnd) {//are we in the adjacent following window?
            passage.followTerms.add(wt);
          }
        }
      //}
    }}
  }



  public void setExpectations(String field, int numTerms, boolean storeOffsets, boolean storePositions) {
    // do nothing for this example
    //See also the PositionBasedTermVectorMapper.
  }

}

