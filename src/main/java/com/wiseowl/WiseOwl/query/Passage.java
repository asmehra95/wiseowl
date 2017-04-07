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
import java.util.SortedSet;
import java.util.TreeSet;

import com.wiseowl.WiseOwl.query.WindowTerm;

class Passage implements Cloneable {
    int lDocId;
    String field;

    float score;
    SortedSet<WindowTerm> terms = new TreeSet<WindowTerm>();
    SortedSet<WindowTerm> prevTerms = new TreeSet<WindowTerm>();
    SortedSet<WindowTerm> followTerms = new TreeSet<WindowTerm>();
    SortedSet<WindowTerm> secPrevTerms = new TreeSet<WindowTerm>();
    SortedSet<WindowTerm> secFollowTerms = new TreeSet<WindowTerm>();
    SortedSet<WindowTerm> bigrams = new TreeSet<WindowTerm>();

    Passage() {
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
      Passage result = (Passage) super.clone();
      result.terms = new TreeSet<WindowTerm>();
      for (WindowTerm term : terms) {
        result.terms.add((WindowTerm) term.clone());
      }
      result.prevTerms = new TreeSet<WindowTerm>();
      for (WindowTerm term : prevTerms) {
        result.prevTerms.add((WindowTerm) term.clone());
      }
      result.followTerms = new TreeSet<WindowTerm>();
      for (WindowTerm term : followTerms) {
        result.followTerms.add((WindowTerm) term.clone());
      }
      result.secPrevTerms = new TreeSet<WindowTerm>();
      for (WindowTerm term : secPrevTerms) {
        result.secPrevTerms.add((WindowTerm) term.clone());
      }
      result.secFollowTerms = new TreeSet<WindowTerm>();
      for (WindowTerm term : secFollowTerms) {
        result.secFollowTerms.add((WindowTerm) term.clone());
      }
      result.bigrams = new TreeSet<WindowTerm>();
      for (WindowTerm term : bigrams) {
        result.bigrams.add((WindowTerm) term.clone());
      }

      return result;
    }


    public void clear() {
      terms.clear();
      prevTerms.clear();
      followTerms.clear();
      secPrevTerms.clear();
      secPrevTerms.clear();
      bigrams.clear();
    }


  }
