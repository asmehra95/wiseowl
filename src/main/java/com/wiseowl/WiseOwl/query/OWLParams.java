
package com.wiseowl.WiseOwl.query;
/**
 *
 *
 **/
public interface OWLParams {
  public static final String OWL_PREFIX = "Owl.";
  /**
   * The Size of the Passage window around a match, measured in Tokens
   */
  public static final String PRIMARY_WINDOW_SIZE = OWL_PREFIX + "pws";

  public static final String ADJACENT_WINDOW_SIZE = OWL_PREFIX + "aws";

  public static final String SECONDARY_WINDOW_SIZE = OWL_PREFIX + "sws";

  public static final String QUERY_FIELD = OWL_PREFIX + "qf";

  public static final String OWL_ROWS = OWL_PREFIX + "rows";

  public static final String SLOP = OWL_PREFIX + "qSlop";

  public static final String BIGRAM_WEIGHT = OWL_PREFIX + "bw";

  public static final String ADJACENT_WEIGHT = OWL_PREFIX + "aw";

  public static final String SECOND_ADJ_WEIGHT = OWL_PREFIX + "saw";

  public static final String COMPONENT_NAME = "WiseOwl";
}
