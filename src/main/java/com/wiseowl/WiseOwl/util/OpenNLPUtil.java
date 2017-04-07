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

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpenNLPUtil {
  
  private static final Logger log = LoggerFactory.getLogger(OpenNLPUtil.class);
  private static final String DEFAULT_MODEL_LANGUAGE = "en";
  private static final String DEFAULT_MODEL_DIR = "../../opennlp-models";
  public static String getModelDirectory(Map<String, String> args) {
    String modelDirectory = null;
    if (args != null) {
      modelDirectory = args.get("modelDirectory");
    }
    
    if (modelDirectory == null || modelDirectory.equals("")) {
    	modelDirectory = System.getProperty("model.dir");
    }
    
    if (modelDirectory == null || modelDirectory.equals("")) {
      /*throw new RuntimeException("Configuration Error: modelDirectory argument "
          + "or model.dir system property not set: "+modelDirectory);*/
    	modelDirectory=DEFAULT_MODEL_DIR;
    	
    }
    else {
      log.warn("Model directory is: {}", modelDirectory);
    }
    
    return modelDirectory;
  }

  public static String getModelLanguage(Map<String, String> args) {
    String modelLanguage = null;
    
    if (args != null) {
      args.get("modelLanguage");
    }
    
    if (modelLanguage == null || modelLanguage.equals("")) {
      modelLanguage = System.getProperty("model.language");
    }
    
    if (modelLanguage == null || modelLanguage.equals("")) {
      log.warn("modelLanguage argument or model.language property not set, "
          + "using default: " + DEFAULT_MODEL_LANGUAGE);
      modelLanguage = DEFAULT_MODEL_LANGUAGE;
    } 
    
    return modelLanguage;
  }
}
