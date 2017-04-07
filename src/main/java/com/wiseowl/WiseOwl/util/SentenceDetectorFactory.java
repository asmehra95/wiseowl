/*
 * Copyright 2008-2011 Grant Ingersoll, Thomas Morton and Drew Farris
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
 */
package com.wiseowl.WiseOwl.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SentenceDetectorFactory {

  private static final Logger log = LoggerFactory.getLogger(SentenceDetectorFactory.class);

  SentenceDetector detector;
  
  public SentenceDetectorFactory() throws IOException {
    this(null);
  }
  
  public SentenceDetectorFactory(Map<String, String> param) throws IOException {
    String language = OpenNLPUtil.getModelLanguage(param);
    String modelDirectory = OpenNLPUtil.getModelDirectory(param);
    loadSentenceDetector(language, modelDirectory);
    
  }
  public SentenceDetectorFactory(String language, String modelDirectory) throws IOException {
    loadSentenceDetector(language, modelDirectory);
  }
  /** Obtain a reference to an english sentence detector to use in conjunction 
   *  with the NameFinders
   */
  public SentenceDetector getSentenceDetector() {
    return detector;
  }
  
  /** Load the sentence detector
   * 
   * @param language
   * @param modelDirectory
   * @throws IOException
   */
  protected void loadSentenceDetector(String language, String modelDirectory) throws IOException {
    String modelFile = modelDirectory + 
        File.separatorChar + language + "-sent.bin";
    
    log.info("Loading sentence model {}", modelFile);
    InputStream modelStream = new FileInputStream(modelFile);
    SentenceModel model = new SentenceModel(modelStream);
    detector = new SentenceDetectorME(model);
  }
  
}
