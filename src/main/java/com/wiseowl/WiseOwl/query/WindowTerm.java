package com.wiseowl.WiseOwl.query;


public class WindowTerm implements Cloneable, Comparable<WindowTerm> {
    String term;
    int position;
    int start, end = -1;

    WindowTerm(String term, int position, int startOffset, int endOffset) {
      this.term = term;
      this.position = position;
      this.start = startOffset;
      this.end = endOffset;
    }

    public WindowTerm(String s, int position) {
      this.term = s;
      this.position = position;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
      return super.clone();
    }

    public int compareTo(WindowTerm other) {
      int result = position - other.position;
      if (result == 0) {
        result = term.compareTo(other.term);
      }
      return result;
    }

    @Override
    public String toString() {
      return "WindowEntry{" +
              "term='" + term + '\'' +
              ", position=" + position +
              '}';
    }
  }

