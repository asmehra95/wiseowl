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
        * The integration code is free to use but you must check with StanfordCoreNLP for licencing issues
        * Modifications are copyright 2016-2017 WiseOwl Team, Avtar Singh, Sumit Kumar and Yuvraj Singh
        * https://www.linkedin.com/in/avtar-singh-6a481a124/
        */
package com.wiseowl.WiseOwl.summarizer;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.stats.ClassicCounter;
import edu.stanford.nlp.stats.Counter;
import edu.stanford.nlp.util.CoreMap;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

/**
 * @author Jon Gauthier
 */
public class Summarizer {

  private static final StanfordCoreNLP pipeline;
  static {
    Properties props = new Properties();
    props.setProperty("annotators", "tokenize,ssplit,pos");
    props.setProperty("tokenize.language", "en");
    props.setProperty("pos.model", "edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger");

    pipeline = new StanfordCoreNLP(props);
  }

  private final Counter<String> dfCounter;
  private final int numDocuments;

  public Summarizer(Counter<String> dfCounter) {
    this.dfCounter = dfCounter;
    this.numDocuments = (int) dfCounter.getCount("__all__");
  }

  private static Counter<String> getTermFrequencies(List<CoreMap> sentences) {
    Counter<String> ret = new ClassicCounter<String>();

    for (CoreMap sentence : sentences)
      for (CoreLabel cl : sentence.get(CoreAnnotations.TokensAnnotation.class))
        ret.incrementCount(cl.get(CoreAnnotations.TextAnnotation.class));

    return ret;
  }

  private class SentenceComparator implements Comparator<CoreMap> {
    private final Counter<String> termFrequencies;

    public SentenceComparator(Counter<String> termFrequencies) {
      this.termFrequencies = termFrequencies;
    }

    public int compare(CoreMap o1, CoreMap o2) {
      return (int) Math.round(score(o2) - score(o1));
    }

    /**
     * Compute sentence score (higher is better).
     */
    private double score(CoreMap sentence) {
      double tfidf = tfIDFWeights(sentence);

      // Weight by position of sentence in document
      int index = sentence.get(CoreAnnotations.SentenceIndexAnnotation.class);
      double indexWeight = 5.0 / index;

      return indexWeight * tfidf * 100;
    }

    private double tfIDFWeights(CoreMap sentence) {
      double total = 0;
      for (CoreLabel cl : sentence.get(CoreAnnotations.TokensAnnotation.class))
        if (cl.get(CoreAnnotations.PartOfSpeechAnnotation.class).startsWith("n"))
          total += tfIDFWeight(cl.get(CoreAnnotations.TextAnnotation.class));

      return total;
    }

    private double tfIDFWeight(String word) {
      if (dfCounter.getCount(word) == 0)
        return 0;

      double tf = 1 + Math.log(termFrequencies.getCount(word));
      double idf = Math.log(numDocuments / (1 + dfCounter.getCount(word)));
      return tf * idf;
    }
  }

  private List<CoreMap> rankSentences(List<CoreMap> sentences, Counter<String> tfs) {
    Collections.sort(sentences, new SentenceComparator(tfs));
    return sentences;
  }

  public String summarize(String document, int numSentences) {
    Annotation annotation = pipeline.process(document);
    List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);

    Counter<String> tfs = getTermFrequencies(sentences);
    sentences = rankSentences(sentences, tfs);

    StringBuilder ret = new StringBuilder();
    for (int i = 0; i < numSentences; i++) {
      ret.append(sentences.get(i));
      ret.append(" ");
    }

    return ret.toString();
  }

  private static final String DF_COUNTER_PATH = "df-counts.ser";

  @SuppressWarnings("unchecked")
  public static Counter<String> loadDfCounter(String path)
    throws IOException, ClassNotFoundException {
    ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
    return (Counter<String>) ois.readObject();
  }

  public static void main(String[] args) throws IOException, ClassNotFoundException {
    Counter<String> dfCounter = loadDfCounter(DF_COUNTER_PATH);
    //String content="An astronomer is a scientist in the field of astronomy who concentrates their studies on a specific question or field outside of the scope of Earth. They look at stars, planets, moons, comets and galaxies, as well as many other celestial objects — either in observational astronomy, in analyzing the data or in theoretical astronomy. Examples of topics or fields astronomers work on include: planetary science, solar astronomy, the origin or evolution of stars, or the formation of galaxies. There are also related but distinct subjects like cosmology which studies the Universe as a whole. Astronomers usually fit into two types: Observational astronomers make direct observations of planets, stars and galaxies, and analyse the data. Theoretical astronomers create and investigate models of things that cannot be observed. Because it takes millions to billions of years for a system of stars or a galaxy to complete a life cycle astronomers have to observe snapshots of different systems at unique points in their evolution to determine how they form, evolve and die. They use this data to create models or simulations to theorize how different celestial bodies work. There are further subcategories inside these two main branches of astronomy such as planetary astronomy, galactic astronomy or cosmology. Academic Historically, astronomy was more concerned with the classification and description of phenomena in the sky, while astrophysics attempted to explain these phenomena and the differences between them using physical laws. Today, that distinction has mostly disappeared and the terms astronomer and astrophysicist are interchangeable. Professional astronomers are highly educated individuals who typically have a Ph.D. in physics or astronomy and are employed by research institutions or universities. They spend the majority of their time working on research, although they quite often have other duties such as teaching, building instruments, or aiding in the operation of an observatory. ";
    String content="The accent in the given name Sebastián is in accordance with Spanish orthography; however, the same rule would require writing Andrés. John Galt John Galt is the primary male hero of Atlas Shrugged. He initially appears as an unnamed menial worker for Taggart Transcontinental, who often dines with Eddie Willers in the employees' cafeteria, and leads Eddie to reveal important information about Dagny Taggart and Taggart Transcontinental. Only Eddie's side of their conversations is given in the novel. Later in the novel, the reader discovers this worker's true identity. Before working for Taggart Transcontinental, Galt worked as an engineer for the Twentieth Century Motor Company, where he secretly invented a generator of usable electric energy from ambient static electricity, but abandoned his prototype, and his employment, when dissatisfied by an easily corrupted novel system of payment. This prototype was found by Dagny Taggart and Hank Rearden. Galt himself remains concealed, throughout much of the novel, in a valley concealed by himself, where he unites the most skillful inventors and business leaders under his leadership. Much of the book's third division is given to his broadcast speech, which presents the author's philosophy of Objectivism.";
    Summarizer summarizer = new Summarizer(dfCounter);
    String result = summarizer.summarize(content, 4);
    System.out.println(result);
  }

}
