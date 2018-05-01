import java.util.HashMap;
import java.util.Set;

/**
 * Class to hold movie object
 * Has term frequencies based on movie summaries
 */
public class Movie implements Comparable<Movie> {
  
  private String title;
  private int id;
  private String summary;
 
  /**
   * A hashmap for term frequencies.
   * Maps a term to the number of times this terms appears in this document. 
   */
  private HashMap<String, Integer> tf;
  
  /**
   * The constructor.
   * It does pre-processing on the summary and then processes the term frequencies
   * @param title of the movie
   * @param summary of the movie
   */
  public Movie(String title, String summary) {
    this.title = title;
    this.summary = summary.replaceAll("[^A-Za-z0-9 ]", "").toLowerCase();
    processTF();
  }
  
  /**
   * This class processes the term frequencies based on the summary
   */
  private void processTF() {
    tf = new HashMap<>();
    for (String term : summary.split(" ")) {
      if (!term.equals("")) {
        if (tf.containsKey(term)) {
          tf.put(term, tf.get(term) + 1);
        } else {
          tf.put(term, 1);
        }
      }
    }
  }
  
  /**
   * Get title of movie
   * @return title
   */
  public String getTitle() {
    return this.title;
  }
  
  /**
   * Get id of movie
   * @return id
   */
  public int getId() {
    return this.id;
  }
  
  /**
   * Get summary of movie
   * @return summary
   */
  public String getSummary() {
    return this.summary;
  }
  
  /**
   * This method will return a set of all the terms which occur in this summary.
   * @return a set of all terms in this summary
   */
  public Set<String> getTermList() {
    return tf.keySet();
  }
  
  /**
   * This method will return the term frequency for a given word.
   * If this summary doesn't contain the word, it will return 0
   * @param word The word to look for
   * @return the term frequency for this word in this summary
   */
  public double getTermFrequency(String word) {
    if (tf.containsKey(word)) {
      return tf.get(word);
    } else {
      return 0;
    }
  }

  /**
   * The overriden method from the Comparable interface.
   */
  @Override
  public int compareTo(Movie o) {
    return title.compareToIgnoreCase(o.title);
  }

}
