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
