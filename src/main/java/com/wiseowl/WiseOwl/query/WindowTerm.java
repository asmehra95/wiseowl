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

